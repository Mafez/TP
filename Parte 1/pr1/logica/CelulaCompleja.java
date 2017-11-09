package es.ucm.fdi.tp.pr1.logica;

public class CelulaCompleja extends Celula {
	/**
	 * CONSTANTE
	 */
	public static final int MAX_COMER = 2;
	
	/**
	 * ATRIBUTO
	 */
	private int numComidas;
	
	/**
	 * CONSTRUCTOR
	 */
	public CelulaCompleja(){
		this.numComidas = 0;
		this.esComestible = false;
	}

	@Override
	 /**
	 * Metodo que ejecuta el moviento de las celulas complejas
	 * @param f Se le pasa una fila	
	 * @param c se la pasa una columna
	 * @param superficie Se le pasa una Superficie
	 * @return Devuelve una casilla
	 */
	public Casilla ejecutaMovimiento(int f, int c, Superficie superficie) {
		Casilla casilla = new Casilla(0, 0);
		String cadena = "";
		
		if(numComidas < MAX_COMER){
			casilla = superficie.posicionAleatoriaCompleja(f, c);
			
			if(superficie.estadoCasilla(casilla.getFila(), casilla.getColumna())){//Si esta vacia la casilla la mueve
				superficie.moverCelula(casilla.getFila(), casilla.getColumna(), f, c);
				cadena +=  "Celula Compleja en (" + f + ','+ c + ") se mueve a (" + casilla.getFila() + ',' + casilla.getColumna()+ ") --NO COME--"; 
			}
			else{
				if(!superficie.casillaCompleja(casilla)){//comprueba si en la casilla hay una celula compleja
					casilla = null;
					cadena = "Celula Compleja en (" + f + ','+ c + ") no se mueve";
				}
				else{
					this.numComidas++;
					cadena += "Celula Compleja en (" + f + ','+ c + ") se mueve a (" + casilla.getFila() + ',' + casilla.getColumna()+ ") --COME--";
					superficie.moverCelula(casilla.getFila(), casilla.getColumna(), f, c);					
					
					if(numComidas == MAX_COMER){ // Si el numComidas es igual a MAX_COMER explota											
						cadena += '\n' + "Explota celula compleja en la posicion (" + casilla.getFila() + ',' + casilla.getColumna() + ')';
						superficie.eliminarCelula(casilla.getFila(), casilla.getColumna());
						casilla = null;
					}
				}
			}		
		}
		
		System.out.println(cadena);
		return casilla;
	}
	/**
	 * Metodo que dibuja la celula compleja
	 */
	public String toString(){
		
		String celula = " * ";
		
		return celula;
	}
	/**
	 * Metodo que devuelve  si es comestible
	 */
	public boolean esComestible() {
		return this.esComestible;
	}
}
