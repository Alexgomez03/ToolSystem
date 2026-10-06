package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import modelo.CategoriaModelo;

public class CategoriaDAO extends GenericDAO<CategoriaModelo> {

	public CategoriaDAO() {
		super(CategoriaModelo.class);
	}

	public List<CategoriaModelo> buscarPorFiltro(String filtro){
		try(Session session = getSession()){
			String hql = "FROM tb_categorias WHERE nombre ILIKE :filtro ORDER BY id";
			Query<CategoriaModelo> query = session.createQuery(hql, CategoriaModelo.class);
			query.setParameter("filtro", "%"+filtro+"%");
			return query.getResultList();
		}
	}

	// Solo categorías activas, para el combo de Producto. */
	public List<CategoriaModelo> buscarActivas() {
		try (Session session = getSession()) {
			String hql = "FROM tb_categorias WHERE (estado IS NULL OR estado = true) ORDER BY nombre";
			Query<CategoriaModelo> query = session.createQuery(hql, CategoriaModelo.class);
			return query.getResultList();
		}
	}

	// Dice si ya existe otra categoría (que no sea la que se está
	// editando) con ese mismo nombre. Así se le avisa a la persona ANTES
	// de guardar, en vez de que se entere por un error feo de la base.
	public boolean existeNombre(String nombre, Integer idExcluido) {
		try (Session session = getSession()) {
			String hql = "SELECT COUNT(c) FROM tb_categorias c WHERE LOWER(c.nombre) = LOWER(:nombre)"
					+ (idExcluido != null ? " AND c.id <> :id" : "");
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("nombre", nombre);
			if (idExcluido != null)
				query.setParameter("id", idExcluido);
			return query.getSingleResult() > 0;
		}
	}

}
