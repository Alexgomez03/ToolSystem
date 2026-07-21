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

}
