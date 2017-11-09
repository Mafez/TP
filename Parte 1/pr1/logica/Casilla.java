package es.ucm.fdi.tp.pr1.logica;

public class Casilla {
	/**
	 * ATRIBUTOS
	 */
	private int fila;
	private int columna;
	
	/**
	 * CONSTRUCTOR
	 * @param fila se la pasa una fila
	 * @param columna se le pasa una columna
	 */
	public Casilla(int fila, int columna) {
		this.fila = fila;
		this.columna = columna;
	}
	
	/**
	 * Coge la fila de la casilla
	 * @return Devuelve fila de casilla
	 */
	public int getFila() {
		return fila;
	}
	
	/**
	 * Modifica la fila de la casilla
	 * @param fila se le pasa una fila para modificarla
	 */
	public void setFila(int fila) {
		this.fila = fila;
	}

	/**
	 * Coge la columna de la casilla
	 * @return devuelve la columna de casilla
	 */
	public int getColumna() {
		return columna;
	}
	
	/**
	 * Modifica la columna de casilla
	 * @param columna se la pasa la columna de casilla para modificarla
	 */
	public void setColumna(int columna) {
		this.columna = columna;
	}
}
