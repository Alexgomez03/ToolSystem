package dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import modelo.CompraModelo;
import modelo.DetalleCompraModelo;
import modelo.ProductoModelo;
import modelo.StockMovimientoModelo.TipoMovimiento;

public class CompraDAO extends GenericDAO<CompraModelo> {

	public CompraDAO() {
		super(CompraModelo.class);
	}

	public List<CompraModelo> buscarPorFiltro(String filtro) {
		try (Session session = getSession()) {
			String hql = "FROM tb_compras c WHERE c.proveedor.razonSocial ILIKE :filtro"
					+ " OR c.proveedor.nombreFantasia ILIKE :filtro OR c.nroFactura ILIKE :filtro ORDER BY c.id";
			Query<CompraModelo> query = session.createQuery(hql, CompraModelo.class);
			query.setParameter("filtro", "%" + filtro + "%");
			return query.getResultList();
		}
	}

	// Igual que buscarPorFiltro(String) pero agregando un rango de
	// fechas opcional (cualquiera de los dos, o ambos, puede venir
	// null). Se usa en el informe "Compras por Período".
	public List<CompraModelo> buscarPorFiltro(String filtro, LocalDate desde, LocalDate hasta) {
		try (Session session = getSession()) {
			String hql = "FROM tb_compras c WHERE (c.proveedor.razonSocial ILIKE :filtro"
					+ " OR c.proveedor.nombreFantasia ILIKE :filtro OR c.nroFactura ILIKE :filtro)"
					+ (desde != null ? " AND c.fecha >= :desde" : "")
					+ (hasta != null ? " AND c.fecha <= :hasta" : "")
					+ " ORDER BY c.fecha, c.id";
			Query<CompraModelo> query = session.createQuery(hql.toString(), CompraModelo.class);
			query.setParameter("filtro", "%" + filtro + "%");
			if (desde != null)
				query.setParameter("desde", desde);
			if (hasta != null)
				query.setParameter("hasta", hasta);
			return query.getResultList();
		}
	}

	// Igual idea que VentaDAO.buscarCreditos: trae las compras a
	// crédito que no están anuladas; el filtro de pagada/pendiente lo
	// hace el controlador del informe con CompraModelo.getEstadoPago.
	public List<CompraModelo> buscarCreditos() {
		try (Session session = getSession()) {
			String hql = "FROM tb_compras WHERE formaPago = :formaPago"
					+ " AND (anulada IS NULL OR anulada = false) ORDER BY fecha";
			Query<CompraModelo> query = session.createQuery(hql, CompraModelo.class);
			query.setParameter("formaPago", modelo.FormaPago.CREDITO);
			return query.getResultList();
		}
	}

	// Mismo mecanismo que VentaDAO.guardarVenta, pero "al revés": una
	// compra nueva SUMA stock (llegó mercadería), así que:
	
	// - Compra NUEVA: suma el stock de cada producto comprado.
	// - Compra que se estaba anulando recién ahora: le RESTA el stock a
	// cada producto, como si esa mercadería nunca hubiera entrado. Antes
	// de restar nada, valida que haya suficiente (si mientras tanto ya
	// se vendió esa mercadería, no se puede anular la compra sin más:
	// se avisa y no se guarda el cambio).
	// - Compra que se estaba "reactivando" (estaba anulada y se
	// destildó "Anulada"): se le vuelve a sumar el stock, sin
	// necesidad de validar nada (sumar nunca falla).
	// - Si no cambió si estaba anulada o no, no se toca el stock.
	public void guardarCompra(CompraModelo compra) throws StockInsuficienteException, Exception {
		boolean esNueva = compra.getId() == null;

		try (Session session = getSession()) {
			Transaction transaction = session.beginTransaction();
			try {
				Boolean anuladaAntes = null;
				if (!esNueva) {
					CompraModelo compraActual = session.find(CompraModelo.class, compra.getId());
					anuladaAntes = compraActual.getAnulada();
				}

				CompraModelo compraGuardada = session.merge(compra);

				if (esNueva) {
					sumarStockYRegistrarMovimiento(session, compraGuardada, "Compra N° " + compraGuardada.getId());
				} else {
					boolean estabaAnulada = Boolean.TRUE.equals(anuladaAntes);
					boolean quedaAnulada = Boolean.TRUE.equals(compraGuardada.getAnulada());

					if (quedaAnulada && !estabaAnulada) {
						// Se acaba de anular: hay que sacar del stock la
						// mercadería que había entrado por esta compra,
						// validando antes que siga habiendo suficiente
						// (por si ya se vendió parte de esa mercadería).
						validarStockSuficienteParaAnular(session, compraGuardada.getDetalles());
						restarStockYRegistrarMovimiento(session, compraGuardada,
								"Anulación de Compra N° " + compraGuardada.getId());
					} else if (!quedaAnulada && estabaAnulada) {
						// Se acaba de "reactivar" una compra que estaba
						// anulada: se le vuelve a sumar el stock.
						sumarStockYRegistrarMovimiento(session, compraGuardada,
								"Compra N° " + compraGuardada.getId() + " (reactivada)");
					}
					// Si no cambió el estado de "Anulada", no se toca el
					// stock: el detalle de una compra guardada no cambia.
				}

				transaction.commit();
			} catch (StockInsuficienteException e) {
				if (transaction != null)
					transaction.rollback();
				throw e;
			} catch (Exception e) {
				if (transaction != null)
					transaction.rollback();
				e.printStackTrace();
				throw e;
			}
		}
	}

