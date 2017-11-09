package es.ucm.fdi.tp.pr1.logica;

import es.ucm.fdi.tp.pr1.logica.Casilla;

public class Superficie {

	//CONSTANTES
	private static final int NUM_CELULAS_COMP = 2;
	private static final int NUM_CELULAS_SIMP = 4;
	
	//ATRIBUTOS
	private int filas;
	private int columnas;
	private Celula[][] superficie;
	private Casilla[] arrayCasilla;
	private boolean[][] arrayPosiciones;

	//CONSTRUCTOR
	public Superficie(int f, int c) {

		this.filas = f;
		this.columnas = c;
		this.superficie = new Celula[filas][columnas];
		arrayCasilla = new Casilla[8];
		arrayPosiciones = new boolean[filas][columnas];
		vaciarSuperficie();
	}


	
	//METODOS
	
	//Metodo que recibe una fila y una columnas y crea una nueva celula en esa posicion.
	public void crearCelulaSimple(int f, int c){
		superficie[f][c] = new CelulaSimple();
	}
	
	public void crearCelulaCompleja(int f, int c){
		superficie[f][c] = new CelulaCompleja();
	}
	
	//Metodo que recibe una fila y una columna y elmina la celula en esa posicion.
	public void eliminarCelula(int f, int c){
		superficie[f][c] = null;
	}
	
	//Metodo que recive una fila y una columna y crea el número de celulas en posiciones aleatorias que haya en las constante 'NUM_CELULAS'. 
	public void iniciarSuperficie(int f, int c) {
		int cont = 0, fa, ca;
		vaciarSuperficie();
		iniciarPosiciones(f, c);
		
		while (cont < NUM_CELULAS_SIMP) {  //Mientras el contador sea menor que el numero de celulas dado por la constante, sigue creando células en posiciones aleatorias del tablero.
			fa = (int) (Math.random() * f);
			ca = (int) (Math.random() * c);
			if (estadoCasilla(fa,ca)) { //Si la casilla esta vacia, crea la célula en la posición (fa,ca)
				crearCelulaSimple(fa, ca);
				cont++;
			}
		}
			cont = 0;
			
		while (cont < NUM_CELULAS_COMP) {  //Mientras el contador sea menor que el numero de celulas dado por la constante, sigue creando células en posiciones aleatorias del tablero.
			fa = (int) (Math.random() * f);
			ca = (int) (Math.random() * c);
			if (estadoCasilla(fa,ca)) { //Si la casilla esta vacia, crea la célula en la posición (fa,ca)
				crearCelulaCompleja(fa, ca);
				cont++;
				
			}
		}
	}
	
	public void iniciarPosiciones(int f, int c){
	
		for (int i = 0; i < f; i++) {
			for (int j = 0; j < c; j++) {
			arrayPosiciones[i][j] = false;			
			}
		}
	}
	
	//Metodo que recibe el atributo filas y columnas y vacia la superficie dada.
	public void vaciarSuperficie() {// ?

		for (int i = 0; i < this.filas; i++) {
			for (int j = 0; j < this.columnas; j++) {
				superficie[i][j] = null;
			}
		}
	}
	
	//Metodo que comprueba si una casilla esta vacia. Devuelve true si lo esta.
	public boolean estadoCasilla(int f, int c) {
		boolean vacia = false;

		if (superficie[f][c] == null) {
			vacia = true;
		}
		return vacia;
	}
	
	public boolean casillaCompleja (Casilla casilla){
		return this.superficie[casilla.getFila()][casilla.getColumna()].esComestible;
	}
	
	//Metodo que comprueba las posiciones libres que existen alrededor de una casilla dada. Devuelve el numero de casillas vacias.
	public int alrededorCelula(int f, int c) {
		int cont = 0;

		for (int i = Math.max(f - 1, 0); i <= Math.min(f + 1, filas - 1); i++) {		 //Bucle for para recorrer las casillas que estan alrededor
			for (int j = Math.max(c - 1, 0); j <= Math.min(c + 1, columnas - 1); j++) { // de la casilla dada.
				if (estadoCasilla(i, j)) {
					arrayCasilla[cont] = new Casilla(i, j);
					cont++;				
				}
			}

		}
		return cont;
	}
	
	public Casilla posicionAleatoriaCompleja(int f, int c){
		Casilla casilla = new Casilla(0,0);
		int fa, ca;
		
		do{
			fa = (int) (Math.random() * this.filas);
			ca = (int) (Math.random() * this.columnas);
			
		}while(fa == f || ca == c);
		
		casilla.setFila(fa);
		casilla.setColumna(ca);
		
		return casilla;	
	}
	
	//Metodo para dibujar el tablero.
	public String toString() {
        String cadena = "";
         
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (estadoCasilla(i, j)){ //Si esta null coloca un guion.
                    cadena += " - " ;
                }
                else
                    cadena += superficie[i][j].toString(); //Sino coloca el numero de pasos sin moverse guion numero de pasos hasta reproducirse.
                cadena += "  ";
            }
          cadena += '\n';
        }
         
        return cadena;
    }
	
	//Metodo que mueve una celula de la posicion (f,c) a la posicion (fa,ca).
	public void moverCelula(int fa, int ca , int f, int c){
		superficie[fa][ca] = superficie[f][c];
		eliminarCelula(f,c);
		arrayPosiciones[fa][ca] = false; //Coloca movimiento a false para saber que ya se ha movido la celula.	
	}
	
	
	public Casilla ejecutaMovimiento(int f, int c){
		return superficie[f][c].ejecutaMovimiento(f, c, this);
	}
	
	public boolean getArrayPosiciones(int f, int c) {
		
		return arrayPosiciones[f][c];
	}

	public void setArrayPosiciones(int f, int c, boolean bool) {
		arrayPosiciones[f][c] = bool;
	}

	public Casilla[] getArrayCasilla() {
		return arrayCasilla;
	}
}
