package persistencia.clasesHibernate;
// Generated 13-may-2017 17:19:02 by Hibernate Tools 3.5.0.Final

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Asignatura generated by hbm2java
 */
public class Asignatura implements Serializable {

	private static final long serialVersionUID = 8284481673561839989L;
	private Integer id;
	private String nombre;
	private Set<Cliente> clientes = new HashSet<Cliente>(0);

	public Asignatura() {
	}

	public Asignatura(String nombre, Set<Cliente> clientes) {
		this.nombre = nombre;
		this.clientes = clientes;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Cliente> getClientes() {
		return this.clientes;
	}

	public void setClientes(Set<Cliente> clientes) {
		this.clientes = clientes;
	}

}
