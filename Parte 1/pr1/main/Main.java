package es.ucm.fdi.tp.pr1.main;

import java.util.Scanner;
import es.ucm.fdi.tp.pr1.control.Controlador;
import es.ucm.fdi.tp.pr1.logica.Mundo;

public class Main {

	public static void main(String[] args) {
		Scanner s= new Scanner(System.in);
		Mundo mundo = new Mundo();
		Controlador controlador = new Controlador(mundo, s);
		controlador.realizaSimulacion();
		
	}
}

