package logica;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import protocolo.Mensaje;

/**
 * Esta clase será un singelton, para manejar a los usuarios conectados
 * @author Youss
 */

public class ManejadorUsuariosConectados{
	
	private static TreeMap<String, Conexion> tmUsuarios;

	/**
	 * Inicializa el treeMap de los usuarios conectados
	 */
	
	public static void inicializaUsuariosConectados(){
		tmUsuarios = new TreeMap<>();
	}
	
	/**
	 * Borra todo lo que tenga la lista de usuarios
	 */
	
	public static void limpiaLista(){
		tmUsuarios.clear();
	}
	
	/**
	 * Annade un usuario a la lista 
	 * @param nombre clave
	 * @param salida valor
	 */
	
	public static synchronized void anadirUsuario(String nombre, Conexion conexion){
		tmUsuarios.put(nombre, conexion);
	}
	
	/**
	 * Comprueba si un usuario existe
	 * @param nombre buscado
	 * @return true si existe, false si no existe en la lista
	 */
	
	public static synchronized boolean existeUsuario(String nombre){
		return tmUsuarios.containsKey(nombre);
	}
	
	/**
	 * Elimina un usuario de la lista de conectados
	 * @param nombre clave
	 */
	
	public static synchronized void eliminarUsuario(String nombre){
		if(tmUsuarios.containsKey(nombre))
			tmUsuarios.remove(nombre);
	}
	
	/**
	 * Devuelve en un arrayList las salidas de los usuarios conectados seleccionados.
	 * Tambien borra del array pasado, los nombres encontrados
	 * @param usuarios seleccionados
	 * @return salidas de los usuarios conectados seleccionados
	 */
	
	public static synchronized ArrayList<ObjectOutputStream> getSalidasUsuarios
									(HashSet<String> usuarios)
	{
		
		ArrayList<ObjectOutputStream> salidas = new ArrayList<>();
		Iterator <String> it = usuarios.iterator();
		String nombre = null;
		while(it.hasNext()){
			nombre = it.next();
			if(tmUsuarios.containsKey(nombre))
				salidas.add(tmUsuarios.get(nombre).getSalida());
			
		}
		return salidas;
	}

	/**
	 * Devuelve un treeSet de las claves de los usuarios conectados
	 * @return TreeSet<String> tsUsuarios
	 */
	
	public static synchronized TreeSet<String> getTreeSetUsuarios(){
		TreeSet<String> tsUsuarios = new TreeSet<String>(tmUsuarios.keySet());
		return tsUsuarios;
	}
	
	public static synchronized ArrayList<Conexion> getConexiones(){
		return (ArrayList<Conexion>) tmUsuarios.values();
	}
	
	/**
	 * Realiza un barrido sobre todas las conexiones para saber si siguen vivas
	 */
	
	public static synchronized void compruebaConexiones(){
		Iterator <String> it = tmUsuarios.keySet().iterator();
		String nombre = null;
		while(it.hasNext()){
			nombre = it.next();
			try {
				tmUsuarios.get(nombre).escribeMensaje(new Mensaje(Mensaje.CANAL_PREGUNTA, 
						-1, null, null, null, null, null));
			} catch (IOException e) {
				tmUsuarios.get(nombre).cierra();
				tmUsuarios.remove(nombre);
			}
		}
	}
}