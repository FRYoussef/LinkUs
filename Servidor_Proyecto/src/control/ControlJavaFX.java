package control;

import logica.IObserver;
import logica.Servidor;

/**
 * Hace de control e intermediario de la vista
 * @author youss
 */

public class ControlJavaFX {
	
	private Servidor servidor;
	
	public ControlJavaFX(){
		servidor = new Servidor();
	}
	
	/**
	 * Arranca el servidor con un puerto especificado
	 * @param puerto especificado
	 */
	
	public void arrancaServidor(int puerto){
		servidor.arrancaServidor(puerto);
	}
	
	/**
	 * Para el servidor
	 */
	
	public void paraServidor(){
		servidor.apagaServidor();
	}
	
	/**
	 * Añade un observador al modelo
	 * @param o observador
	 */
	
	public void annadeObserver(IObserver ob){
		servidor.annadeObserver(ob);
	}
	
	/**
	 * Elimina un observador del modelo 
	 * @param o
	 */
	
	public void eliminaObserver(IObserver ob){
		servidor.eliminaObserver(ob);
	}
}
