package persistencia.controlHibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Singelton, que nos servir� para tener acceso global a la sesion
 * 
 * @author Youss
 */

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Constuye la sessionFactory, sino puede lanza ExceptionInInitializerError
     * @return sessionFactory
     */
    
    private static SessionFactory buildSessionFactory() {
        try{
            return new Configuration().configure().buildSessionFactory(
                    new StandardServiceRegistryBuilder().configure().build());
        }catch(Throwable ex){
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionfactory() {
        return sessionFactory;
    }
}

