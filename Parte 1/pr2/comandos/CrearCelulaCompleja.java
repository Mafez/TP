package es.ucm.fdi.tp.pr2.comandos;

import es.ucm.fdi.tp.pr1.logica.Mundo;

public class CrearCelulaCompleja implements Comando {
	private int fila;
	private int columna;
	
	public CrearCelulaCompleja(int fila, int columna) {
		this.fila = fila;
		this.columna = columna;
	}

	@Override
	public void ejecuta(Mundo mundo) {
		mundo.crearCelulaCompleja(fila, columna);

	}

	@Override
	public Comando parsea(String[] cadenaComando) {
		Comando comando = null;
		
		if (cadenaComando.length == 3 && cadenaComando[0].toUpperCase().equals("CREARCELULACOMPLEJA") ) {
			int fila = Integer.parseInt(cadenaComando[1]);     //Convierten el elemento 1
			int columna = Integer.parseInt(cadenaComando[2]); // y 2 del array a enteros.
			if(fila >= Mundo.FILAS || fila < 0 || columna >= Mundo.COLUMNAS || columna < 0){
				comando = new Error();
			}
			else{			
				comando = new CrearCelulaCompleja(fila,columna);
			}						
		}
		return comando;
	}

	@Override
	public String textoAyuda() {
		String string = "CREARCELULACOMPLEJA F C: crea una nueva celula compleja en la posicion (f,c) si es posible.";
		
		return string;
	}

}
