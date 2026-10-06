package dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import modelo.DetalleVentaModelo;
import modelo.ProductoModelo;
import modelo.StockMovimientoModelo.TipoMovimiento;
import modelo.VentaModelo;

public class VentaDAO extends GenericDAO<VentaModelo> {

	public VentaDAO() {
		super(VentaModelo.class);
	}

	// Trae ventas para la pantalla de Listado de Ventas y para el
	// historial administrativo, filtrando opcionalmente por cliente
	// (nombre o apellido) y por un rango de fechas (los dos límites
	// incluidos). Cualquiera de los tres filtros puede venir vacío/null
	// para no aplicarse. Se ordena de la más reciente a la más antigua.
	public List<VentaModelo> buscarPorFiltro(String filtro, LocalDate desde, LocalDate hasta) {
		try (Session session = getSession()) {
			StringBuilder hql = new StringBuilder(
					"FROM tb_ventas v WHERE (v.cliente.nombre ILIKE :filtro OR v.cliente.apellido ILIKE :filtro)");
			if (desde != null)
				hql.append(" AND v.fecha >= :desde");
			if (hasta != null)
				hql.append(" AND v.fecha <= :hasta");
			hql.append(" ORDER BY v.fecha DESC, v.id DESC");

			Query<VentaModelo> query = session.createQuery(hql.toString(), VentaModelo.class);
			query.setParameter("filtro", "%" + (filtro == null ? "" : filtro) + "%");
			if (desde != null)
				query.setParameter("desde", desde);
			if (hasta != null)
				query.setParameter("hasta", hasta);
			return query.getResultList();
		}
	}

	// Trae todas las ventas a crédito que no están anuladas, para el
	// informe de Cuentas por Cobrar. Acá no se filtra por si ya están
	// pagadas del todo o no, porque el estado de pago se calcula en
	// Java (VentaModelo.getEstadoPago), no es un dato de la base; ese
	// filtro lo hace el controlador del informe después de traer la
	// lista.
	public List<VentaModelo> buscarCreditos() {
		try (Session session = getSession()) {
			String hql = "FROM tb_ventas WHERE formaPago = :formaPago"
					+ " AND (anulada IS NULL OR anulada = false) ORDER BY fecha";
			Query<VentaModelo> query = session.createQuery(hql, VentaModelo.class);
			query.setParameter("formaPago", modelo.FormaPago.CREDITO);
			return query.getResultList();
		}
	}

	// Esto es lo que se llama cada vez que se guarda una venta, ya sea
	// nueva o una edición de una que ya existía. Según el caso, hace
	// cosas distintas con el stock:
	
	// - Venta NUEVA: descuenta el stock de cada producto vendido (antes
	// revisa que haya suficiente para todos los ítems juntos).
	// - Venta que se estaba anulando recién ahora (antes no estaba
	// anulada, y ahora sí): le DEVUELVE el stock a cada producto, como
	// si la venta nunca hubiera pasado. Antes esto no se hacía, y por
	// eso el producto "quedaba colgado" sin volver al inventario.
	// - Venta que se estaba "reactivando" (antes estaba anulada, y
	// ahora se destildó "Anulada"): se vuelve a descontar el stock,
	// validando de nuevo que alcance.
	// - Si no cambió si estaba anulada o no, no se toca el stock para
	// nada (el detalle de una venta ya guardada no se puede editar).
	
