package practica6.Response;


import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;

@SuppressWarnings("serial")
public class ErrorResponse implements Response {
	
	private String msg;

	public ErrorResponse(String msg) {
		this.msg = msg;
	}
	
	public void run(GameObserver o) {
		o.onError(msg);
	}
}
