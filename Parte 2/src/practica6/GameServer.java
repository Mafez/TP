package practica6;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import practica6.Response.ChangeTurnResponse;
import practica6.Response.ErrorResponse;
import practica6.Response.GameOverResponse;
import practica6.Response.GameStartResponse;
import practica6.Response.MoveEndResponse;
import practica6.Response.MoveStartResponse;
import practica6.Response.Response;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class GameServer extends Controller implements GameObserver {
	private int port; // Puerto usado por el servidor
	private int numPlayers; // Num de jugadores (necesario para iniciar el juego)
	private int numOfConnectedPlayers; // Num de jugadores conectados (se usa para saber cuando hay que iniciar el juego)
	private GameFactory gameFactory;
	private List<Connection> clients; // Lista de clientes conectados
	private JTextArea infoArea;
	
	volatile private ServerSocket server; // Referencia al servidor
	volatile private boolean stopped; // Indica si el servidor ha sido apagado
	volatile private boolean gameOver; // Indica si el juego ha terminado 
	private boolean primera = true;
	
	public GameServer(GameFactory gameFactory, List<Piece> pieces, int port) {
		super(new Game(gameFactory.gameRules()), pieces);
		// initialise the fields with corresponding values
		this.numOfConnectedPlayers = 0;
		this.gameFactory = gameFactory;
		this.port = port;
		this.numPlayers = pieces.size();
		this.clients = new ArrayList<Connection>();
		game.addObserver(this);
	}

	// sobreescribimos los metodos de controller de la super clase para poder capturar excepciones, podemos perder la conexion si se lanza una excepcion
	
	@Override
	public synchronized void makeMove(Player player) {
		try { 
			super.makeMove(player); 
		} catch (GameError e) { 
			log("Algo fue mal al intentar hacer un movimiento" + e.getMessage());
		}
	}
	
	@Override
	public synchronized void stop() {
		try { 
			super.stop(); 
		} catch (GameError e) {
			log("Algo fue mal al intentar parar el controlador" + e.getMessage());
		}
	}
	
	@Override
	public synchronized void restart() {
		try { 
			super.restart(); 
		} catch (GameError e) {
			log("Algo fue mal al intentar resetear la partida" + e.getMessage());
		}
	}
	
	@Override
	public void start() {
		controlGUI();
		startServer();
	}
	
	private void controlGUI() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					constructGUI();
				}
			});
		} catch(InvocationTargetException | InterruptedException e) {
			throw new GameError("Something went wrong when constructing the GUI");
		}
	}
	
	private void constructGUI() {
		JFrame window = new JFrame("Game Server");
		JPanel panel = new JPanel(new BorderLayout());
		
		// Text area infoArea para pintar mensajes
		infoArea = new JTextArea();
		infoArea.setEditable(false);
		
		JButton quitButton = new JButton("Stop Server");
		quitButton.setPreferredSize(new Dimension(100, 100));
		
		// Implementamos el actionListener del boton, cuando se pulsa se para el servidor y se sale de la aplicación
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 1º Paramos el servidor:
				stopped = true;
				// Paramos el juego:
				if(!gameOver) { // Si el juego esta InPlay:
					stop();
					gameOver = true;
					for(Connection c: clients) { // Para cada cliente:
						try {
							c.stop();
							//numOfConnectedPlayers = 0;
						} catch (Exception e1) {
							log("Algo fue mal desconectando a un cliente" + e1.getMessage());
						}
					}
				}
				// Apagamos el servidor:
				try {
					server.close();
				} catch (IOException e1) { 
					log("Algo fue mal al intentar apagar el servidor" + e1.getMessage());
				}
				// 2º Salimos de la aplicacion:
				stopped = true;
				// Cerramos el servidor
				try {
					server.close();
				} catch (IOException e1) {
					log("Algo fue mal al intentar cerrar el servidor" + e1.getMessage());
				}
				window.dispose();
			}
		});
		
		panel.add(infoArea, BorderLayout.CENTER);
		panel.add(quitButton, BorderLayout.SOUTH);
		window.add(panel);
		window.setLocation(750, 300);
		window.setPreferredSize(new Dimension(600, 600));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}
	
	private void log(String msg) {
		// Usar este metodo desde todas partes del GameServer para añadir mensajes a infoArea (usar invokeLater)
		this.infoArea.append(" " + msg + "\n");
	}
	
	private void startServer() {
		try {
			server = new ServerSocket(port);
		} catch (IOException e1) {
			log("Algo fue mal al intentar arrancar un Socket" + e1.getMessage()); // AQUI SALTA LA EXCEPCION
		}
		stopped = false;
		
		while(!stopped) {
			try {
				// 1. Accept a conection into a socket s
				Socket s = server.accept(); // Esperamos a que un cliente se conecte con el metodo accept()
				// 2. log a corresponding message
				log("Conexion aceptada");
				// 3. call handleRequest(s)
				handleRequest(s); // Pasar el socket del servidor a handleRequest() para responder a la peticion.
			} catch(Exception e) {
				if(!stopped) {
					log("error while waiting for a connection: " + e.getMessage());
				}
			}
		}
	}
	
	private void handleRequest(Socket s) throws Exception {
		try {
			Connection c = new Connection(s);
			
			Object clientRequest = c.getObject(); // El primer mensaje del cliente ha de ser el String "Connect"
			log("El primer mensaje del cliente es: " + clientRequest);
			if(!(clientRequest instanceof String) && !((String) clientRequest).equalsIgnoreCase("Connect")) {
				c.sendObject(new GameError("Invalid Request"));
				c.stop();
				return;
			}

			/*// 1. Si el nº de clientes conectados ya ha alcanzado el maximo, respondemos con un GameError adecuado.
			if(numOfConnectedPlayers == clients.size()-1) { // clientes (jugadores) conectados como maximo
				log("Numero maximo de clientes conectados alcanzado");
			}*/
			numOfConnectedPlayers++; // 2. Incrementar el nº de clientes conectados 
			clients.add(c); // 2. Añadir 'c' a la lista de clientes.
			// 3. Enviar el String "OK" al cliente, seguido por el GameFactory
			c.sendObject("OK");
			c.sendObject(gameFactory);
			// Asignamos al i-esimo cliente la i-esima ficha (de la lista pieces) // Enviar el piece a usar
			c.sendObject(pieces.get((clients.size()-1)));
			// 4.
			if(numOfConnectedPlayers == numPlayers) { // Si hay un nº suficiente de clientes
				log("Numero maximo de clientes conectados alcanzado");
				// Iniciar el juego (la primera vez usando start, despues usando restart)
				if(primera) {
					super.start();
					primera = false;
				} else {
					this.restart();
				}
			}
			startClientListener(c); // 5. Invocar a startClientListener para iniciar una hebra y recibir comandos del cliente
		} catch(IOException | ClassNotFoundException e) {
			log("Excepcion capturada procesando la peticion del cliente" + e.getMessage());
		}
	}
	
	private void startClientListener(Connection c) {
		gameOver = false;
		// Iniciar una hebra para ejecutar el bucle de abajo mientras el juego no haya terminado y el servidor no haya sido parado.
		Thread t = new Thread() {
			@Override
			public void run() {
				while(!stopped && !gameOver) {
					try{
						Command cmd;
						// 1. Recibir un object del cliente y hacer casting a Command
						cmd = (Command) c.getObject();
						// 2. Ejecutar cmd.exec pasándole el Controller GameServer.this, exec llamará a makeMove, stop, etc.
						cmd.execute(GameServer.this);
					} catch (Exception e) {
						if(!stopped && !gameOver) {
							// Paramos el juego (no el servidor):
							for(Connection co: clients) { // Para cada cliente:
								try {
									co.stop();
								} catch (Exception e1) {
									log("Algo fue mal desconectando a un cliente" + e1.getMessage());
								}
							}
						}
					}
				}
			}
		};
		t.start();
	}
	
	/**
	 * Comprobar si estan bien implementados los metodos Response
	 */

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		forwardNotification(new GameStartResponse(board, gameDesc, pieces, turn));
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		forwardNotification(new GameOverResponse(board, state, winner));
		// Además de reenviar la notificación, paramos el juego
		this.stop(); // stop the game
		// Se acaba el juego:
		log("Se han desconectado los clientes"); // Mostramos un mensaje en el servidor
		for(Connection co: clients) { // Para cada cliente:
			try {
				co.stop(); // Paramos la conexion
			} catch (Exception e1) {
				log("Algo fue mal desconectando a un cliente" + e1.getMessage());
			}
		}
		this.numOfConnectedPlayers = 0; // ponemos el atributo de jugadores conectados a 0
		this.clients = new ArrayList<Connection>(); // Inicializamos otra vez el arraylist de conexiones de clientes
		gameOver = true;
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		forwardNotification(new MoveStartResponse(board, turn));
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		forwardNotification(new MoveEndResponse(board, turn, success));
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		forwardNotification(new ChangeTurnResponse(board, turn));
	}

	@Override
	public void onError(String msg) {
		forwardNotification(new ErrorResponse(msg));
	}
	
	void forwardNotification(Response r) {
		// Envía el objecto Response correspondiente a todos los clientes
		for(Connection c: clients) { // Para cada cliente:
			try {
				c.sendObject(r);
			} catch (Exception e) {
				log("Excepcion capturada enviando el objeto response de un cliente");
			}
		}
	}
}
