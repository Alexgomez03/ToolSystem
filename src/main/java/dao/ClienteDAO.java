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

	
	 // Igual que {@link #buscarPorFiltro(String)} pero solo trae clientes
	 // activos (estado = true o sin cargar, para no romper registros viejos
	 // creados antes de que este campo existiera). Se usa al buscar un
	 // cliente para una venta nueva: uno dado de baja no debería poder
	 // facturarse, aunque sí puede seguir figurando en ventas ya guardadas.
	 
	public List<ClienteModelo> buscarActivos(String filtro) {
		try (Session session = getSession()) {
			String hql = "FROM tb_clientes WHERE (estado IS NULL OR estado = true) AND (nombre ILIKE :filtro"
					+ " OR apellido ILIKE :filtro OR documento ILIKE :filtro) ORDER BY id";
			Query<ClienteModelo> query = session.createQuery(hql, ClienteModelo.class);
			query.setParameter("filtro", "%" + filtro + "%");
			return query.getResultList();
		}
	}

}
