package practica5.connectn;

import java.util.List;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;

@SuppressWarnings("serial")
public class ConnectNSwingPlayer extends Player {

	private int fila;
	private int columna;
	
	public void setMove(int fila, int columna) {
		this.fila = fila;
		this.columna = columna;
	}

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		return new ConnectNMove(this.fila, this.columna, p);
	}
}
