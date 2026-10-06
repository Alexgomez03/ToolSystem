package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import modelo.MarcaModelo;

public class MarcaDAO extends GenericDAO<MarcaModelo> {

	public MarcaDAO() {
		super(MarcaModelo.class);
	}

	public List<MarcaModelo> buscarPorFiltro(String filtro){
		try(Session session = getSession()){
			String hql = "FROM tb_marcas WHERE nombre ILIKE :filtro ORDER BY id";
			Query<MarcaModelo> query = session.createQuery(hql, MarcaModelo.class);
			query.setParameter("filtro", "%"+filtro+"%");
			return query.getResultList();
		}
	}

	// Solo marcas activas, para el combo de Producto. 
	public List<MarcaModelo> buscarActivas() {
		try (Session session = getSession()) {
			String hql = "FROM tb_marcas WHERE (estado IS NULL OR estado = true) ORDER BY nombre";
			Query<MarcaModelo> query = session.createQuery(hql, MarcaModelo.class);
			return query.getResultList();
		}
	}

	// Dice si ya existe otra marca (que no sea la que se está editando)
	// con ese mismo nombre.
	public boolean existeNombre(String nombre, Integer idExcluido) {
		try (Session session = getSession()) {
			String hql = "SELECT COUNT(m) FROM tb_marcas m WHERE LOWER(m.nombre) = LOWER(:nombre)"
					+ (idExcluido != null ? " AND m.id <> :id" : "");
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("nombre", nombre);
			if (idExcluido != null)
				query.setParameter("id", idExcluido);
			return query.getSingleResult() > 0;
		}
	}

}
