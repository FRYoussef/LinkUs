package logica;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import protocolo.Mensaje;

/**
 * Hilo que lanza el servidor para mantener la conexion de los clientes.
 * Cada hilo es un cliente.
 * @author Youss
 */

public class RnCliente implements Runnable {

	private Conexion conexion;
	private String nombre;
	private int tipoUsuario;
	private ArrayList<IObserver> observers;
	private IGestorDB gestorDb;
	
	public RnCliente(Conexion conexion, ArrayList<IObserver> _observers, IGestorDB _gestorDb){
		this.conexion = conexion;
		nombre = "";
		observers = _observers;
		tipoUsuario = Mensaje.USUARIO_DESCONOCIDO;
		gestorDb = _gestorDb;
	}
	
	@Override
	public void run(){
		try{
			//hay que completar el login para seguir
			faseLogIn();
			Mensaje mensaje = null;
			while(true){
				System.out.println("Escuchando mensajes");
				mensaje = conexion.leeMensaje();
				
				if(mensaje.getTipo() == Mensaje.CANAL_PREGUNTA){
					
				}
				else if(mensaje.getTipo() == Mensaje.MENSAJE_RECIBIDO){
					System.out.println("Recibido msj de confirmacion");
					gestorDb.borraMensaje(mensaje.getId(), nombre);
				}
				else if(mensaje.getTipo() == Mensaje.MENSAJE_DIFUSION){
					System.out.println("Recibido msj de difusion");
					gestionaMensaje(mensaje);
					System.out.println("enviado msj de difusion");
				}
				else if(mensaje.getTipo() == Mensaje.MENSAJE_FORO){
					System.out.println("Recibido msj de foro");
					gestionaMensaje(mensaje);
					System.out.println("enviado msj de foro");
				}
				else if(mensaje.getTipo() == Mensaje.LISTA_ASIGNATURAS){
					
					System.out.println("recibido  msj de asignaturas");
					HashSet<String> hsAsignaturas = dameAsignaturas();
					conexion.escribeMensaje(new Mensaje(Mensaje.LISTA_ASIGNATURAS, -1, null, 
								null, null, null, hsAsignaturas));
					System.out.println("enviado msj de asignaturas");
				}
				else if(mensaje.getTipo() == Mensaje.TIPO_USUARIO){
					conexion.escribeMensaje(new Mensaje(Mensaje.TIPO_USUARIO, -1, null, null, 
								null, null, tipoUsuario));
				}
			}		
		}catch (SocketTimeoutException e){
			if(!nombre.equals(""))
				onMuestraTexto("Se ha perdido la conexion con " + nombre);
			e.printStackTrace();
		}
		catch (IOException e){
			if(!nombre.equals(""))
				onMuestraTexto("Se ha desconectado " + nombre);
			System.out.println("Error de conexion en el cliente.");
			e.printStackTrace();
		}catch (ClassNotFoundException e){
			System.out.println("Error en el protocolo del cliente.");
			e.printStackTrace();
		}catch(Exception e){
			if(!nombre.equals(""))
				onMuestraTexto("Se ha perdido la conexion con " + nombre);
			e.printStackTrace();
		}finally{
			ManejadorUsuariosConectados.eliminarUsuario(nombre);
			conexion.cierra();
		}
	}
	
	private void faseLogIn() throws ClassNotFoundException, IOException{
		boolean logIn = false;
		while(!logIn){
			Mensaje mensaje = conexion.leeMensaje();
			if(mensaje.getTipo() == Mensaje.PETICION_LOGIN){
				
				System.out.println("Msj de login recibido");
				if(!ManejadorUsuariosConectados.existeUsuario(mensaje.getEmisor()) &&
					compruebaCredenciales(mensaje.getEmisor(), (String)mensaje.getContenido())){
					
					nombre = mensaje.getEmisor();
					// Annadimos el cliente al treeMap
					ManejadorUsuariosConectados.anadirUsuario(nombre, conexion);
					onMuestraTexto("Se ha conectado " + nombre);
					
					// Mandamos un mensaje con el buzon
					conexion.escribeMensaje(dameBuzon());
					System.out.println("enviado msj de buzon");
					borraBuzon();
					logIn = true;
				}
				else{
					conexion.escribeMensaje(new Mensaje(Mensaje.LOGIN_INCORRECTO, -1,null, null, null, null, null));
					System.out.println("enviado msj de login incorrecto");
				}
			}
		}
	}
	
