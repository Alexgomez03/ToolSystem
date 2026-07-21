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

}
