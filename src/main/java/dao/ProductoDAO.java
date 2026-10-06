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

	
	 // Solo productos activos, filtrados por código/descripción. Se usa al
	 // registrar Ventas y Compras para que un producto dado de baja no se
	 // pueda seguir vendiendo/comprando (antes {@code buscarPorFiltro} traía
	 // cualquier producto, activo o no, y el punto de venta lo dejaba facturar igual).
	 
	public List<ProductoModelo> buscarActivosPorFiltro(String filtro) {
		try (Session session = getSession()) {
			String hql = "FROM tb_productos WHERE (estado IS NULL OR estado = true)"
					+ " AND (descripcion ILIKE :filtro OR codigo ILIKE :filtro) ORDER BY descripcion";
			Query<ProductoModelo> query = session.createQuery(hql, ProductoModelo.class);
			query.setParameter("filtro", "%" + filtro + "%");
			return query.getResultList();
		}
	}

	
	 // Productos activos cuyo stock actual llegó (o cayó por debajo de) su
	 // stock mínimo definido. Los productos sin stock mínimo cargado no
	 // entran en la alerta, ya que no tienen un umbral con el que comparar.
	 
	public List<ProductoModelo> buscarConStockBajo(){
		try(Session session = getSession()){
			String hql = "FROM tb_productos WHERE stockMinimo IS NOT NULL AND stock <= stockMinimo"
					+ " AND estado = true ORDER BY descripcion";
			Query<ProductoModelo> query = session.createQuery(hql, ProductoModelo.class);
			return query.getResultList();
		}
	}

	
	 // Todos los productos, activos e inactivos, ordenados por descripción.
	 // Se usa en el informe "Stock Completo" para inventario físico: un
	 // producto dado de baja puede seguir teniendo unidades físicas en el
	 // depósito, así que conviene que también aparezca en el conteo (con su
	 // estado indicado, para que quien inventaría sepa que está descontinuado).
	 
	public List<ProductoModelo> buscarTodosParaInventario() {
		try (Session session = getSession()) {
			String hql = "FROM tb_productos ORDER BY descripcion";
			Query<ProductoModelo> query = session.createQuery(hql, ProductoModelo.class);
			return query.getResultList();
		}
	}

	// Dice si ya existe otro producto (que no sea el que se está
	// editando) con ese mismo código. El código de un producto es su
	// identidad única, así que esto se usa para avisar ANTES de
	// guardar, en vez de dejar que falle por la restricción de la base
	// de datos.
	public boolean existeCodigo(String codigo, Integer idExcluido) {
		try (Session session = getSession()) {
			String hql = "SELECT COUNT(p) FROM tb_productos p WHERE LOWER(p.codigo) = LOWER(:codigo)"
					+ (idExcluido != null ? " AND p.id <> :id" : "");
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("codigo", codigo);
			if (idExcluido != null)
				query.setParameter("id", idExcluido);
			return query.getSingleResult() > 0;
		}
	}

}
