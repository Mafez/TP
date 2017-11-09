package es.ucm.fdi.tp.pr2.comandos;

import es.ucm.fdi.tp.pr1.logica.Mundo;

public interface Comando {
	
	public abstract void ejecuta(Mundo mundo);
	
	public abstract Comando parsea(String[ ] cadenaComando);
	
	public abstract String textoAyuda();

}