	/**
	 * Comprueba la contrasenna del cliente, y fija su tipo de usuario
	 * @param usuario
	 * @param contrasenna 
	 * @return true si su contrasenna es correcta, sino false
	 */
	
	private boolean compruebaCredenciales(String usuario, String contrasenna){
		tipoUsuario = gestorDb.dameTipoCliente(usuario);
		String contraDb = gestorDb.dameContrasennaCliente(usuario);
		if(contraDb != null)
			return contraDb.equals(contrasenna);
		return false;
	}
	
	/**
	 * Devuelve un mensaje del tipo buzon, que contiene todos los mensajes
	 * del buzon del cliente.
	 * @return un mensaje del tipo buzon, si hay mensajes en el buzon, sino null
	 */
	
	private Mensaje dameBuzon(){
		Mensaje msj = new Mensaje(Mensaje.MENSAJES_BUZON, -1, null, null, nombre, null, null);
		HashSet<byte[]> hsOM = gestorDb.dameObjMensajesCliente(nombre);
		if(hsOM != null){
			HashSet<Mensaje> hsM = new HashSet<Mensaje>();
			for(byte[] b : hsOM){
			    try {
			    	ByteArrayInputStream baip = new ByteArrayInputStream(b);
					ObjectInputStream ois = new ObjectInputStream(baip);
					hsM.add((Mensaje) ois.readObject());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			msj.setContenido(hsM);
		}
		
		return msj;
	}
	
	/**
	 * Manda un mensaje a los usuarios conectados, 
	 * y a los que no lo estan, se lo guarda en el buzon
	 * 
	 * @param msj a mandar
	 */
	
	private void gestionaMensaje(Mensaje msj){
		
		//si es un alumno, y el msj es de difusion, solo irá a alumnos
		HashSet<String> hsAsistencias = null;
		if(msj.getTipo() == Mensaje.MENSAJE_DIFUSION && tipoUsuario == Mensaje.USUARIO_ALUMNO){
			hsAsistencias = gestorDb.dameClientesAsignatura((String)msj.getReceptor(), "Alumno");
		}else{
			hsAsistencias = gestorDb.dameClientesAsignatura((String)msj.getReceptor(), "Todo");
		}
		
		//hay que guardar en el buzon el mensaje
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(msj);
			byte[] msjBytes = baos.toByteArray();
			msj.setId(gestorDb.guardaMensaje(msjBytes, hsAsistencias));
		} catch (IOException e) {e.printStackTrace();}
		
		ArrayList<ObjectOutputStream> salidas = ManejadorUsuariosConectados.
					getSalidasUsuarios(hsAsistencias);
		//mandamos a todos los usuarios conectados el msj
		for (ObjectOutputStream salida : salidas){
		try{
			salida.writeObject(msj);
		}
		catch (IOException e){
			e.printStackTrace();
		}
}
	   
	}
	
	/**
	 * Hace una peticion al servidor para devolver las asignaturas
	 * del usuario.
	 * @param nombre del cliente
	 * @return asignaturas asociadas al cliente
	 */
	
	private HashSet<String> dameAsignaturas(){
		return gestorDb.dameAsignaturasCliente(nombre);
	}
	
	/**
	 * Borra los mensaje del buzon del usuario.
	 */
	
	private void borraBuzon(){
		gestorDb.borraBuzonCliente(nombre);
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
}
