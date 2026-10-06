package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import modelo.FuncionarioModelo;

public class FuncionarioDAO extends GenericDAO<FuncionarioModelo> {

	public FuncionarioDAO() {
		super(FuncionarioModelo.class);
	}

	public List<FuncionarioModelo> buscarPorFiltro(String filtro) {
		try (Session session = getSession()) {
			String hql = "FROM tb_funcionarios WHERE nombre ILIKE :filtro OR apellido ILIKE :filtro "
					+ "OR documento ILIKE :filtro OR cargo ILIKE :filtro ORDER BY id";
			Query<FuncionarioModelo> query = session.createQuery(hql, FuncionarioModelo.class);
			query.setParameter("filtro", "%" + filtro + "%");
			return query.getResultList();
		}
	}

	
	 // Solo los funcionarios activos, para poblar combos de selección (por
	 // ejemplo, el vendedor al registrar una venta) donde uno dado de baja no
	 // debería quedar disponible para elegir en operaciones nuevas.
	 
	public List<FuncionarioModelo> buscarActivos() {
		try (Session session = getSession()) {
			String hql = "FROM tb_funcionarios WHERE estado = true ORDER BY nombre, apellido";
			Query<FuncionarioModelo> query = session.createQuery(hql, FuncionarioModelo.class);
			return query.getResultList();
		}
	}

	// Dice si ya existe otro funcionario (que no sea el que se está
	// editando) con ese mismo número de documento.
	public boolean existeDocumento(String documento, Integer idExcluido) {
		try (Session session = getSession()) {
			String hql = "SELECT COUNT(f) FROM tb_funcionarios f WHERE f.documento = :documento"
					+ (idExcluido != null ? " AND f.id <> :id" : "");
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("documento", documento);
			if (idExcluido != null)
				query.setParameter("id", idExcluido);
			return query.getSingleResult() > 0;
		}
	}

}
