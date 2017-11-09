package practica5.swingcomponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class ColorChooser extends JDialog {
	
	private JColorChooser colorChooser;
	private Color color;
	
	public ColorChooser(JFrame parent, String title, Color initialColor) {
		super(parent, title);
		
		setModalityType(DEFAULT_MODALITY_TYPE);
		
		this.colorChooser = new JColorChooser(initialColor == null ? Color.WHITE : initialColor);
		this.getContentPane().add(this.colorChooser);
		
		JPanel mainPanel = new JPanel();
		
		JButton OKbutton = new JButton("OK");
		OKbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				color = colorChooser.getColor();
				closeMainPanel();
			}
		});
		mainPanel.add(OKbutton);
		
		JButton cancelbutton = new JButton("Cancel");
		cancelbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeMainPanel();
			}
		});
		mainPanel.add(cancelbutton);
		
		getContentPane().add(mainPanel, BorderLayout.PAGE_END);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void closeMainPanel() {
		setVisible(false);
		dispose();
	}
	
	public JRootPane exitWithEscape() {
		JRootPane mainPanel = new JRootPane();
		KeyStroke key = KeyStroke.getKeyStroke("ESCAPE");
		Action action = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				closeMainPanel();
			}	
		};
		InputMap map = mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		map.put(key, "ESCAPE");
		mainPanel.getActionMap().put("ESCAPE", action);
		
		return mainPanel;
	}
	
	public Color getColor() {
		return color;
	}
	
}
