package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static final SessionFactory SESSION_FACTORY = buildSessionFactory();
	
	//Es el metodo que configura el SessionFactory en base al archivo hibernate.cfg
	private static SessionFactory buildSessionFactory() {
		try {
			return new Configuration().configure().buildSessionFactory();
		} catch (Throwable e) {
			System.out.println("Error al iniciar el builSessionFactory");
			throw new ExceptionInInitializerError();
		}
	}
	
	//Metodo para llamar la conexion en el DAO
	public static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}
	
	//Metodo para cerrar conexion
	public static void cerrarSession() {
		getSessionFactory().close();
	}
	
	
	
	
	
	

}
