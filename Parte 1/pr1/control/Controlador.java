package es.ucm.fdi.tp.pr1.control;

import java.util.Scanner;
import es.ucm.fdi.tp.pr2.comandos.Comando;
import es.ucm.fdi.tp.pr2.comandos.ParserComandos;
import es.ucm.fdi.tp.pr1.logica.Mundo;

public class Controlador {
	/**
	 * ATRIBUTOS
	 */
	private Mundo mundo;
	private Scanner scanner;
	
	/**
	 * CONSTRUCTOR
	 * @param mundo se le pasa un mundo
	 * @param in se le pasa una variable Scanner
	 */
	public Controlador(Mundo mundo, Scanner in) {
		mundo = new Mundo();
		this.mundo = mundo;
		scanner = in;
	}
	
	/**
	 * METODOS
	 */
	
	
	public void realizaSimulacion() {
		String str;
		String[] cmdArray;
		mundo.iniciar();
				
		while(mundo.isSimulacionTerminada()){
			mundo.dibujar();//PREGUNTAR SI SE DEBE DIBUJAR/INICIAR AQUI
			System.out.println("Comando<");
			str = scanner.nextLine();
			cmdArray = str.split(" "); 	
			
			Comando comando = ParserComandos.parseaComando(cmdArray);
			if (comando!=null){
				comando.ejecuta(this.mundo);	
			}			
		}
	}
}
