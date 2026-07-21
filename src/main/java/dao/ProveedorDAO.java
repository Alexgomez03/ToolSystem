package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import modelo.ProveedorModelo;

public class ProveedorDAO extends GenericDAO<ProveedorModelo> {

	public ProveedorDAO() {
		super(ProveedorModelo.class);
	}

	public List<ProveedorModelo> buscarPorFiltro(String filtro){
		try(Session session = getSession()){
			String hql = "FROM tb_proveedores WHERE razonSocial ILIKE :filtro OR nombreFantasia ILIKE :filtro "
					+ " OR ruc ILIKE :filtro ORDER BY id";
			Query<ProveedorModelo> query = session.createQuery(hql, ProveedorModelo.class);
			query.setParameter("filtro", "%"+filtro+"%");
			return query.getResultList();
		}
	}

}
