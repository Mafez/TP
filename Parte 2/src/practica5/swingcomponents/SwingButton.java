package practica5.swingcomponents;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class SwingButton extends JButton implements ActionListener, MouseListener {

	private int fila;
	private int columna;
	protected SwingView swingview; // Instancia de swingview
	
	public SwingButton() {
		super.setVisible(true);
		this.setBackground(Color.LIGHT_GRAY);
		this.addActionListener(this);
		this.revalidate();
	}
	
	public SwingButton(int fila, int columna, SwingView swingview) { // , Controller ctrl
		super.setVisible(true);
		this.fila = fila;
		this.columna = columna;
		this.swingview = swingview;
		this.setBackground(Color.LIGHT_GRAY);
		this.addActionListener(this);
		this.revalidate();
	}
	
	public int getFila() {
		return this.fila;
	}
	
	public int getColumna() {
		return this.columna;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
