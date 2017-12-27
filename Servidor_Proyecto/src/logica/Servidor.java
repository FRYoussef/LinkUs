package logica;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import persistencia.controlHibernate.ControlHibernate;

public class Servidor {
	
	private int puerto;
	private ServerSocket serverSocket = null;
	private Thread thServidor = null;
	private ArrayList<IObserver> observers = null;
	private IGestorDB hibernate = null;
	
	public Servidor(){
		observers = new ArrayList<IObserver>();
		ManejadorUsuariosConectados.inicializaUsuariosConectados();
		hibernate = new ControlHibernate();
	}
	
	/**
	 * Annade un observador al modelo 
	 * @param o
	 */
	
	public void annadeObserver(IObserver ob){
		observers.add(ob);
	}
	
	/**
	 * Elimina un observador del modelo
	 * @param o
	 */
	
	public void eliminaObserver(IObserver ob){
		observers.remove(ob);
	}
	
	/**
	 * Notifica a los observadores que se quiere mostrar un texto
	 * @param texto
	 */
	
	public void onMuestraTexto(String texto){
		for(IObserver ob : observers){
			ob.onMuestraTexto(texto);
		}
	}
	
	public void arrancaServidor(int _puerto){
		puerto = _puerto;
		thServidor = new Thread(new RnServidor());
		thServidor.setDaemon(true);
		thServidor.start();
		Thread thConexiones = new Thread(new RnConfirmadorConexion());
		thConexiones.setDaemon(true);
		thConexiones.start();
	}
	
	/**
	 * Cierra el servidor
	 */
	
	public void apagaServidor(){
		if(serverSocket != null){
			try{
				serverSocket.close();
				ManejadorUsuariosConectados.limpiaLista();
				if(thServidor != null)
					thServidor.interrupt();
				
			}catch(IOException e){}
		}
	} 
	
	/**
	 * Va a permitir dar acceso a clientes que se conecten,
	 * lanzando hilos.
	 */
	
	private class RnServidor implements Runnable{

		@Override
		public void run() {
			try
			{
				System.out.println("Servidor arrancado.");
				serverSocket = new ServerSocket(puerto);
				Conexion conexion;
				while(true){
					try{
						conexion = new Conexion(serverSocket.accept());
						System.out.println("Cliente conectado");
						Thread thCliente = new Thread(new RnCliente(conexion, observers, hibernate));
						thCliente.setDaemon(true);
						thCliente.start();
					}catch(IOException e){}
					
				}
			}
			catch (IOException e){
				System.out.println("Error de conexion.");
			}
			finally{
				apagaServidor();
			}
		}
	}
	
	/**
	 * Este hilo servira para confirmar que el canal sigue abierto con los clientes
	 * @author Youss
	 */
	
	private class RnConfirmadorConexion implements Runnable{

		@Override
		public void run() {
			while(true){
				try {
					Thread.sleep(300000);
					ManejadorUsuariosConectados.compruebaConexiones();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
