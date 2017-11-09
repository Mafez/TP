package es.ucm.fdi.tp.pr2.comandos;

import es.ucm.fdi.tp.pr1.logica.Mundo;

public class Ayuda implements Comando {

	@Override
	public void ejecuta(Mundo mundo) {
		
		System.out.println(ParserComandos.AyudaComandos());

	}

	@Override
	public Comando parsea(String[] cadenaComando) {
		Comando comando = null;
	
		if (cadenaComando.length == 1 && cadenaComando[0].toUpperCase().equals("AYUDA") ) {
			comando = new Ayuda();
		}
		return comando;
	}

	public String textoAyuda() {
		String string = "AYUDA: muestra esta ayuda.";
		
		return string;
	}

}
