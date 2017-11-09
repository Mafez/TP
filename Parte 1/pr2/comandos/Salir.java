package es.ucm.fdi.tp.pr2.comandos;

import es.ucm.fdi.tp.pr1.logica.Mundo;

public class Salir implements Comando {

	@Override
	public void ejecuta(Mundo mundo) {
		mundo.setSimulacionTerminada(false);
		System.out.println("Hasta luego.");
	}

	@Override
	public Comando parsea(String[] cadenaComando) {
		Comando comando = null;
		
		if (cadenaComando.length == 1 && cadenaComando[0].toUpperCase().equals("SALIR") ) {
			comando = new Salir();
		}
		return comando;
	}

	@Override
	public String textoAyuda() {
		String string = "SALIR: cierra la aplicacion.";
		
		return string;
	}
}
