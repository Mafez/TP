package practica6.Response;

import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;

public interface Response extends java.io.Serializable {
	public void run(GameObserver o);
}