	// Antes de restar stock por anular una compra, hay que asegurarse
	// de que quede en 0 o más para cada producto (nunca negativo). Si
	// alguno no alcanza, corta todo con StockInsuficienteException,
	// explicando qué producto y cuánto falta.
	private void validarStockSuficienteParaAnular(Session session, List<DetalleCompraModelo> detalles)
			throws StockInsuficienteException {
		if (detalles == null)
			return;

		Map<Integer, Double> aRestarPorProducto = new HashMap<Integer, Double>();
		for (DetalleCompraModelo detalle : detalles) {
			Integer productoId = detalle.getProducto().getId();
			Double acumulado = aRestarPorProducto.get(productoId);
			aRestarPorProducto.put(productoId, (acumulado == null ? 0.0 : acumulado) + detalle.getCantidad());
		}
		for (Map.Entry<Integer, Double> entry : aRestarPorProducto.entrySet()) {
			ProductoModelo producto = session.find(ProductoModelo.class, entry.getKey());
			if (producto.getStock() < entry.getValue()) {
				throw new StockInsuficienteException("No se puede anular: \"" + producto.getDescripcion()
						+ "\" ya tiene menos stock (" + producto.getStock() + " " + producto.getUnidadMedida()
						+ ") del que entró por esta compra (" + entry.getValue()
						+ "). Probablemente ya se vendió parte de esa mercadería.");
			}
		}
	}

	// Le suma a cada producto del detalle la cantidad comprada, y deja
	// registro de una ENTRADA en el Kardex.
	private void sumarStockYRegistrarMovimiento(Session session, CompraModelo compra, String motivo) {
		if (compra.getDetalles() == null)
			return;

		StockMovimientoDAO stockMovimientoDAO = new StockMovimientoDAO();
		for (DetalleCompraModelo detalle : compra.getDetalles()) {
			ProductoModelo producto = session.find(ProductoModelo.class, detalle.getProducto().getId());
			producto.setStock(producto.getStock() + detalle.getCantidad());
			session.merge(producto);
			stockMovimientoDAO.registrar(session, producto, TipoMovimiento.ENTRADA, detalle.getCantidad(),
					producto.getStock(), motivo);
		}
	}

	// Es lo contrario: le resta a cada producto del detalle la cantidad
	// que había entrado por esta compra, y deja registro de una SALIDA
	// en el Kardex aclarando que es por una anulación.
	private void restarStockYRegistrarMovimiento(Session session, CompraModelo compra, String motivo) {
		if (compra.getDetalles() == null)
			return;

		StockMovimientoDAO stockMovimientoDAO = new StockMovimientoDAO();
		for (DetalleCompraModelo detalle : compra.getDetalles()) {
			ProductoModelo producto = session.find(ProductoModelo.class, detalle.getProducto().getId());
			producto.setStock(producto.getStock() - detalle.getCantidad());
			session.merge(producto);
			stockMovimientoDAO.registrar(session, producto, TipoMovimiento.SALIDA, detalle.getCantidad(),
					producto.getStock(), motivo);
		}
	}

}
