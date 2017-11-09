package practica5.swingcomponents;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

@SuppressWarnings("serial")
public abstract class RectBoardSwingView extends SwingView {

	private BoardComponent boardComp;
	
	public RectBoardSwingView(Observable<GameObserver> g, Controller c, Piece localPiece, Player randPlayer, Player aiPlayer) { //argumentos...
		super(g, c, localPiece, randPlayer, aiPlayer);
	}

	@Override
	protected void initBoardGui() {
		this.boardComp = new BoardComponent(this) {
			@Override
			protected void mouseClicked(int row, int col, int mouseButton) throws Exception {
				if(mouseButton != 3 && getBoard().getPosition(row, col) == getTurn()) { // NO CAMBIAR POR EQUALS!!
					setIntelligentButtonEnabled(false);
					setRandomButtonEnabled(false);
					setQuitButtonEnabled(false);
					if(getPlayerModesAvailable().size() != 1) {
						setQuitButtonEnabled(false);
						setLstListaJugadores2Enabled(false);
						setLstListaPlayerTypesEnables(false);
					}
					if(getLocalPiece() == null)
						setRestartButtonEnabled(false);
				}
				// Llama a handleMouseClick para permitir a las subclases manejar el evento.
				handleMouseClick(row, col, mouseButton);
			}
		};
		this.setBoardArea(this.boardComp);
	}

	@Override
	protected void redrawBoard() {
		this.boardComp.redraw();
	}
	
	@Override
	protected void activateBoard() {
		for(int i = 0; i < super.getBoard().getRows(); i++)
			for(int j = 0; j < super.getBoard().getCols(); j++)
				this.boardComp.setSwingBoardEnable(true, i, j);
		this.revalidate();
	}

	@Override
	protected void deActivateBoard() {
		for(int i = 0; i < super.getBoard().getRows(); i++)
			for(int j = 0; j < super.getBoard().getCols(); j++)
				this.boardComp.setSwingBoardEnable(false, i, j);
		this.revalidate();
	}
	
	protected abstract void handleMouseClick(int row, int col, int mouseButton) throws Exception;
}
