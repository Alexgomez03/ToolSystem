package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import modelo.VentaModelo;

public class VentaDAO extends GenericDAO<VentaModelo> {

	public VentaDAO() {
		super(VentaModelo.class);
	}

	public List<VentaModelo> buscarPorFiltro(String filtro){
		try(Session session = getSession()){
			String hql = "FROM tb_ventas v WHERE v.cliente.nombre ILIKE :filtro OR v.cliente.apellido ILIKE :filtro"
					+ " ORDER BY v.id";
			Query<VentaModelo> query = session.createQuery(hql, VentaModelo.class);
			query.setParameter("filtro", "%"+filtro+"%");
			return query.getResultList();
		}
	}

}
