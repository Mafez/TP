package practica4.ataxx;


import java.util.List;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Clase para representar un movimiento del juego Ataxx.
 */
public class AtaxxMove extends GameMove {
	
	private static final long serialVersionUID = 1L;
	
	private int row;	/**Fila de la ficha que queremos mover*/
	private int col;	/**Columna de la ficha que queremos mover*/
	private int rowTo;  /**Fila en la que se coloca la ficha*/
	private int colTo;	/**Columna en la que se coloca la ficha*/

	public AtaxxMove(){
	}
	
	/**
	 * Construye un movimiento que coloca una ficha ({@code row},{@code col}). de tipo {@code p} en la
	 * posicion ({@code rowTo},{@code colTo}).
	 * @param row
	 *            Numero de fila.
	 * @param col
	 *            Numero de columna.
	 * @param p
	 *            Ficha que se debe colocar en la posicion ({@code rowTo},  {@code colTo}). 
	 * @param rowTo
	 *            Numero de fila destino.
	 * @param colTo
	 *            Numero de columna destino.        
	 */
	public AtaxxMove(int row, int col, Piece p, int rowTo, int colTo) {
		super(p);
		this.row = row;
		this.col = col;
		this.rowTo = rowTo;
		this.colTo = colTo;
	}
	
	@Override
	/**
	 * Metodo que ejecuta un movimiento del juego Ataxx, comprueba si la ficha corresponde al jugador actual,
	 * si el movimiento es válido, si la posicion destino es colindante o no y comprueba las posiciones colindantes. 
	 */
	public void execute(Board board, List<Piece> pieces) {  
		if(this.fichaOtroJugador(row, col, board, pieces)) {
			throw new GameError("The piece in the position (" + row + "," + col + ") belongs to another player!");
		} else {
			if(Math.max(Math.abs(rowTo - row), Math.abs(colTo - col)) > 2) {
				throw new GameError("Invalid move (" + row + "," + col + ") to " + "(" + rowTo + "," + colTo + ")!");
			} else {
				if (board.getPosition(row, col) == null || board.getPosition(row, col).getId() == "*") { // Si la posicion elegida esta vacia
					throw new GameError("position (" + row + "," + col + ") is already empty or has an obstacle!");
				} else { // Si la posicion elegida tiene una ficha:
					if (board.getPosition(rowTo, colTo) == null) { // Si la posicion a la que queremos mover no esta ocupada:
						// Se realiza el movimiento
						if(this.posicionColindante(row, col, rowTo, colTo)) { // Si la posicion destino es colindante:
							// Movemos la pieza a la posicion destino y dejamos la posicion anterior con la misma ficha
							Piece p = board.getPosition(row, col);
							board.setPosition(rowTo, colTo, p);
						} else if(!this.posicionColindante(row, col, rowTo, colTo)) { // Si no es colindante:
							// Movemos la pieza a la posicion destino y dejamos la posicion anterior vacia
							Piece p = board.getPosition(row, col);
							board.setPosition(rowTo, colTo, p);
							board.setPosition(row, col, null); // Eliminamos la ficha de la posicion anterior
						}
						// Después del movimiento, todas las fichas oponentes adyacentes a la casilla destino se convierten al color del jugador
						this.compruebaColindantes(rowTo, colTo, board);
					} else { // Si lo esta:
						throw new GameError("position (" + rowTo + "," + colTo + ") is already occupied!");
					}
				}
			}		
		}
	}
	
	/**
	 * Metodo que comprueba si una posicion el colindante a otra dada. (Esta a distancia 1 o 2 de ella).
	 * @param row
	 * @param col
	 * @param rowTo
	 * @param colTo
	 * @return
	 */
	private boolean posicionColindante(int row, int col, int rowTo, int colTo) {
		if (Math.abs(rowTo - row) < 2 && Math.abs(colTo - col) < 2) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Metodo que convierte todas las casillas adyacentes a la casilla al color del jugador.
	 * @param row
	 * @param col
	 * @param board
	 */
	private void compruebaColindantes(int row, int col, Board board) {
		for(int i = row - 1; i <= row + 1; i++) {
			for(int j = col - 1; j <= col + 1; j++) {
				if(this.dentroDelTablero(board, i, j)) {
					if(board.getPosition(i, j) != null && board.getPosition(i, j).getId() != "*") {
						board.setPosition(i, j, getPiece());
					}
				}
			}
		}
	}
	
	/**
	 * Metodo que comprueba si una ficha (row,col) del tablero pertenece a otro jugador que no sea el actual.
	 * @param row
	 * @param col
	 * @param board
	 * @param pieces
	 * @return
	 */
	private boolean fichaOtroJugador(int row, int col, Board board, List<Piece> pieces) {
		Piece turno = getPiece();
		if(board.getPosition(row, col) != null && board.getPosition(row, col).getId() != "*" && !board.getPosition(row, col).getId().equals(turno.getId())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Comprueba si una posicion esta dentro del tablero.
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
	
	/**
	 * Se puede construir un movimiento desde un string de la forma
	 * "row SPACE col SPACE rowTo SPACE colTo" donde row y col son enteros que representan una posicion de una ficha
	 *  y rowTo y colTo representan la posicion destino de la ficha.
	 */
	@Override
	public GameMove fromString(Piece p, String str) {
		String[] words = str.split(" ");
		if (words.length != 4) {
			return null;
		}

		try {
			int row, col, rowTo, colTo;
			row = Integer.parseInt(words[0]);
			col = Integer.parseInt(words[1]);
			rowTo = Integer.parseInt(words[2]);
			colTo = Integer.parseInt(words[3]);
			return createMove(row, col, p, rowTo, colTo);
		} catch (NumberFormatException e) {
			return null;
		}

	}

	/**
	 * Crea un nuevo movimiento con la misma ficha utilizada en el movimiento
	 * actual. Llamado desde {@link #fromString(Piece, String)}; se separa este
	 * metodo del anterior para permitir utilizar esta clase para otros juegos
	 * similares sobrescribiendo este metodo.
	 * @param row	Fila de la ficha que vamos a mover.
	 * @param col	Columna de la ficha que vamos a mover.
	 * @param p		Ficha que vamos a mover.
	 * @param rowTo	Fila donde movemos la ficha.
	 * @param colTo	Columna donde movemos la ficha.
	 */
	protected GameMove createMove(int row, int col, Piece p, int rowTo, int colTo) {
		return new AtaxxMove(row, col, p, rowTo, colTo);
	}
	
	@Override
	public String help() {
		return "'row column rowTo columnTo', to place a piece in (row, col) at the corresponding position (rowTo, colTo).";
	}
	
	@Override
	public String toString() {
		if (getPiece() == null) {
			return help();
		} else {
			return "Place a piece '" + getPiece() + "' at (" + rowTo + "," + colTo + ")";
		}
	}
}
