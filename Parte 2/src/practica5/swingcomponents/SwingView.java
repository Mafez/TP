package practica5.swingcomponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

@SuppressWarnings("serial")
public abstract class SwingView extends JFrame implements GameObserver {

	private Controller ctrl;
	private Piece localPiece;
	private Piece turn;
	private Board board;
	private JPanel boardPanel;
	private RightPanel rightPanel;
	private boolean playing;
	
	private Player randomPlayer;
	private Player AIplayer;
	
	private List<Piece> pieces;
	private Map<Piece, Color> pieceColors;
	private Map<Piece, PlayerMode> playerTypes;
	
	private String gameDesc;
	
	enum PlayerMode {
		MANUAL("Manual"), RANDOM("Random"), AI("Intelligent");
		private String name;
		
		PlayerMode(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name;
		}
	}
	
	final protected List<PlayerMode> getPlayerModesAvailable() {
		List<PlayerMode> ret = new ArrayList<PlayerMode>();
		ret.add(PlayerMode.MANUAL);	// Manual siempre estará disponible.
		if(this.randomPlayer != null)
			ret.add(PlayerMode.RANDOM);
		if(this.AIplayer != null)
			ret.add(PlayerMode.AI);
		return ret;
	}
	
	final protected Piece getTurn() {
		return turn;
	}
	
	final protected Board getBoard() {
		return board;
	}
	
	final protected void setBoardArea(JPanel c) {
		this.boardPanel.add(c);
	}
	
	final protected List<Piece> getPieces() {
		return pieces;
	}
	
	final protected Piece getLocalPiece() {
		return localPiece;
	}
	
	final protected Map<Piece, PlayerMode> getPlayerTypes() {
		return playerTypes;
	}
	
	final protected PlayerMode setPlayerTypes(Piece p, PlayerMode pm) {
		return playerTypes.put(p, pm);
	}
	
	final protected Map<Piece, Color> getPieceColor() {
		return pieceColors;
	}

	
	final protected void setPieceColor(Piece p, Color c) {
		pieceColors.put(p, c);
		this.redrawBoard();
	}
	
	final protected Player getRandomPlayer() {
		return randomPlayer;
	}
	
	final protected Player getAIPlayer() {
		return AIplayer;
	}
	
	final protected void setPlaying(Boolean b) {
		this.playing = b;
	}

