package logica;

import java.util.HashSet;

/**
 * Esta interfaz nos servirá para que el programa no se haga dependiente,
 * del gestor de la base de datos, de esta forma, podría haber mas gestores
 * 
 * @author Youss
 */

public interface IGestorDB {
	
	/**
	 * Cierra la sesion con la bd 
	 */
	public void cierra();
	
	/**
	 * Devuelve el tipo del cliente
	 * @param nombre del cliente
	 * @return ALUMNO, PROFESOR, DESCONOCIDO
	 */
	public int dameTipoCliente(String nombre);
	
	/**
	 * Devuelve la contrasenna de un cliente
	 * @param nombre del cliente
	 * @return contrasenna si existe, sino null
	 */
	public String dameContrasennaCliente(String nombre);
	
	/**
	 * Devuelve la lista de asignaturas de un usuario determinado
	 * @param nombre del usuario
	 * @return lista de asignaturas
	 */
	public HashSet<String> dameAsignaturasCliente (String nombre);
	
	/**
	 * Devuelve quien asiste a una asignatura, en base a un filtro
	 * @param asignatura que buscamos
	 * @param filtro puede ser 'Alumno', 'Profesor', o 'Todo',
	 * 		asi, devolvera alumnos, profesores, o todos los clientes asociados.
	 * @return si la asignatura existe devuelve quien asiste, sino null
	 */
	public HashSet<String> dameClientesAsignatura(String asignatura, String filtro);
	
	/**
	 * Guarda los mensajes en el buzon de los usuarios
	 * @param msjBytes
	 * @param nombres
	 * @return el id del mensaje
	 */
	public int guardaMensaje(byte[] msjBytes, HashSet<String> nombres);
	
	/**
	 * Borra un mensaje del cliente
	 * @param id del mensaje
	 * @param cliente
	 */
	
	public void borraMensaje(int id, String cliente);
	
	/**
	 * Selecciona el usuario para borrar sus mensajes.
	 * @param nombre del usuario
	 */
	public void borraBuzonCliente(String nombre);
	
	/**
	 * Devuelve un hashSet de tipo byte, con los mensajes del buzon
	 * @param nombre del cliente
	 * @return HashSet<byte[]> si hay msj en el buzon, sino null 
	 */
	public HashSet<byte[]> dameObjMensajesCliente(String nombre);
}
