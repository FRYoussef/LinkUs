package persistencia.controlHibernate;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import logica.IGestorDB;
import persistencia.clasesHibernate.Asignatura;
import persistencia.clasesHibernate.Cliente;
import persistencia.clasesHibernate.Mensaje;

/**
 * Usaremos esta clase para manejar la bd con hibernate
 * @author Youss
 */

public class ControlHibernate implements IGestorDB{
	
	private SessionFactory sessionFactory;
	
	/**
	 * Inicializa la sesion para manejar la bd 
	 */
	
	public ControlHibernate(){
		sessionFactory = HibernateUtil.getSessionfactory();
	}
	
	/**
	 * Devuelve internamente el cliente asociado a ese nombre 
	 * @param nombre
	 * @param session
	 * @return
	 */
	
	private Cliente dameCliente(String nombre, Session session){
		String hql = "from Cliente as cli where cli.usuario = :nom";
		Query q = session.createQuery(hql);
		q.setParameter("nom", nombre);
		Cliente cliente = (Cliente) q.uniqueResult();
		return cliente;
	}
	
	public void cierra(){
		sessionFactory.close();
	}
	
	public synchronized HashSet<String> dameAsignaturasCliente
						(String nombre)
	{
		Session session = sessionFactory.openSession();
		HashSet<String> hs = null;
		Cliente cliente = dameCliente(nombre, session);
		if(cliente != null){
			hs = new HashSet<String>();
			Set<Asignatura> hsAsignaturas = cliente.getAsignaturas();
			for(Asignatura a : hsAsignaturas){
				hs.add(a.getNombre());
				System.out.println(a.getNombre());
			}
		}
		session.close();
		return hs;
	}
	
	public synchronized HashSet<String> dameClientesAsignatura
				(String asignatura, String filtro)
	{
		
		Session session = sessionFactory.openSession();
		//recupero la asignatura
		String hql = "from Asignatura as asig where asig.nombre = :nom";
		Query q = session.createQuery(hql);
		q.setParameter("nom", asignatura);
		Asignatura a = (Asignatura) q.uniqueResult();
		HashSet<String> hsClientes = null;
		if(a != null){
			hsClientes = new HashSet<String>();
			Set<Cliente> hsC = a.getClientes();
			for(Cliente c : hsC){
				if(!filtro.equals("Todo")){
					
					if(filtro.equals("Alumno") && c.getTipo().equals("Alumno"))
						hsClientes.add(c.getUsuario());
					else if(filtro.equals("Profesor") && c.getTipo().equals("Profesor"))
						hsClientes.add(c.getUsuario());
					
				}else if(filtro.equals("Todo")){
					hsClientes.add(c.getUsuario());
					//System.out.println(c.getUsuario());
				}	
			}
		}
			
		session.close();
		return hsClientes;
	}
	
	public synchronized int guardaMensaje(byte[] msjBytes, HashSet<String> nombres){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Set<Cliente> clientes = new HashSet<Cliente>();
		for(String nombre : nombres){
			Cliente c = dameCliente(nombre, session);
			if(c != null){
				clientes.add(c);
			}
		}
		Mensaje msj = new Mensaje(msjBytes, clientes);
		session.save(msj);
		tx.commit();
		session.close();
		return msj.getId();
	}
	
	public synchronized void borraBuzonCliente(String nombre){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		Cliente c = dameCliente(nombre, session);
		if(c != null){
			c.getMensajes().clear();
			session.update(c);
		}
		
		tx.commit();
		session.close();
	}
	
	public synchronized void borraMensaje(int id, String cliente){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Cliente c = dameCliente(cliente, session);
		if(c != null){
			c.getMensajes().remove(session.get(Mensaje.class, id));
			session.merge(c);
			//System.out.println("borrado msj " + id + ", de " + cliente);
		}
		
		tx.commit();
		session.close();
	}
	
	public synchronized int dameTipoCliente(String nombre) {
		Session session = sessionFactory.openSession();
		Cliente cliente = dameCliente(nombre, session);
		int tipo = -1;
		if(cliente != null){
			if(cliente.getTipo().equals("Profesor"))
				tipo = 0;
			else if(cliente.getTipo().equals("Alumno"))
				tipo = 1;
		}
		session.close();
		return tipo;
	}

	public synchronized String dameContrasennaCliente(String nombre) {
		Session session = sessionFactory.openSession();
		Cliente cliente = dameCliente(nombre, session);
		String con = null;
		if(cliente != null)
			con = cliente.getContrasenna();
		session.close();
		return con;
	}

	public synchronized HashSet<byte[]> dameObjMensajesCliente(String nombre) {
		Session session = sessionFactory.openSession();
		Cliente cliente = dameCliente(nombre, session);
		Set<Mensaje> hsM = cliente.getMensajes();
		HashSet<byte[]> hsOM = null;
		if(!hsM.isEmpty()){
			hsOM = new HashSet<byte[]>();
			for(Mensaje m : hsM){
				hsOM.add(m.getMensaje());
			}
		}
		
		session.close();
		return hsOM;
	}
}