	public SwingView(Observable<GameObserver> g, Controller c, Piece localPiece, Player randPlayer, Player aiPlayer) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.ctrl = c;
		this.localPiece = localPiece;
		this.pieceColors = new HashMap<Piece, Color>();
		this.playerTypes = new HashMap<Piece, PlayerMode>();
		this.randomPlayer = randPlayer;
		this.AIplayer = aiPlayer;
		g.addObserver(this);
	}
	
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		
		this.boardPanel = new JPanel(new BorderLayout());
		mainPanel.add(BorderLayout.CENTER, this.boardPanel);
		this.initBoardGui();
		
		this.rightPanel = new RightPanel(ctrl, this);
		mainPanel.add(BorderLayout.LINE_END, this.rightPanel);
		
		// Informaci�n de la ventana
		this.pack();
		this.setSize(700, 600);
		this.setLocation(500, 300);
		this.setVisible(true);
	}
	
	protected void decideMakeManualMove(Player manualPlayer) {
		if(this.playing && this.playerTypes.get(this.turn) == PlayerMode.MANUAL) {
			ctrl.makeMove(manualPlayer);
		}
	}
	
	protected void decideMakeAutomaticMove() {
		if(this.playerTypes.get(this.turn) == PlayerMode.AI) {
			decideMakeAutomaticMove(this.AIplayer);
		} else {
			decideMakeAutomaticMove(this.randomPlayer);
		}
	}
	
	protected void decideMakeAutomaticMove(Player player) {
		if(this.playing) {
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					ctrl.makeMove(player);
				}
			});
		}
	}
	
	protected abstract void initBoardGui();
	protected abstract void activateBoard();
	protected abstract void deActivateBoard();
	protected abstract void redrawBoard();
	
	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		this.turn = turn;
		this.board = board;
		this.pieces = pieces;
		this.playing = true;
		Random rand = new Random();
		for(Piece p : pieces)
			this.pieceColors.put(p, new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
		this.initGUI();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleOnGameStart(board, gameDesc, pieces, turn);
			}
		});
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleOnGameOver(board, state, winner);
			}
		});
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleOnMoveStart(board, turn);
			}
		});
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleOnMoveEnd(board, turn, success);
			}
		});
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleOnChangeTurn(board, turn);
			}
		});
	}

	@Override
	public void onError(String msg) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleOnError(msg);
			}
		});
	}
	
	private void handleOnGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) { 
		String[] gameDescr = gameDesc.split(" ");
		String playerintitle = "";
		if(this.localPiece != null)
			playerintitle = "(" + this.localPiece + ")";
		this.gameDesc = gameDescr[0];
		this.setTitle("Board Games: " + this.gameDesc + " " + playerintitle);
		handleOnChangeTurn(board, turn);
	}
	
	private void handleOnGameOver(Board board, State state, Piece winner) {
		this.board = board;
		this.playing = false;
		String stateStr;
		if(state == State.Won && this.localPiece.equals(this.turn) )
			stateStr = "won.";
		else
			stateStr = "lost.";
		this.rightPanel.addMsgToStatusArea("Game Over: " + stateStr);
		String stateGame = "";
		if(state == State.Draw) stateGame = "Draw.";
		if(state == State.Won) {
			if(winner.equals(this.localPiece)) {
				stateGame = "You won the game.";
				JOptionPane.showMessageDialog(new JFrame(), stateGame);
			}
			else{
				stateGame = winner + " won the game.";
				JOptionPane.showMessageDialog(new JFrame(), stateGame);
			}
				
		}
		this.rightPanel.addMsgToStatusArea(stateGame);
		this.deActivateBoard();
		//
		if(!this.playing) {
			this.dispose();
		}
		//
	}

	private void handleOnMoveStart(Board board, Piece turn) {
		this.board = board;
	}

	private void handleOnMoveEnd(Board board, Piece turn, boolean success) {
		this.board = board;
		redrawBoard();
	}

	private void handleOnChangeTurn(Board board, Piece turn) { 
		this.board = board;
		if(this.playing) {
			this.turn = turn;
			this.rightPanel.updateTable();
			String turnStr = "";
			if(this.localPiece != null)
				if(this.localPiece.equals(this.turn))
					turnStr = " (You!)";
			this.setStatusArea("Turn for " + this.turn.toString() + turnStr);
			
			if(this.getPlayerModesAvailable().size() != 1) {
				this.rightPanel.setSetButtonEnabled(true);
				this.rightPanel.setLstListaJugadores2Enabled(true);
				this.rightPanel.setLstListaPlayerTypesEnabled(true);
			}

			if(this.localPiece == null || this.localPiece.equals(this.turn)) {
				if(this.getPlayerTypes().get(this.getTurn()).equals(PlayerMode.RANDOM) || this.getPlayerTypes().get(this.getTurn()).equals(PlayerMode.AI)){
					this.rightPanel.setRandomButtonEnabled(false);
					this.rightPanel.setIntelligentButtonEnabled(false);
					if(this.localPiece == null)
						this.rightPanel.setRestartButtonEnabled(false);
					this.deActivateBoard();
					this.decideMakeAutomaticMove();
				}
				else {
					this.rightPanel.setRandomButtonEnabled(true);
					this.rightPanel.setIntelligentButtonEnabled(true);
					if(this.localPiece == null)
						this.rightPanel.setRestartButtonEnabled(true);
					this.rightPanel.setQuitButtonEnabled(true);
					this.activateBoard();
					// Status messages.
					if(this.localPiece == null || this.localPiece.equals(this.turn)) {
						if(this.gameDesc.equals("Advanced")) {
							if(this.board.getPieceCount(turn) != 0)
								this.setStatusArea("Click on an empty cell.");
							else
								this.setStatusArea("Click on an origin piece.");
						}
						else if(this.gameDesc.equals("Tic-Tac-Toe"))
							this.setStatusArea("Click on an empty cell.");
						else if(this.gameDesc.equals("ConnectN"))
							this.setStatusArea("Click on an empty cell.");
						else if(this.gameDesc.equals("Ataxx"))
							this.setStatusArea("Click on an origin piece.");
					}
				}
			}
			// Multiview.
			else {
				this.rightPanel.setQuitButtonEnabled(true);
				if(!this.localPiece.equals(this.turn)) {
					this.deActivateBoard();
					this.rightPanel.setIntelligentButtonEnabled(false);
					this.rightPanel.setRandomButtonEnabled(false);
				}
				else {
					this.activateBoard();
					if(this.playerTypes.get(this.localPiece) == PlayerMode.MANUAL) {
						this.rightPanel.setIntelligentButtonEnabled(true);
						this.rightPanel.setRandomButtonEnabled(true);
					}
				}
			}
		}
	}

	private void handleOnError(String msg) {
		if(!this.playing) {
			JOptionPane.showMessageDialog(new JFrame(), msg, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setStatusArea(String str) {
		this.rightPanel.addMsgToStatusArea(str);
	}
	
	protected void setRandomButtonEnabled(boolean b) {
		this.rightPanel.setRandomButtonEnabled(b);
	}
	
	protected void setIntelligentButtonEnabled(boolean b) {
		this.rightPanel.setIntelligentButtonEnabled(b);
	}
	
	protected void setRestartButtonEnabled(boolean b) {
		this.rightPanel.setRestartButtonEnabled(b);
	}
	
	protected void setQuitButtonEnabled(boolean b) {
		this.rightPanel.setQuitButtonEnabled(b);
	}
	
	protected void setLstListaJugadores2Enabled(boolean b) {
		this.rightPanel.setLstListaJugadores2Enabled(b);
	}
	
	protected void setLstListaPlayerTypesEnables(boolean b) {
		this.rightPanel.setLstListaPlayerTypesEnabled(b);
	}
}
