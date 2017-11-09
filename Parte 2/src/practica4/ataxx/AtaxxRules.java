package practica4.ataxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import practica4.accesorios.Constantes;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.FiniteRectBoard;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Pair;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Reglas del juego Ataxx. 
 * Las dimendiones del tablero no pueden ser menores a 5.
 * El numero de jugadores esta entre 2 y 4.
 */
public class AtaxxRules implements GameRules {
	
	private int dimR;
	private int dimC;
	private int obstaculos;
	
	public AtaxxRules(int dimR, int dimC, int obstaculos) {
		if (dimR < Constantes.dimMinima && dimC < Constantes.dimMinima) { // Comprobamos que las dimensiones sean >= 5
			throw new GameError("Dimensions must be at least 5: " + dimR + " " + dimC);
		} else {
			this.dimR = dimR;
			this.dimC = dimC;
			this.obstaculos = obstaculos;
		}
	}

	@Override
	public String gameDesc() {
		return "Ataxx ";
	}
	
	/**
	 * Metodo que crea un tablero del juego Ataxx dependiendo del numero de jugadores, tambien se 
	 * crean los obstaculos que se hayan indicado por el par�metro -o
	 */
	@Override
	public Board createBoard(List<Piece> pieces) {
		Board board = new FiniteRectBoard(dimR, dimC);
		// Creamos piezas segun el numero de jugadores:
		if(pieces.size() == 2) { // Dos jugadores
			this.initializePieces(board, pieces);
		} else if(pieces.size() == 3) { // Tres jugadores
			this.initializePieces(board, pieces);
			// No crear fichas nuevas, usar la lista de jugadores pieces.get(n�jugador)
			Piece piezaR = pieces.get(2);
			board.setPosition(board.getRows()/2, 0, piezaR);
			board.setPosition(board.getRows()/2, board.getCols() - 1, piezaR);
		} else if(pieces.size() == 4) { // Cuatro jugadores
			this.initializePieces(board, pieces);
			Piece piezaR = pieces.get(2);
			board.setPosition(board.getRows()/2, 0, piezaR);
			board.setPosition(board.getRows()/2, board.getCols() - 1, piezaR);
			Piece piezaB = pieces.get(3);
			board.setPosition(0, board.getCols()/2, piezaB);
			board.setPosition(board.getRows() - 1, board.getCols()/2, piezaB);
		}
		// Creamos obstaculos segun el numero de obstaculos:
		this.createObstacles(board);
		if(!this.posibleGame(board, pieces)) {
			throw new GameError("Any player can move, imposible to play");
		}
		return board;
	}
	
	/**
	 * Metodo que crea los obstaculos indicados con el par�metro -o en el tablero dado.
	 * @param board
	 * @return board
	 */
	private Board createObstacles(Board board) {
		Random r = new Random();
		int row = dimR, col = dimC;
		int randomRow, randomCol; // Creamos una fila, columna aleatorias
		Piece obstacle = new Piece("*");
			while(obstaculos > 0) {
				randomRow = r.nextInt(row); // Creamos una fila, columna aleatorias
				randomCol = r.nextInt(col); 
				if(randomRow/2 - 1 >= 0 && randomCol/2 - 1 >= 0) { // Comprobamos que la posicion aleatoria este dentro de algun cuadrante:
					if(board.getPosition(randomRow/2 - 1, randomCol/2 - 1) == null) { // Si no esta ocupada:
						board.setPosition(randomRow/2 - 1, randomCol/2 - 1, obstacle); // Colocamos el * en el 1er cuadrante
						board.setPosition(randomRow/2 - 1, dimC - randomCol/2, obstacle); // Colocamos el * en el 2o cuadrante
						board.setPosition(dimR - randomRow/2, randomCol/2 - 1, obstacle); // Colocamos el * en el 3er cuadrante
						board.setPosition(dimR - randomRow/2, dimC - randomCol/2, obstacle); // Colocamos el * en el 4o cuadrante
						obstaculos--;	
					} else { // Si la casilla esta ocupada:
						randomRow = r.nextInt(row); // Creamos otra fila y columna aleatorias
						randomCol = r.nextInt(col);
					}	
				}
			}
		return board;
	}
	
	/**
	 * Metodo que inicializa el tablero por defecto con 2 jugadores.
	 * @param board
	 * @param pieces
	 * @return board
	 */
	private Board initializePieces(Board board, List<Piece> pieces) {
		Piece pieza = pieces.get(0);
		Piece pieza2 = pieces.get(1);
		board.setPosition(0, 0, pieza);
		board.setPosition(board.getRows() - 1, board.getCols() - 1, pieza);
		board.setPosition(0, board.getCols() - 1, pieza2);
		board.setPosition(board.getRows() - 1, 0, pieza2);
		return board;
	}
	
	@Override
	public Piece initialPlayer(Board board, List<Piece> playersPieces) {
		return playersPieces.get(0);
	}
	
	@Override
	public int minPlayers() {
		return 2;
	}

	@Override
	public int maxPlayers() {
		return 4;
	}
	
	/**
	 * Metodo que comprueba si es posible jugar. (Comprobando si cada pieza tiene movimientos validos)
	 * @param board
	 * @param playersPieces
	 * @return true si es posible jugar
	 */
	private boolean posibleGame(Board board, List<Piece> playersPieces) {
		boolean sepuedejugar = false;
		for(int i = 0; i < playersPieces.size(); i++) {
			if(!this.validMoves(board, playersPieces, playersPieces.get(i)).isEmpty()) {
				sepuedejugar = true;
			}
		}
		return sepuedejugar;
	}
	
