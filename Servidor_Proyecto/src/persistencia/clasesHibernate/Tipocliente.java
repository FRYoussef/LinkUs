package persistencia.clasesHibernate;

import java.io.Serializable;

// Generated 13-may-2017 17:19:02 by Hibernate Tools 3.5.0.Final

/**
 * Tipocliente generated by hbm2java
 */
public class Tipocliente implements Serializable {

	private static final long serialVersionUID = 2207318764835172669L;
	private String tipo;

	public Tipocliente() {
	}

	public Tipocliente(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
