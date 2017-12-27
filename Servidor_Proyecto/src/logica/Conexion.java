package logica;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import protocolo.Mensaje;

/**
 * Servira de acceso a los recursos de conexion de un socket
 * 
 * @author Youss
 */
public class Conexion {
	private Socket socket;
	private ObjectInputStream entrada = null;
	private ObjectOutputStream salida = null;
	
	public Conexion(Socket _socket) throws IOException{
		socket = _socket;
		entrada = new ObjectInputStream(socket.getInputStream());
		salida = new ObjectOutputStream(socket.getOutputStream());
	}
	
	public Mensaje leeMensaje() throws IOException, ClassNotFoundException{
		return (Mensaje) entrada.readObject();
	}
	
	public void escribeMensaje(Mensaje msj) throws IOException{
		if(salida != null)
			salida.writeObject(msj);
	}
	
	public void colocaTiempoLectura(int tiempo) throws SocketException, SocketTimeoutException{
		socket.setSoTimeout(tiempo);
	}
	
	public void cierra(){
		try{
			if(entrada != null) {entrada.close();}
			if(salida != null) {salida.close();}
			if(socket != null) {socket.close();}
		}catch(IOException e){}
	}

	public ObjectInputStream getEntrada() {
		return entrada;
	}

	public ObjectOutputStream getSalida() {
		return salida;
	}
}
