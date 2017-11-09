package es.ucm.fdi.tp.pr1.logica;

import es.ucm.fdi.tp.pr1.logica.Superficie;

public class Mundo {
	//CONSTANTES
	public static final int FILAS = 5;
	public static final int COLUMNAS = 4;
	private boolean simulacionTerminada;
	
	//ATRIBUTOS
	private Superficie superficie;
	
	//CONSTRUCTOR
	public Mundo() {
		this.superficie = new Superficie(FILAS, COLUMNAS);
		this.simulacionTerminada = true;
	}

	//METODOS
	
	public void evoluciona() {
		
		for (int i = 0; i < FILAS; i++) {         //Bucle for para recorrer el tablero y poner el atributo movimiento (que indica si una celula 
			for (int j = 0; j < COLUMNAS; j++) {  //se ha movido o no) a true de nuevo.
				if (!superficie.estadoCasilla(i, j)) {					
					superficie.setArrayPosiciones(i, j, true);						
				}
				else{
					superficie.setArrayPosiciones(i, j, false);	
				}
			}
		}

		for (int i = 0; i < FILAS; i++) {
			for (int j = 0; j < COLUMNAS; j++) {
				if (superficie.getArrayPosiciones(i,j)) { //Si no esta vacia la casilla.
					superficie.ejecutaMovimiento(i, j);
				}
			}
		}	
	}
	
	public void crearCelulaSimple(int f, int c) {
		superficie.crearCelulaSimple(f, c);
	}
	
	public void crearCelulaCompleja(int f, int c) {
		superficie.crearCelulaCompleja(f, c);
	}

	public void eliminarCelula(int f, int c) {
		superficie.eliminarCelula(f, c);
	}

	public void iniciar() {
		superficie.iniciarSuperficie(FILAS, COLUMNAS);
	}

	public void vaciar() {
		superficie.vaciarSuperficie();
	}

	public void dibujar() {
		System.out.println(superficie.toString());
	}
	
	public boolean isSimulacionTerminada() {
		return simulacionTerminada;
	}

	public void setSimulacionTerminada(boolean simulacionTerminada) {
		this.simulacionTerminada = simulacionTerminada;
	}

	public Casilla[] getArrayCasilla(){
		return superficie.getArrayCasilla();
	}
	
	public void error(){
		System.out.println("ERROR" + '\n');
	}

}