	// Devuelve la lista de productos que, después de todo esto, quedaron
	// con el stock igual o por debajo de su stock mínimo, para poder
	// avisarle a la persona sin que tenga que ir a mirar el informe de
	// Stock Bajo por su cuenta.
	public List<ProductoModelo> guardarVenta(VentaModelo venta) throws StockInsuficienteException, Exception {
		boolean esNueva = venta.getId() == null;
		List<ProductoModelo> productosConStockBajo = new ArrayList<ProductoModelo>();

		try (Session session = getSession()) {
			Transaction transaction = session.beginTransaction();
			try {
				// Antes de guardar nada, hay que saber cómo estaba la
				// venta ANTES de este cambio (si ya existía), para poder
				// comparar y saber si lo que cambió fue justamente el
				// estado de "Anulada".
				Boolean anuladaAntes = null;
				if (!esNueva) {
					VentaModelo ventaActual = session.find(VentaModelo.class, venta.getId());
					anuladaAntes = ventaActual.getAnulada();
				}

				if (esNueva)
					validarStockSuficiente(session, venta.getDetalles());

				VentaModelo ventaGuardada = session.merge(venta);

				if (esNueva) {
					descontarStockYRegistrarMovimiento(session, ventaGuardada, "Venta N° " + ventaGuardada.getId(),
							productosConStockBajo);
				} else {
					boolean estabaAnulada = Boolean.TRUE.equals(anuladaAntes);
					boolean quedaAnulada = Boolean.TRUE.equals(ventaGuardada.getAnulada());

					if (quedaAnulada && !estabaAnulada) {
						// Se acaba de anular: se devuelve todo el stock
						// de esta venta, como si no se hubiera vendido.
						devolverStockYRegistrarMovimiento(session, ventaGuardada);
					} else if (!quedaAnulada && estabaAnulada) {
						// Se acaba de "reactivar" una venta que estaba
						// anulada: hay que descontar el stock de nuevo,
						// y por lo tanto validar que siga alcanzando.
						validarStockSuficiente(session, ventaGuardada.getDetalles());
						descontarStockYRegistrarMovimiento(session,
								ventaGuardada, "Venta N° " + ventaGuardada.getId() + " (reactivada)",
								productosConStockBajo);
					}
					// Si no cambió el estado de "Anulada", no se toca el
					// stock: el detalle de una venta guardada no cambia.
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

		return productosConStockBajo;
	}

	// Revisa que haya stock suficiente para vender todos los ítems del
	// detalle. Si el mismo producto aparece en más de una línea, se
	// suman las cantidades antes de comparar (para no dejar pasar un
	// caso donde cada línea por separado parece alcanzar, pero juntas
	// no). Si falta stock de algún producto, corta todo tirando
	// StockInsuficienteException, con el nombre del producto y cuánto
	// falta.
	private void validarStockSuficiente(Session session, List<DetalleVentaModelo> detalles)
			throws StockInsuficienteException {
		if (detalles == null)
			return;

		Map<Integer, Double> demandaPorProducto = new HashMap<Integer, Double>();
		for (DetalleVentaModelo detalle : detalles) {
			Integer productoId = detalle.getProducto().getId();
			Double acumulado = demandaPorProducto.get(productoId);
			demandaPorProducto.put(productoId, (acumulado == null ? 0.0 : acumulado) + detalle.getCantidad());
		}
		for (Map.Entry<Integer, Double> entry : demandaPorProducto.entrySet()) {
			ProductoModelo producto = session.find(ProductoModelo.class, entry.getKey());
			if (producto.getStock() < entry.getValue()) {
				throw new StockInsuficienteException("Stock insuficiente para \"" + producto.getDescripcion()
						+ "\". Disponible: " + producto.getStock() + " " + producto.getUnidadMedida()
						+ ", solicitado: " + entry.getValue() + ".");
			}
		}
	}

	// Le resta a cada producto del detalle la cantidad vendida, deja
	// registro de una SALIDA en el Kardex, y anota en
	// "productosConStockBajo" los que quedaron en su mínimo o por
	// debajo.
	private void descontarStockYRegistrarMovimiento(Session session, VentaModelo venta, String motivo,
			List<ProductoModelo> productosConStockBajo) {
		if (venta.getDetalles() == null)
			return;

		StockMovimientoDAO stockMovimientoDAO = new StockMovimientoDAO();
		for (DetalleVentaModelo detalle : venta.getDetalles()) {
			ProductoModelo producto = session.find(ProductoModelo.class, detalle.getProducto().getId());
			producto.setStock(producto.getStock() - detalle.getCantidad());
			session.merge(producto);
			stockMovimientoDAO.registrar(session, producto, TipoMovimiento.SALIDA, detalle.getCantidad(),
					producto.getStock(), motivo);

			if (producto.getStockMinimo() != null && producto.getStock() <= producto.getStockMinimo())
				productosConStockBajo.add(producto);
		}
	}

	// Es lo contrario del método de arriba: le devuelve a cada producto
	// del detalle la cantidad que se había vendido, y deja registro de
	// una ENTRADA en el Kardex aclarando que es por una anulación (para
	// que quede clara la diferencia con una compra real).
	private void devolverStockYRegistrarMovimiento(Session session, VentaModelo venta) {
		if (venta.getDetalles() == null)
			return;

		StockMovimientoDAO stockMovimientoDAO = new StockMovimientoDAO();
		for (DetalleVentaModelo detalle : venta.getDetalles()) {
			ProductoModelo producto = session.find(ProductoModelo.class, detalle.getProducto().getId());
			producto.setStock(producto.getStock() + detalle.getCantidad());
			session.merge(producto);
			stockMovimientoDAO.registrar(session, producto, TipoMovimiento.ENTRADA, detalle.getCantidad(),
					producto.getStock(), "Devolución por anulación de Venta N° " + venta.getId());
		}
	}

}
