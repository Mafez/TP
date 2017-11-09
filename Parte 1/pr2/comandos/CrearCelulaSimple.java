package es.ucm.fdi.tp.pr2.comandos;

import es.ucm.fdi.tp.pr1.logica.Mundo;

public class CrearCelulaSimple implements Comando {
	private int fila;
	private int columna;
	
	public CrearCelulaSimple(int fila, int columna) {
		this.fila = fila;
		this.columna = columna;
	}

	@Override
	public void ejecuta(Mundo mundo) {
		mundo.crearCelulaSimple(fila, columna);

	}

	@Override
	public Comando parsea(String[] cadenaComando) {
		Comando comando = null;
		
		if (cadenaComando.length == 3 && cadenaComando[0].toUpperCase().equals("CREARCELULASIMPLE") ) {
			
			int fila = Integer.parseInt(cadenaComando[1]);     //Convierten el elemento 1  y 2 del array a enteros.
			int columna = Integer.parseInt(cadenaComando[2]);
			if(fila >= Mundo.FILAS || fila < 0 || columna >= Mundo.COLUMNAS || columna < 0){
				comando = new Error();
			}
			else{			
			comando = new CrearCelulaSimple(fila,columna);
			}
		}
		return comando;
	}

	@Override
	public String textoAyuda() {
		String string = "CREARCELULASIMPLE F C: crea una nueva celula simple en la posicion (f,c) si es posible.";
		
		return string;
	}

}
