package es.ucm.fdi.tp.pr2.comandos;

public class ParserComandos {
	
	public static Comando[] comando = {
		new Ayuda(),
		new Iniciar(),
		new Vaciar(),
		new CrearCelulaSimple(0,0),
		new CrearCelulaCompleja(0,0),
		new EliminarCelula(0,0),
		new Paso(),
		new Salir(),
		new Error()
	};
	
	static public String AyudaComandos(){
		
		String cadena = "";
		
		for (int i = 0; i < comando.length; i++) {
			cadena += comando[i].textoAyuda() + '\n';
		}	
		return cadena;
		
	}
	
	static public Comando parseaComando(String[ ] cadenas){
		Comando comand = null;
		int contador = 0;
		
		while (comand == null) {
		comand = comando[contador].parsea(cadenas);
		contador++;
			
		}
		if(comand.equals(null)){
			comand = new Error();
		}
		return comand;
		
	}


}
