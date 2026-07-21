package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import modelo.ProductoModelo;

public class ProductoDAO extends GenericDAO<ProductoModelo> {

	public ProductoDAO() {
		super(ProductoModelo.class);
	}

	public List<ProductoModelo> buscarPorFiltro(String filtro){
		try(Session session = getSession()){
			String hql = "FROM tb_productos WHERE descripcion ILIKE :filtro OR codigo ILIKE :filtro ORDER BY id";
			Query<ProductoModelo> query = session.createQuery(hql, ProductoModelo.class);
			query.setParameter("filtro", "%"+filtro+"%");
			return query.getResultList();
		}
	}

}
