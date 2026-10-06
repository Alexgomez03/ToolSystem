package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import modelo.UsuarioModelo;

public class UsuarioDAO extends GenericDAO<UsuarioModelo> {

	public UsuarioDAO() {
		super(UsuarioModelo.class);
	}

	// Búsqueda exacta por nombre de usuario, para el login. Case-insensitive. 
	public UsuarioModelo buscarPorUsuario(String usuario) {
		try (Session session = getSession()) {
			String hql = "FROM tb_usuarios WHERE LOWER(usuario) = LOWER(:usuario)";
			Query<UsuarioModelo> query = session.createQuery(hql, UsuarioModelo.class);
			query.setParameter("usuario", usuario);
			List<UsuarioModelo> resultado = query.getResultList();
			return resultado.isEmpty() ? null : resultado.get(0);
		}
	}

	public List<UsuarioModelo> buscarPorFiltro(String filtro) {
		try (Session session = getSession()) {
			String hql = "FROM tb_usuarios u WHERE u.usuario ILIKE :filtro"
					+ " OR u.funcionario.nombre ILIKE :filtro OR u.funcionario.apellido ILIKE :filtro ORDER BY u.id";
			Query<UsuarioModelo> query = session.createQuery(hql, UsuarioModelo.class);
			query.setParameter("filtro", "%" + filtro + "%");
			return query.getResultList();
		}
	}

	// ¿Ya existe otro usuario (distinto del id dado) con ese nombre de usuario? 
	public boolean existeUsuario(String usuario, Integer idExcluido) {
		try (Session session = getSession()) {
			String hql = "SELECT COUNT(u) FROM tb_usuarios u WHERE LOWER(u.usuario) = LOWER(:usuario)"
					+ (idExcluido != null ? " AND u.id <> :id" : "");
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("usuario", usuario);
			if (idExcluido != null)
				query.setParameter("id", idExcluido);
			return query.getSingleResult() > 0;
		}
	}

	
	 // Actualiza solo la marca de último acceso, sin pasar por el flujo
	 // completo de validaciones de {@code guardar} (que es para el ABM, no
	 // para este toque puntual en cada login exitoso).
	 
	public void registrarAcceso(UsuarioModelo usuario) {
		try (Session session = getSession()) {
			org.hibernate.Transaction transaction = session.beginTransaction();
			try {
				usuario.setUltimoAcceso(java.time.LocalDateTime.now());
				session.merge(usuario);
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				e.printStackTrace();
			}
		}
	}

}
