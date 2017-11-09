package practica5.attt;

import practica5.swingcomponents.RectBoardSwingView;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

@SuppressWarnings("serial")
public class ATTTSwingView extends RectBoardSwingView {

	private ATTTSwingPlayer player;
	private boolean segundo;
	private int rowInit, colInit;

	public ATTTSwingView(Observable<GameObserver> g, Controller c, Piece localPiece, Player randPlayer, Player aiPlayer) {
		super(g, c, localPiece, randPlayer, aiPlayer);
		this.player = new ATTTSwingPlayer();
		this.segundo = false;
	}

	@Override
	protected void handleMouseClick(int row, int col, int mouseButton) throws Exception {
		if(super.getBoard().getPieceCount(super.getTurn()) > 0) {
			if(super.getBoard().getPosition(row, col) == null) {
				this.player.setMove(-1, -1, row, col);
				super.decideMakeManualMove(this.player);
				super.setStatusArea("Piece placed on (" + row + "," + col + ").");
			}
			else
				throw new Exception("Error executing move.");
		}
		else {
			if(!this.segundo) {
				if(super.getBoard().getPosition(row, col) != null && super.getBoard().getPosition(row, col).equals(super.getTurn())) {
					this.segundo = true;
					this.rowInit = row;
					this.colInit = col;
					super.setStatusArea("You have selected (" + this.rowInit + "," + this.colInit + ") as origin.");
					super.setStatusArea("Click on a destination cell.");
				}
				else
					throw new Exception("Esa casilla no te pertenece.");
			}
			else {
				if(mouseButton == 1) {
					Piece p = super.getBoard().getPosition(row, col);
					if(p == null) {	// Si la casilla esta vacía.
						this.player.setMove(this.rowInit, this.colInit, row, col);
						super.decideMakeManualMove(this.player);
						this.segundo = false;
						super.setStatusArea("Piece moved from (" + this.rowInit + "," + this.colInit + ") to (" + row + "," + col + ").");
					}
					else
						throw new Exception("La casilla destino es un obstaculo.");
				}
				else
					this.segundo = false;
			}
		}
	}
}