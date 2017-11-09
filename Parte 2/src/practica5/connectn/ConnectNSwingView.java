package practica5.connectn;

import practica5.swingcomponents.RectBoardSwingView;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

@SuppressWarnings("serial")
public class ConnectNSwingView extends RectBoardSwingView {

	private ConnectNSwingPlayer player;
	
	public ConnectNSwingView(Observable<GameObserver> g, Controller c, Piece localPiece, Player randPlayer, Player aiPlayer) {
		super(g, c, localPiece, randPlayer, aiPlayer);
		this.player = new ConnectNSwingPlayer();
	}


	@Override
	protected void handleMouseClick(int row, int col, int mouseButton) throws Exception {
		// No hace nada si el tablero no esta activado.
		if(super.getBoard().getPosition(row, col) == null) {
			this.player.setMove(row,col);
			super.decideMakeManualMove(this.player);
		}
		else
			throw new Exception("Error excuting move.");
	}
}