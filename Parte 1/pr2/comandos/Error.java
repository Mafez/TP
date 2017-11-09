package es.ucm.fdi.tp.pr2.comandos;

import es.ucm.fdi.tp.pr1.logica.Mundo;

public class Error implements Comando{

	@Override
	public void ejecuta(Mundo mundo) {
		mundo.error();	
	}

	@Override
	public Comando parsea(String[] cadenaComando) {
		
		return new Error();
	}

	@Override
	public String textoAyuda() {
		
		String string = "ERROR: Comando no valido.";
		
		return string;
	}

}
