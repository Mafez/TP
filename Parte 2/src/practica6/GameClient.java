package practica6;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import practica6.Response.Response;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.control.commands.PlayCommand;
import es.ucm.fdi.tp.basecode.bgame.control.commands.QuitCommand;
import es.ucm.fdi.tp.basecode.bgame.control.commands.RestartCommand;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class GameClient extends Controller implements Observable<GameObserver> {
	
	private String host;
	private int port;
	private List<GameObserver> observers; // Las notificaciones que manda el servidor se reenvían a todos los observadores
	private Piece localPiece;
	private GameFactory gameFactory;
	private Connection connectionToServer; // Conexion al servidor
	private boolean gameOver; // Indica si el juego ha terminado
	
	public GameClient(String host, int port) throws Exception {
		super(null, null);
		this.host = host;
		this.port = port;
		this.observers = new ArrayList<GameObserver>();
		connect();
	}
	
	public GameFactory getGameFactory() {
		// Consultar el valor de gameFactory
		return gameFactory;
	}
	public Piece getPlayerPiece() {
		// Consultar el valor de localPiece
		return localPiece;
	}

	// Las vistas usan estos metodos para registrarse a recibir notificaciones ▼
	
	@Override
	public void addObserver(GameObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(GameObserver o) {
		observers.add(o);
	}
	
	// Cuando la vistas invocan estos metodos de GameClient, GameClient reenvía la petición a GameServer para ejecutar su GameMove ▼
	
	@Override
	public void makeMove(Player p) {
		forwardCommand(new PlayCommand(p));
	}
	
	@Override
	public void stop() {
		forwardCommand(new QuitCommand());
	}
	
	@Override
	public void restart() {
		forwardCommand(new RestartCommand());
	}
	
	private void forwardCommand(Command cmd) {
		// Si el juego no ha acabado reenvíar la petición a GameServer
		if(!gameOver) {
			try {
				connectionToServer.sendObject(cmd);
			} catch (Exception e) {
				throw new GameError("Algo fue mal enviando un comando" + e.getMessage());
			}
		}
	}
	
	// Cuando la vistas invocan estos metodos de GameClient, GameClient reenvía la petición a GameServer para ejecutar su GameMove ▲
	
	private void connect() throws Exception {
		connectionToServer = new Connection(new Socket(host, port)); // Da problemas al crear la conexion con el host "localhost"
		
		connectionToServer.sendObject("Connect"); // Enviar el String "Connect" para expresar su interes por jugar
		
		Object response = connectionToServer.getObject(); // Leemos el primer objeto en la respuesta del servidor
		if(response instanceof Exception) {
			throw (Exception) response;
		}
		
		// Si no es una instancia de Exception, sería el string "OK" seguido por GameFactory y Piece que el cliente tiene que usar
		try {
			gameFactory = (GameFactory) connectionToServer.getObject();
			localPiece = (Piece) connectionToServer.getObject();
		} catch(Exception e) {
			throw new GameError("Unknown server response: " + e.getMessage());
		}
	}
	
	public void start() {
		/**
		 * Crear una instancia anonima de GameObserver y registrarla como observadora de GameClient se refiere a lo de abajo?
		 */
		this.observers.add(new GameObserver() {

			@Override
			public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {	}

			@Override
			public void onGameOver(Board board, State state, Piece winner) {
				gameOver = true;
				// Cerrar la conexion con el servidor:
				try {
					connectionToServer.stop();
				} catch (Exception e) {
					throw new GameError("Excepcion capturada intentando cerrar la conexion con el servidor " + e.getMessage());
				}
			}
			
			@Override
			public void onMoveStart(Board board, Piece turn) { }

			@Override
			public void onMoveEnd(Board board, Piece turn, boolean success) { }

			@Override
			public void onChangeTurn(Board board, Piece turn) {	}

			@Override
			public void onError(String msg) { }			
		});
		
		gameOver = false;
		while(!gameOver) {
			try {
				Response res = (Response) connectionToServer.getObject(); // read a response
				for(GameObserver o: observers) {
					// execute the response on the server
					res.run(o);
				}
			} catch (Exception e) {
				throw new GameError("Algo fue mal leyendo la respuesta del servidor" + e.getMessage());
			}
		}
	}
}
