package es.ucm.fdi.tp.pr2.comandos;

import es.ucm.fdi.tp.pr1.logica.Mundo;

public class Iniciar implements Comando {

	@Override
	public void ejecuta(Mundo mundo) {
		mundo.iniciar();
		
	}

	@Override
	public Comando parsea(String[] cadenaComando) {
		Comando comando = null;
		
		if (cadenaComando.length == 1 && cadenaComando[0].toUpperCase().equals("INICIAR") ) {
			comando = new Iniciar();
		}
		return comando;
	}

	@Override
	public String textoAyuda() {
		String string = "INICIAR: inicia la nueva simulacion.";
		
		return string;
	}

}
