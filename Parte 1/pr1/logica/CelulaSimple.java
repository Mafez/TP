package es.ucm.fdi.tp.pr1.logica;
import es.ucm.fdi.tp.pr1.logica.Superficie;

public class CelulaSimple extends Celula {
	/**
	 * CONSTANTES
	 */
	public static final int MAX_PASOS_SIN_MOVER = 1;
	public static final int PASOS_REPRODUCCION = 2;
	
	/**
	 * ATRIBUTOS
	 */
	private int numPasos;
	private int numReproduccion;
	
	/**
	 * CONSTRUCTOR
	 */
	public CelulaSimple() {
		this.numPasos = MAX_PASOS_SIN_MOVER;
		this.numReproduccion = PASOS_REPRODUCCION;
		this.esComestible = true;
	}
	
	/**
	 * Metodo que dibuja la celula simple
	 */
	public String toString(){
		
		String celula = " X ";
		
		return celula;
	}
	
	/**
	 * Metodo que te indica si toca reproducirse. En caso de que numReproduccion sea igual a cero lo resetea
	 * sino lo reduce una unidad.
	 * @return Devuelve true si se ha reproducido. False en caso contrario.
	 */
	public boolean tocaReprod(){
		boolean ok = false;

		if (numReproduccion == 0) {
			numReproduccion = PASOS_REPRODUCCION;
			ok = true;
		}
		else
		{
			numReproduccion -= 1;
		}
		return ok;
	}
	
	/**
	 * Metodo que comprueba si numReproduccio  es igual a cero. Lo utilizamos para cuando no se puede mover la celula
	 * @return Devuelve true si es igual a 0 y muere por no poder reproducirse
	 */
	public boolean haMuerto(){
		boolean ok = false;
		
		if (numReproduccion == 0) {
			ok = true;
		}
		
		return ok;
	}
	
	/**
	 * Metodo que comprueba si el numPasos es igual a cero, en caso negativo lo reduce una unidad
	 * @return Deveulve true si es igual a cero y false en caso contrario
	 */
	public boolean noMover(){
		boolean ok = false;
		
		if(numPasos == 0){
			ok = true;
		}
		else
		{
			numPasos -= 1;
		}
		return ok;
	}
	
	/**
	 * Metodo que ejecuta el moviento de las celulas simples
	 * @param f Se le pasa una fila	
	 * @param c se la pasa una columna
	 * @param superficie Se le pasa una Superficie
	 * @return Devuelve una casilla
	 */
	public Casilla ejecutaMovimiento(int f, int c, Superficie superficie){
		Casilla casilla = new Casilla(f, c);
		
		int casillaAleatoria, casillasVacias = superficie.alrededorCelula(f, c);
			
		if (superficie.getArrayPosiciones(f,c)) { //Comprueba si la celula se tiene que mover
			if (casillasVacias != 0) {
				
				casillaAleatoria = (int) Math.random() * casillasVacias;
				casilla = superficie.getArrayCasilla()[casillaAleatoria];

				superficie.moverCelula(casilla.getFila(), casilla.getColumna(), f, c);//Mueve la celula de (f,c)				              
				System.out.println("Movimiento de (" + f + ',' + c + " a " + casilla.getFila() + ',' + casilla.getColumna() + ')');
				if (tocaReprod()) {                     															
					superficie.crearCelulaSimple(f, c);                        
					System.out.println("Nace nueva celula en (" + f + ',' + c + ") cuyo padre ha sido (" + casilla.getFila() + ',' + casilla.getColumna() + ')');
				}
				
					
			} else {//Si no tiene sitios alrededor para moverse.
				if (haMuerto()) {
					superficie.eliminarCelula(f, c);			   
					System.out.println("Muere la celula de la casilla (" + f + ',' + c + ") por no poder reproducirse.");
				}
				else if(noMover()){
					superficie.eliminarCelula(f, c);
					System.out.println("Muere la celula de la casilla (" + f + ',' + c + ") por inactividad.");
				}
				casilla = null;
			}
		}
		return casilla;
	}

	@Override
	/**
	 * Metodo que devuelve si es comestible
	 */
	public boolean esComestible() {
		return this.esComestible;
	}
}
