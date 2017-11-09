package practica5.attt;

import java.util.List;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTMove;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

@SuppressWarnings("serial")
public class ATTTSwingPlayer extends Player {

	private int filaI;
	private int columnaI;
	private int filaD;
	private int columnaD;
	
	public void setMove(int filaI, int columnaI, int filaD, int columnaD) {
		this.filaI = filaI;
		this.columnaI = columnaI;
		this.filaD = filaD;
		this.columnaD = columnaD;
	}
	
	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		return new AdvancedTTTMove(this.filaI, this.columnaI, this.filaD, this.columnaD, p);
	}
}
