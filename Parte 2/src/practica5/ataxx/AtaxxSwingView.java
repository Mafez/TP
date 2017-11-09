package practica5.ataxx;

import practica5.swingcomponents.RectBoardSwingView;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

@SuppressWarnings("serial")
public class AtaxxSwingView extends RectBoardSwingView {

	private AtaxxSwingPlayer player;
	private boolean segundo;
	private int rowInit, colInit;

	public AtaxxSwingView(Observable<GameObserver> g, Controller c,	Piece localPiece, Player randPlayer, Player aiPlayer) {
		super(g, c, localPiece, randPlayer, aiPlayer);
		this.player = new AtaxxSwingPlayer();
		this.segundo = false;
	}

	@Override
	protected void handleMouseClick(int row, int col, int mouseButton) throws Exception {
		System.out.println("estoy aqui");
		if(!this.segundo) {
			if(mouseButton == 1) {
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
		}
		else {
			if(mouseButton == 1) {
				Piece p = super.getBoard().getPosition(row, col);
				if(p == null) {	// Si la casilla esta vacía.
					this.player.setMove(this.rowInit, this.colInit, row, col);
					super.decideMakeManualMove(this.player);
					this.segundo = false;
				}
				else if(!super.getBoard().getPosition(row, col).toString().equals("*")) {	// Si la casilla no est� vac�a y no es un obstaculo.
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
