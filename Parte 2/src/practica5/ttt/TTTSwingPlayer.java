package practica5.ttt;

import java.util.List;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;

@SuppressWarnings("serial")
public class TTTSwingPlayer extends Player {

	private int fila;
	private int columna;
	
	public TTTSwingPlayer(int fila, int columna) {
		this.fila = fila;
		this.columna = columna;
	}

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		return new ConnectNMove(fila, columna, p);
	}
	
}
