package es.ucm.fdi.tp.pr2.comandos;

import es.ucm.fdi.tp.pr1.logica.Mundo;

public class Paso implements Comando {

	@Override
	public void ejecuta(Mundo mundo) {
		mundo.evoluciona();
	}

	@Override
	public Comando parsea(String[] cadenaComando) {
		Comando comando = null;
		
		if (cadenaComando.length == 1 && cadenaComando[0].toUpperCase().equals("PASO") ) {
			comando = new Paso();
		}
		return comando;
	}

	public String textoAyuda() {
		
		String string = "PASO: realiza un paso en la simulacion.";
		
		return string;
	}

}
