package practica6.Response;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

@SuppressWarnings("serial")
public class MoveEndResponse implements Response {
	
	private Board board;
	private Piece turn;
	private boolean success;

	public MoveEndResponse(Board board, Piece turn, boolean success) {
		this.board = board;
		this.turn = turn;
		this.success = success;
	}
	
	public void run(GameObserver o) {
		o.onMoveEnd(board, turn, success);
	}


}
