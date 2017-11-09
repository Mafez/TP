package practica4.ataxx;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import practica4.accesorios.Constantes;
import es.ucm.fdi.tp.basecode.bgame.control.ConsolePlayer;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.DummyAIPlayer;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.views.GenericConsoleView;

/**
 * Factoria del juego Ataxx. Crea reglas diferentes.
 * 
 */
public class AtaxxFactory implements GameFactory {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int dimR;
	private int dimC;
	private int obstaculos;
	
	public AtaxxFactory() {
		this.dimR = Constantes.dimPorDefecto;
		this.dimC = Constantes.dimPorDefecto;
		this.obstaculos = 0;
	}
	
	public AtaxxFactory(int dimR, int dimC, int obstaculos) {
		if (dimR < Constantes.dimMinima || dimC < Constantes.dimMinima)
			throw new GameError("Dimension must be at least 5: " + dimR + "x" + dimC);
		else {
			this.dimR = dimR;
			this.dimC = dimC;
			this.obstaculos = obstaculos;
		}
	}
	
	
	 public AtaxxFactory(int obstaculos) {
	  	this.dimR = Constantes.dimPorDefecto;
	  	this.dimC = Constantes.dimPorDefecto;
	  	this.obstaculos = obstaculos;
	 }
	 
	@Override
	public GameRules gameRules() {
		return new AtaxxRules(dimR, dimC, obstaculos);
	}

	@Override
	public Player createConsolePlayer() {
		ArrayList<GameMove> possibleMoves = new ArrayList<GameMove>();
		possibleMoves.add(new AtaxxMove());
		return new ConsolePlayer(new Scanner(System.in), possibleMoves);
	}

	@Override
	public Player createRandomPlayer() {
		return new AtaxxRandomPlayer();
	}

	@Override
	public Player createAIPlayer(AIAlgorithm alg) {
		return new DummyAIPlayer(createRandomPlayer(), 1000);
	}
	
	/**
	 * Por defecto, dos jugadores, X y O.
	 */
	@Override
	public List<Piece> createDefaultPieces() {
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(new Piece("X"));
		pieces.add(new Piece("O"));
		return pieces;
	}

	@Override
	public void createConsoleView(Observable<GameObserver> g, Controller c) {
		new GenericConsoleView(g, c);
	}

	@Override
	public void createSwingView(Observable<GameObserver> game, Controller ctrl, Piece viewPiece, Player randPlayer, Player aiPlayer) {
		throw new UnsupportedOperationException("There is no swing view");
	}
}