	/**
	 * Metodo que comprueba si el jugador siguiente al actual va a poder mover, si no puede, comprueba el 
	 * siguiente...etc
	 */
	@Override
	public Piece nextPlayer(Board board, List<Piece> playersPieces, Piece lastPlayer) {
		// Comprobamos que el jugador siguiente al actual pueda hacer un movimiento, sino el siguiente...etc
		List<Piece> pieces = playersPieces;
		int i = pieces.indexOf(lastPlayer);
		int j = 1;
		boolean salir = false;
		while(this.validMoves(board, playersPieces, pieces.get((i + j) % pieces.size())).isEmpty() && !salir) {	// Comprobamos si el siguiente jugador puede mover.
			if(pieces.get((i + j) % pieces.size()).equals(lastPlayer))	{// Si hemos recorrido ya todos los jugadores habr� ganado lastPlayer.
				salir = false;
			} else {
				j++;
			}
		}
		return pieces.get((i + j) % pieces.size());
	}

	/**
	 * Metodo que actualiza el estado del juego, haciendo las comprobaciones pertinentes y
	 * devolviendo el estado correspondiente
	 */
	/*
	 * PROBAR SI FUNCIONA QUITANDO EL BREAK Y CON LA VARIABLE BOOLEANA
	*/
	public Pair<State, Piece> updateState(Board board, List<Piece> playersPieces, Piece lastPlayer) {
		// Recorremos el HashMap para buscar que jugador tiene el numero maximo de fichas
		HashMap<Piece, Integer> mapa = this.contadorFichasJugadores(board, playersPieces);
		int numJugadores = playersPieces.size();
		int jugadorGanador = -1;		// Jugador ganador (si es -1 es que no hay todavía ninguno o que hay empate).
		boolean salir = false;
		
		if(board.isFull()) { // Si el tablero esta lleno: comprobar si hay un jugador ganador o hay un empate
			int maximo = 0;
			for(int i = 0; i < numJugadores && !salir; i++) {
				if(mapa.get(playersPieces.get(i)) > maximo) {	// Si el jugador i tiene el máximo número de fichas.
					maximo = mapa.get(playersPieces.get(i));	// El máximo es el del jugador i.
					jugadorGanador = i;							// El jugador i sería el ganador.
				} else if(mapa.get(playersPieces.get(i)) == maximo) {	// Si hay otro jugador que tiene el mismo número de fichas.
					jugadorGanador = -1;						// Hay un empate.
					salir = true;										// Salimos del bucle.
				}
			}
			if(jugadorGanador == -1) {
				return new Pair<State, Piece>(State.Draw, null);
			} else {
				return new Pair<State, Piece>(State.Won, playersPieces.get(jugadorGanador));
			}
		} else {	// Si no lo está: comprobar si hay algún jugador ganador.
			int jugadoresFin = 0;			// Número de jugadores que han terminado.
			for(int i = 0; i < numJugadores; i++) {
				if(mapa.get(playersPieces.get(i)) == 0)	{// Si ha terminado incrementamos el número de jugadores que han ganado.
					jugadoresFin++;
				} else {					// Si no ha terminado lo damos como candidato ganador.
					jugadorGanador = i;
				}
			}
			if(jugadoresFin == mapa.size() - 1)	{// Si sólo hay un jugador que no haya terminado ha ganado.
				return new Pair<State, Piece>(State.Won, playersPieces.get(jugadorGanador));
			}
		}
		return new Pair<State, Piece>(State.InPlay, lastPlayer);
	}
	
	/**
	 * Metodo que cuenta las fichas que cada jugador tiene en el tablero y las almacena en un HashMap
	 * @param board
	 * @param playersPieces
	 * @return
	 */
	private HashMap<Piece, Integer> contadorFichasJugadores(Board board, List<Piece> playersPieces) {
		HashMap<Piece, Integer> fichacolor = new HashMap<Piece, Integer>();

		for(Piece p : playersPieces) {
			fichacolor.put(p, 0);
		}
		fichacolor.get(playersPieces.get(0));
		for(int i=0; i<board.getRows(); i++) {
			for(int j=0; j<board.getCols(); j++) {
				Piece p = board.getPosition(i, j);
				
				if(p != null && p.getId() != "*") {
					fichacolor.put(p,fichacolor.get(p) + 1);
				}
			}	
		}
		return fichacolor;
	}

	/**
	 * Metodo que dado un tablero, una lista de jugadores y un turno, devuelve si hay movimientos posibles para 
	 * la ficha que corresponde a ese turno
	 */
	@Override
	public List<GameMove> validMoves(Board board, List<Piece> playersPieces, Piece turn) {
		// Movimientos que esten a distancia uno o a distancia dos y que sean una ficha vacia
		List<GameMove> moves = new ArrayList<GameMove>();
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				if(board.getPosition(i,j) != null && board.getPosition(i, j).equals(turn)) {
					for(int k = i - 2; k <= i + 2; k++) {
						for(int l = j - 2; l <= j + 2; l++) {
							if(this.dentroDelTablero(board, k, l) && board.getPosition(k, l) == null) {
								moves.add(new AtaxxMove(i, j, turn, k, l));
							}
						}
					}
				}
			}
		}
		return moves;
	}
	
	/**
	 * Moetodo que comprueba si la posicion proporcionasda esta dentro del tablero.
	 * @param board
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean dentroDelTablero(Board board, int row, int col) {
		if(row < 0 || col < 0 || row >= board.getRows() || col >= board.getCols()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		// TODO Auto-generated method stub
		return 0;
	}
}
