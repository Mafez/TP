package es.ucm.fdi.tp.pr2.comandos;

import es.ucm.fdi.tp.pr1.logica.Mundo;

public class Vaciar implements Comando {

	@Override
	public void ejecuta(Mundo mundo) {
		mundo.vaciar();

	}

	@Override
	public Comando parsea(String[] cadenaComando) {
		Comando comando = null;
		
		if (cadenaComando.length == 1 && cadenaComando[0].toUpperCase().equals("VACIAR") ) {
			comando = new Vaciar();
		}
		return comando;
	}

	@Override
	public String textoAyuda() {
		String string = "VACIAR: crea un mundo vacio.";
		
		return string;
	}
}
