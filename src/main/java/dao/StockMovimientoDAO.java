package dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import modelo.ProductoModelo;
import modelo.StockMovimientoModelo;
import modelo.StockMovimientoModelo.TipoMovimiento;

public class StockMovimientoDAO extends GenericDAO<StockMovimientoModelo> {

	public StockMovimientoDAO() {
		super(StockMovimientoModelo.class);
	}

	
	 // Registra un movimiento de Kardex reutilizando la sesión (y por lo tanto
	 // la transacción) del caller. Lo usan CompraDAO y VentaDAO al guardar,
	 // para que el ajuste de stock y su registro en el Kardex sean atómicos.
	 
	public void registrar(Session session, ProductoModelo producto, TipoMovimiento tipo, Double cantidad,
			Double stockResultante, String motivo) {
		StockMovimientoModelo movimiento = new StockMovimientoModelo();
		movimiento.setProducto(producto);
		movimiento.setTipo(tipo);
		movimiento.setCantidad(cantidad);
		movimiento.setStockResultante(stockResultante);
		movimiento.setMotivo(motivo);
		movimiento.setFecha(LocalDateTime.now());
		session.persist(movimiento);
	}

	
	 // Lista movimientos de Kardex para el diálogo de Control de Stock,
	 // filtrando opcionalmente por producto (código o descripción) y por un
	 // rango de fechas (ambos límites inclusive). Cualquiera de los tres
	 // filtros puede ser null/vacío para no aplicarse.
	 
	public List<StockMovimientoModelo> buscarPorFiltro(String filtroProducto, LocalDate desde, LocalDate hasta) {
		try (Session session = getSession()) {
			StringBuilder hql = new StringBuilder(
					"FROM tb_stock_movimientos m WHERE (m.producto.descripcion ILIKE :filtro OR m.producto.codigo ILIKE :filtro)");
			if (desde != null)
				hql.append(" AND m.fecha >= :desde");
			if (hasta != null)
				hql.append(" AND m.fecha <= :hasta");
			hql.append(" ORDER BY m.fecha DESC, m.id DESC");

			Query<StockMovimientoModelo> query = session.createQuery(hql.toString(), StockMovimientoModelo.class);
			query.setParameter("filtro", "%" + (filtroProducto == null ? "" : filtroProducto) + "%");
			if (desde != null)
				query.setParameter("desde", LocalDateTime.of(desde, LocalTime.MIN));
			if (hasta != null)
				query.setParameter("hasta", LocalDateTime.of(hasta, LocalTime.MAX));
			return query.getResultList();
		}
	}

}
