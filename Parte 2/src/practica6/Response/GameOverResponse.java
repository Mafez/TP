package practica6.Response;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

@SuppressWarnings("serial")
public class GameOverResponse implements Response {

	private Board board;
	private Game.State state;
	private Piece winner;

	public GameOverResponse(Board board, Game.State state, Piece winner) {
		this.board = board;
		this.state = state;
		this.winner = winner;
	}
	
	public void run(GameObserver o) {
		o.onGameOver(board, state, winner);
	}

}
