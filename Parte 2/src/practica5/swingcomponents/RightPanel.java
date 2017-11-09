package practica5.swingcomponents;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

import practica5.swingcomponents.SwingView.PlayerMode;
import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

@SuppressWarnings("serial")
public class RightPanel extends JPanel {
	
	private SwingView swingview; // Instancia de la clase SwingView
	private PlayerTableModel tableModel;
	private JTextArea statusmsg;
	
	private JComboBox<PlayerMode> lstListaplayertypes;
	private JComboBox<Piece> lstListaJugadores2;
	
	private JButton randomButton;
	private JButton intelligentButton;
	
	private JButton restartButton;
	private JButton quitButton;
	private JButton setButton;
	
	public RightPanel(Controller ctrl, SwingView swingview) {		
		this.swingview = swingview;
		
		// Creamos un panel con un BoxLayout que organiza los componentes de arriba a abajo 
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Creamos un JPanel con sus respectivos componentes que van a estar en el panel derecho
		
		// StatusMessages Panel
		JPanel statusmessagescomponent = new JPanel();
		statusmessagescomponent.setBorder(BorderFactory.createTitledBorder("Status Messages"));
		statusmsg = new JTextArea(11,14);
		statusmsg.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(statusmsg);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		statusmessagescomponent.add(scrollPane);
		this.add(statusmessagescomponent);
		
		// PlayerInformation Panel
		JPanel playerinformationcomponent = new JPanel(new BorderLayout());
		playerinformationcomponent.setBorder(BorderFactory.createTitledBorder("Player Information"));
		
		this.tableModel = new PlayerTableModel();
		
		JTable tableplayers = new JTable(this.tableModel) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);
				comp.setBackground(swingview.getPieceColor().get((swingview.getPieces().get(row))));
				return comp;
			}
		};
		JScrollPane scrlP = new JScrollPane(tableplayers);
		playerinformationcomponent.setPreferredSize(new Dimension(100, 100));
		playerinformationcomponent.add(scrlP);
		this.add(playerinformationcomponent);

		// PieceColors Panel
		JPanel piececolorscomponent = new JPanel();
		piececolorscomponent.setBorder(BorderFactory.createTitledBorder("Piece Colors"));
		
		JComboBox<Piece> lstListaJugadores = new JComboBox<Piece>();
		lstListaJugadores.removeAllItems();
		for(Piece p : this.swingview.getPieces()) {
			if(swingview.getPieceColor().get(p) == null) {
				swingview.setPieceColor(p, Utils.randomColor());
			}
			lstListaJugadores.addItem(p);
		}
		lstListaJugadores.setAlignmentX(LEFT_ALIGNMENT);
		lstListaJugadores.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				lstListaJugadores.getSelectedItem();
			}
		});	
		
		JButton choosecolorButton = new JButton("Choose Color");
		choosecolorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Piece p = (Piece) lstListaJugadores.getSelectedItem(); // Intentar evitar el cast
				ColorChooser co = new ColorChooser(new JFrame(), "Select Color Piece", swingview.getPieceColor().get(p));
				if(co.getColor() != null) { // Si se le asigna un color:
					swingview.setPieceColor(p, co.getColor());
					repaint();
				}
			}
		});
		choosecolorButton.setAlignmentX(RIGHT_ALIGNMENT);
		piececolorscomponent.add(lstListaJugadores);
		piececolorscomponent.add(choosecolorButton);
		this.add(piececolorscomponent);
		
		
		// PlayerModes Panel
		List<PlayerMode> modes = this.swingview.getPlayerModesAvailable();
		if(modes.size() != 1) {	// Hay más modos que el manual.			
			JPanel playermodescomponent = new JPanel();
			playermodescomponent.setBorder(BorderFactory.createTitledBorder("Player Mode"));
			
			// Para cada una de las piezas tenemos que ver de qu� tipo es el jugador de esa pieza:
			this.lstListaJugadores2 = new JComboBox<Piece>();
			if(this.swingview.getLocalPiece() == null) {
				for(Piece p : this.swingview.getPieces()) {
					this.lstListaJugadores2.addItem(p);
					if(!this.swingview.getPlayerTypes().containsValue(p))
						this.swingview.getPlayerTypes().put(p, PlayerMode.MANUAL);
				}
			}
			else {
				this.lstListaJugadores2.addItem(this.swingview.getLocalPiece());
				this.swingview.getPlayerTypes().put(this.swingview.getLocalPiece(), PlayerMode.MANUAL);
			}
			this.lstListaJugadores2.setAlignmentX(LEFT_ALIGNMENT);
			this.lstListaJugadores2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					lstListaJugadores2.getSelectedItem();
				}
			});
			
			this.lstListaplayertypes = new JComboBox<PlayerMode>();
			
			for(PlayerMode m : modes)
				this.lstListaplayertypes.addItem(m);
			this.lstListaplayertypes.setAlignmentX(CENTER_ALIGNMENT);
			
			this.setButton = new JButton("Set");
			this.setButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Piece p = (Piece) lstListaJugadores2.getSelectedItem();
					PlayerMode pm = (PlayerMode) lstListaplayertypes.getSelectedItem();
					swingview.setPlayerTypes(p, pm);
					updateTable();
					if(swingview.getLocalPiece() == null || swingview.getTurn().equals(p)) {
						if(pm == PlayerMode.RANDOM || pm == PlayerMode.AI) {
							randomButton.setEnabled(false);
							intelligentButton.setEnabled(false);
							quitButton.setEnabled(false);
							if(swingview.getLocalPiece() == null)
								restartButton.setEnabled(false);
							swingview.deActivateBoard();
							swingview.decideMakeAutomaticMove();
						}
						else {
							randomButton.setEnabled(true);
							intelligentButton.setEnabled(true);
							swingview.activateBoard();
						}
					}
				}
			});
			this.setButton.setAlignmentX(RIGHT_ALIGNMENT);
			
			playermodescomponent.add(this.lstListaJugadores2);
			playermodescomponent.add(this.lstListaplayertypes);
			playermodescomponent.add(this.setButton);
			
			this.add(playermodescomponent);
			
			// AutomaticMoves Panel
			JPanel automaticmovescomponent = new JPanel();
			automaticmovescomponent.setBorder(BorderFactory.createTitledBorder("Automatic Moves"));
			this.randomButton = new JButton("Random");
			this.randomButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					swingview.decideMakeAutomaticMove();
					randomButton.setEnabled(false);
				}
			});
			this.randomButton.setAlignmentX(LEFT_ALIGNMENT);
			automaticmovescomponent.add(this.randomButton);
			
			this.intelligentButton = new JButton("Intelligent");
			this.intelligentButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					swingview.decideMakeAutomaticMove();
				}
			});
			this.intelligentButton.setAlignmentX(RIGHT_ALIGNMENT);
			automaticmovescomponent.add(this.intelligentButton);
			this.add(automaticmovescomponent);	
		}
		// QuitAndRestart Panel
		JPanel quitandrestartcomponent = new JPanel();
		this.quitButton = new JButton("Quit");
		this.quitButton.setAlignmentX(LEFT_ALIGNMENT);
		this.quitButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int answer;
			answer = JOptionPane.showOptionDialog(new JFrame(), "�Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if(answer == 0) {
				ctrl.stop();
				swingview.setVisible(false);
				System.exit(0);
			}
		}
		});
		quitandrestartcomponent.add(this.quitButton);
		
		if(this.swingview.getLocalPiece() == null) {
			this.restartButton = new JButton("Restart");
			this.restartButton.setAlignmentX(RIGHT_ALIGNMENT);
			this.restartButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ctrl.restart();
				}
			});
			quitandrestartcomponent.add(this.restartButton);
		}
		this.add(quitandrestartcomponent);
		
		this.setVisible(true);
	}
	
	protected void addMsgToStatusArea(String msg) {
		this.statusmsg.append(" " + msg + "\n");
	}	
	
	protected void setSetButtonEnabled(boolean b) {
		this.setButton.setEnabled(b);
	}
	
	protected void setRandomButtonEnabled(boolean b) {
		this.randomButton.setEnabled(b);
	}
	
	protected void setIntelligentButtonEnabled(boolean b) {
		this.intelligentButton.setEnabled(b);
	}
	
	protected void setRestartButtonEnabled(boolean b) {
		this.restartButton.setEnabled(b);
	}
	
	protected void setQuitButtonEnabled(boolean b) {
		this.quitButton.setEnabled(b);
	}
	
	protected void setLstListaJugadores2Enabled(boolean b) {
		this.lstListaJugadores2.setEnabled(b);
	}
	
	protected void setLstListaPlayerTypesEnabled(boolean b) {
		this.lstListaplayertypes.setEnabled(b);
	}
	
	protected void updateTable() {
		this.tableModel.deleteAllRows();
		for(Piece p: this.swingview.getPieces())
			this.tableModel.addRow(new Object[] {p.toString(), this.swingview.getPlayerTypes().get(p), this.swingview.getBoard().getPieceCount(p)});
	}
}
