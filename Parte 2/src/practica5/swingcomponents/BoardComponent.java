package practica5.swingcomponents;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")

public abstract class BoardComponent extends JPanel {

	private SwingButton[][] swingBoard = null;
	private SwingView swingview;
	
	public BoardComponent(SwingView swingview) {
		this.swingview = swingview;
		this.initBoard();
	}
	
	public void redraw() {
		for(int i = 0; i < swingview.getBoard().getRows(); i++)
			for(int j = 0; j < swingview.getBoard().getCols(); j++) {
				if(swingview.getBoard().getPosition(i, j) == null)
					this.swingBoard[i][j].setBackground(Color.LIGHT_GRAY);
				else if(swingview.getBoard().getPosition(i, j).toString().equals("*"))
					this.swingBoard[i][j].setBackground(Color.BLACK);
				else
					this.swingBoard[i][j].setBackground(this.swingview.getPieceColor().get(swingview.getBoard().getPosition(i, j)));
			}
		this.setVisible(true);
	}
	
	public void initBoard() {
		this.setLayout(new GridLayout(swingview.getBoard().getRows(), swingview.getBoard().getCols()));
		this.swingBoard = new SwingButton[swingview.getBoard().getRows()][swingview.getBoard().getCols()];
		for(int i = 0; i < swingview.getBoard().getRows(); i++) {
			for(int j = 0; j < swingview.getBoard().getCols(); j++) {
				this.swingBoard[i][j] = new SwingButton(i, j, this.swingview) {

					@Override
					public void mousePressed(MouseEvent e) {
						if(swingview.getLocalPiece() == null || swingview.getLocalPiece().equals(swingview.getTurn())) {
							try {
								BoardComponent.this.mouseClicked(this.getFila(), this.getColumna(), e.getButton());
								if(e.getButton() == 1)
									this.setBackground(swingview.getPieceColor().get(swingview.getTurn()));
							} catch(Exception ex) {
								JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
							}
						}
					}
				};
				this.swingBoard[i][j].addMouseListener(this.swingBoard[i][j]);
				this.add(swingBoard[i][j]);
				// Color.
				if(this.swingview.getBoard().getPosition(i, j) == null)
					this.swingBoard[i][j].setBackground(Color.LIGHT_GRAY);
				else if(swingview.getBoard().getPosition(i, j).toString().equals("*"))
					this.swingBoard[i][j].setBackground(Color.BLACK);
				else
					this.swingBoard[i][j].setBackground(this.swingview.getPieceColor().get(this.swingview.getBoard().getPosition(i, j)));
			}
		}
		this.setVisible(true);
	}
	
	final protected SwingButton getSwingBoard(int i, int j) {
		return this.swingBoard[i][j];
	}
	
	protected abstract void mouseClicked(int row, int col, int mouseButton) throws Exception;

	protected void setSwingBoardEnable(boolean b, int i, int j) {
		this.swingBoard[i][j].setEnabled(b);
	}
	
}