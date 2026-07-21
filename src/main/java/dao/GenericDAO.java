package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import util.HibernateUtil;

public class GenericDAO<T> {
	
	protected Class<T> clase;

	public GenericDAO(Class<T> clase) {
		super();
		this.clase = clase;
	}
	
	//LLama el contexto y abre la session
	protected Session getSession() {
		return HibernateUtil.getSessionFactory().openSession();
	}
	
	
	public void guardar(T entity) throws Exception {
		try(Session session = getSession()){
			Transaction transaction = session.beginTransaction();
			try {
				session.merge(entity);
				transaction.commit();
			} catch (Exception e) {
				if(transaction != null) transaction.rollback();
				e.printStackTrace();
				throw e;
			}
		}
	}
	
	
	public void eliminar(T entity) throws Exception{
		try(Session session = getSession()){
			Transaction transaction = session.beginTransaction();
			try {
				session.remove(session.contains(entity) ? entity : session.merge(entity));
				transaction.commit();
			} catch (Exception e) {
				if(transaction != null) transaction.rollback();
				e.printStackTrace();
				throw e;
			}
		}
	}
	
	public T recuperarPorId(Integer id) {
		try(Session session = getSession()){
			return session.find(clase, id);
		}
	}
	
	public List<T> recuperarTodo(){
		try(Session session = getSession()){
			String hql = "FROM "+clase.getName()+" e ORDER BY id";
			Query<T> query = session.createQuery(hql, clase);
			return query.getResultList();
		}
	}
	
	
	

}
