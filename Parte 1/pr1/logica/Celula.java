package es.ucm.fdi.tp.pr1.logica;


public abstract class Celula {
	
	/**
	 * ATRIBUTO
	 */
	protected boolean esComestible;
	
	/**
	 * METODOS
	 */
	
	/**
	 * Metodo abstracto que se utiliza en todas las clase hija
	 * @param f Se le pasa una fila	
	 * @param c se la pasa una columna
	 * @param superficie Se le pasa una Superficie
	 * @return Devuelve una casilla
	 */
	public abstract Casilla ejecutaMovimiento(int f, int c, Superficie superficie);
	
	/**
	 * Se utiliza para saber que tipo de celula es(Simple o Compleja)
	 * @return Devuelve true si es simple y false si es compleja
	 */
	public abstract boolean esComestible();


}
