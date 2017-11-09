package practica5.ataxx;

import java.util.List;
import practica4.ataxx.AtaxxMove;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

@SuppressWarnings("serial")
public class AtaxxSwingPlayer extends Player {

	private int filaI;
	private int columnaI;
	private int filaD;
	private int columnaD;
	
	public void setMove(int filaI, int columnaI, int filaD, int columnaD){
		this.filaI = filaI;
		this.columnaI = columnaI;
		this.filaD = filaD;
		this.columnaD = columnaD;	
	}
	
	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		return new AtaxxMove(this.filaI, this.columnaI, p, this.filaD, this.columnaD );
	}
	
}
