package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import modelo.ClienteModelo;

public class ClienteDAO extends GenericDAO<ClienteModelo> {

	public ClienteDAO() {
		super(ClienteModelo.class);
	}
	
	public List<ClienteModelo> buscarPorFiltro(String filtro){
		try(Session session = getSession()){
			String hql = "FROM tb_clientes WHERE nombre ILIKE :filtro OR apellido ILIKE :filtro "
					+ " OR documento ILIKE :filtro ORDER BY id";
			Query<ClienteModelo> query = session.createQuery(hql, ClienteModelo.class);
			query.setParameter("filtro", "%"+filtro+"%");
			return query.getResultList();
		}
	}

}
