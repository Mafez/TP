package practica4.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Un jugador aleatorio para Ataxx.
 *
 */
public class AtaxxRandomPlayer extends Player {

	private static final long serialVersionUID = 1L;

	/**
	 * Metodo que crea un movimiento aleatorio para el juego Ataxx 
	 */
	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		int ficha = Utils.randomInt(pieces.size());	// Cogemos una ficha aleatoria.
		while(!pieces.get(ficha).equals(p)) { // Mientras la ficha escogida no corresponda al turno actual
			ficha = Utils.randomInt(pieces.size());	// Cogemos otra ficha aleatoria.
		}
		List<GameMove> movimientos = rules.validMoves(board, pieces, pieces.get(ficha));
		if(!movimientos.isEmpty()) {
			int aleatorio = Utils.randomInt(movimientos.size()); // Cogemos un movimiento aleatorio válido.
			return movimientos.get(aleatorio);
		} else {
			throw new GameError("There's no possible move for " + p);
		}
	}

	/**
	 * Crea el movimiento concreto que sera devuelto por el jugador. Se separa
	 * este metodo de {@link #requestMove(Piece, Board, List, GameRules)} para
	 * permitir la reutilizacion de esta clase en otros juegos similares,
	 * sobrescribiendo este metodo.
	 * @param row	Fila de la ficha que queremos mover.
	 * @param col	Columna de la ficha que queremos mover.
	 * @param p		Ficha.
	 * @param rowTo	Fila destino.
	 * @param colTo	Columna destino.
	 * @return
	 */
	protected GameMove createMove(int row, int col, Piece p, int rowTo, int colTo) {
		return new AtaxxMove(row, col, p, rowTo, colTo);
	}
}
