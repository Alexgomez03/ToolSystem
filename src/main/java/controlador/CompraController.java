package controlador;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;

import dao.CompraDAO;
import dao.ProductoDAO;
import dao.ProveedorDAO;
import dao.StockInsuficienteException;
import interfaces.InterfaceABM;
import modelo.CompraModelo;
import modelo.DetalleCompraModelo;
import modelo.ProductoModelo;
import modelo.ProveedorModelo;
import tablas.ModeloTablaCompra;
import tablas.ModeloTablaDetalleCompra;
import util.FechaUtil;
import vista.CompraVista;

public class CompraController implements InterfaceABM {

	private CompraVista vista;
	private CompraModelo compra;
	private CompraDAO dao;
	private List<CompraModelo> compras;
	private ModeloTablaCompra tabla;
	private ModeloTablaDetalleCompra tablaDetalle;

	public CompraController(CompraVista compraVista) {
		super();
		this.vista = compraVista;
		this.vista.setInterfaceABM(this);
		dao = new CompraDAO();
		tabla = new ModeloTablaCompra();
		this.vista.getTabla().setModel(tabla);
		tablaDetalle = new ModeloTablaDetalleCompra();
		this.vista.getTablaDetalle().setModel(tablaDetalle);
		cargarCombos();
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}

	private void cargarCombos() {
		// Solo proveedores y productos activos: uno dado de baja no debería
		// poder elegirse en una compra nueva.
		List<ProveedorModelo> proveedores = new ProveedorDAO().buscarActivos();
		DefaultComboBoxModel<ProveedorModelo> modeloProveedor = new DefaultComboBoxModel<ProveedorModelo>();
		for (ProveedorModelo p : proveedores)
			modeloProveedor.addElement(p);
		this.vista.getCbProveedor().setModel(modeloProveedor);
		this.vista.getCbProveedor().setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof ProveedorModelo) {
					ProveedorModelo p = (ProveedorModelo) value;
					setText(p.getNombreFantasia() != null ? p.getNombreFantasia() : p.getRazonSocial());
				}
				return this;
			}
		});

		List<ProductoModelo> productos = new ProductoDAO().buscarActivosPorFiltro("");
		DefaultComboBoxModel<ProductoModelo> modeloProducto = new DefaultComboBoxModel<ProductoModelo>();
		for (ProductoModelo p : productos)
			modeloProducto.addElement(p);
		this.vista.getCbProducto().setModel(modeloProducto);
		this.vista.getCbProducto().setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof ProductoModelo)
					setText(((ProductoModelo) value).getDescripcion());
				return this;
			}
		});
	}

	private void setAcciones() {
		this.vista.getTabla().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					seleccionarRegistro();
			}
		});

		this.vista.getBtnAgregarItem().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				agregarItem();
			}
		});

		this.vista.getBtnQuitarItem().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				quitarItem();
			}
		});

		// Cuando se cambia entre Contado y Crédito, se habilita o
		// deshabilita el campo de Monto Pagado (a Contado no hace falta
		// escribir nada, se considera pagado del todo).
		this.vista.getCbFormaPago().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actualizarHabilitadoMontoPagado();
			}
		});
	}

	// El campo "Monto Pagado" solo tiene sentido cuando la forma de
	// pago es "Crédito"; a "Contado" se considera pagado por completo
	// automáticamente.
	private void actualizarHabilitadoMontoPagado() {
		boolean esCredito = this.vista.getCbFormaPago().getSelectedItem() == modelo.FormaPago.CREDITO;
		this.vista.getTfMontoPagado().setEnabled(esCredito);
		if (!esCredito)
			this.vista.getTfMontoPagado().setText("");
	}

	private void cargarTabla(String filtro) {
		compras = dao.buscarPorFiltro(filtro);
		tabla.setLista(compras);
	}

	private void estadoInicial() {
		this.vista.getBtnNuevo().setEnabled(true);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(false);

		this.vista.getTfFecha().setEnabled(false);
		this.vista.getCbProveedor().setEnabled(false);
		this.vista.getTfNroFactura().setEnabled(false);
		this.vista.getTfObservacion().setEnabled(false);
		this.vista.getCbAnulada().setEnabled(false);
		this.vista.getCbProducto().setEnabled(false);
		this.vista.getTfCantidad().setEnabled(false);
		this.vista.getTfPrecioCosto().setEnabled(false);
		this.vista.getBtnAgregarItem().setEnabled(false);
		this.vista.getBtnQuitarItem().setEnabled(false);
		this.vista.getCbFormaPago().setEnabled(false);
		this.vista.getTfMontoPagado().setEnabled(false);

		this.vista.getTfFecha().setValue(null);
		this.vista.getTfNroFactura().setText("");
		this.vista.getTfObservacion().setText("");
		this.vista.getCbAnulada().setSelected(false);
		this.vista.getTfCantidad().setText("");
		this.vista.getTfPrecioCosto().setText("");
		this.vista.getTfTotal().setText("");
		this.vista.getTfMontoPagado().setText("");

		tablaDetalle.setLista(new ArrayList<DetalleCompraModelo>());
		compra = null;
	}

	@Override
	public void nuevo() {
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(true);

		this.vista.getTfFecha().setEnabled(false);
		this.vista.getCbProveedor().setEnabled(true);
		this.vista.getTfNroFactura().setEnabled(true);
		this.vista.getTfObservacion().setEnabled(true);
		this.vista.getCbAnulada().setEnabled(false);
		this.vista.getCbProducto().setEnabled(true);
		this.vista.getTfCantidad().setEnabled(true);
		this.vista.getTfPrecioCosto().setEnabled(true);
		this.vista.getBtnAgregarItem().setEnabled(true);
		this.vista.getBtnQuitarItem().setEnabled(true);
		this.vista.getCbFormaPago().setEnabled(true);
		this.vista.getCbFormaPago().setSelectedItem(modelo.FormaPago.CONTADO);
		actualizarHabilitadoMontoPagado();

		compra = new CompraModelo();
		compra.setDetalles(new ArrayList<DetalleCompraModelo>());
		this.vista.getTfFecha().setText(FechaUtil.fechaAString(LocalDate.now()));
		tablaDetalle.setLista(compra.getDetalles());
		actualizarTotal();
	}

	private void agregarItem() {
		if (compra == null)
			return;
		ProductoModelo productoSeleccionado = (ProductoModelo) this.vista.getCbProducto().getSelectedItem();
		if (productoSeleccionado == null) {
			JOptionPane.showMessageDialog(null, "Seleccione un producto");
			return;
		}
		Double cantidad;
		Double precioCosto;
		try {
			cantidad = Double.valueOf(this.vista.getTfCantidad().getText().replace(",", "."));
			if (cantidad <= 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida");
			return;
		}
		try {
			precioCosto = Double.valueOf(this.vista.getTfPrecioCosto().getText().replace(",", "."));
			if (precioCosto < 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Ingrese un costo unitario válido");
			return;
		}

		DetalleCompraModelo detalle = new DetalleCompraModelo();
		detalle.setProducto(productoSeleccionado);
		detalle.setCantidad(cantidad);
		detalle.setPrecioCosto(precioCosto);
		detalle.setCompra(compra);

		compra.getDetalles().add(detalle);
		tablaDetalle.setLista(compra.getDetalles());
		this.vista.getTfCantidad().setText("");
		this.vista.getTfPrecioCosto().setText("");
		actualizarTotal();
	}

	private void quitarItem() {
		if (compra == null)
			return;
		int fila = this.vista.getTablaDetalle().getSelectedRow();
		if (fila < 0) {
			JOptionPane.showMessageDialog(null, "Seleccione un item del detalle para quitar");
			return;
		}
		compra.getDetalles().remove(fila);
		tablaDetalle.setLista(compra.getDetalles());
		actualizarTotal();
	}

	private void actualizarTotal() {
		double total = tablaDetalle.getTotal();
		compra.setTotal(total);
		this.vista.getTfTotal().setText(String.valueOf(total));
	}

	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0)
			return;
		compra = compras.get(fila);

		this.vista.getTfFecha().setText(compra.getFecha() != null ? FechaUtil.fechaAString(compra.getFecha()) : "");
		this.vista.getCbProveedor().setSelectedItem(buscarProveedorPorId(compra.getProveedor()));
		this.vista.getTfNroFactura().setText(compra.getNroFactura());
		this.vista.getTfObservacion().setText(compra.getObservacion());
		this.vista.getCbAnulada().setSelected(Boolean.TRUE.equals(compra.getAnulada()));
		this.vista.getCbFormaPago()
				.setSelectedItem(compra.getFormaPago() != null ? compra.getFormaPago() : modelo.FormaPago.CONTADO);
		this.vista.getTfMontoPagado()
				.setText(compra.getMontoPagado() != null ? String.valueOf(compra.getMontoPagado()) : "0.0");
		// Todavía no se tocó "Editar", así que el campo queda
		// deshabilitado (esto es solo para ver los datos).
		this.vista.getTfMontoPagado().setEnabled(false);
		this.vista.getTfTotal().setText(String.valueOf(compra.getTotal()));
		tablaDetalle.setLista(compra.getDetalles() != null ? compra.getDetalles() : new ArrayList<DetalleCompraModelo>());

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(true);
	}

	@Override
	public void editar() {
		// El detalle de una compra ya registrada no se modifica: el stock ya
		// fue acreditado a los productos al guardarla la primera vez, así que
		// solo se permiten cambios en los datos de cabecera.
		this.vista.getCbProveedor().setEnabled(true);
		this.vista.getTfNroFactura().setEnabled(true);
		this.vista.getTfObservacion().setEnabled(true);
		this.vista.getCbAnulada().setEnabled(true);
		this.vista.getCbFormaPago().setEnabled(true);
		actualizarHabilitadoMontoPagado();

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(false);
	}

	@Override
	public void guardar() {
		if (this.vista.getCbProveedor().getSelectedItem() == null) {
			JOptionPane.showMessageDialog(null, "Seleccione un proveedor");
			return;
		}
		if (compra.getDetalles() == null || compra.getDetalles().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Agregue al menos un producto al detalle de la compra");
			return;
		}

		compra.setFecha(FechaUtil.stringAFecha(this.vista.getTfFecha().getText()));
		compra.setFechaRegistro(LocalDate.now());
		compra.setProveedor((ProveedorModelo) this.vista.getCbProveedor().getSelectedItem());
		compra.setNroFactura(this.vista.getTfNroFactura().getText());
		compra.setObservacion(this.vista.getTfObservacion().getText());
		compra.setAnulada(this.vista.getCbAnulada().isSelected());
		compra.setTotal(tablaDetalle.getTotal());

		modelo.FormaPago formaPago = (modelo.FormaPago) this.vista.getCbFormaPago().getSelectedItem();
		compra.setFormaPago(formaPago);

		if (formaPago == modelo.FormaPago.CONTADO) {
			// Al contado se considera pagada por completo.
			compra.setMontoPagado(compra.getTotal());
		} else {
			Double montoPagado;
			try {
				montoPagado = Double.valueOf(this.vista.getTfMontoPagado().getText().replace(",", "."));
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "El monto pagado debe ser un número.");
				return;
			}
			if (montoPagado < 0) {
				JOptionPane.showMessageDialog(null, "El monto pagado no puede ser negativo.");
				return;
			}
			if (montoPagado > compra.getTotal()) {
				JOptionPane.showMessageDialog(null, "El monto pagado no puede ser mayor al total de la compra.");
				return;
			}
			compra.setMontoPagado(montoPagado);
		}

		try {
			dao.guardarCompra(compra);
			JOptionPane.showMessageDialog(null,
					compra.getId() == null ? "Compra registrada. El stock de los productos fue actualizado."
							: "Compra actualizada.");
			cargarTabla("");
			estadoInicial();
		} catch (StockInsuficienteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se pudo guardar la compra.");
		}
	}

	@Override
	public void eliminar() {
		if (compra == null)
			return;
		int confirmacion = JOptionPane.showConfirmDialog(null,
				"Estas seguro que deseas eliminar la compra N° " + compra.getId() + "?\n"
						+ "El stock acreditado por esta compra NO se revertirá automáticamente (a diferencia de "
						+ "anular, eliminar borra el registro sin tocar el stock). Si lo que buscás es descontar "
						+ "esa mercadería, marcá \"Anulada\" y guardá, en vez de eliminar.",
				"Atención", JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(compra);
				estadoInicial();
				cargarTabla("");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void cancelar() {
		if (compra == null)
			this.vista.dispose();
		else
			estadoInicial();
	}

	@Override
	public void buscar() {
		cargarTabla(vista.getTfBuscador().getText());
	}

	
	 //Busca el proveedor de una compra ya guardada dentro del combo. Como
	 //el combo ahora solo trae proveedores activos, una compra vieja de un
	 //proveedor que luego fue dado de baja no lo encontraría; en ese caso
	 //se agrega temporalmente al combo para poder mostrarlo (no se guarda
	 //de nuevo salvo que el usuario edite y confirme la compra).
	 
	private ProveedorModelo buscarProveedorPorId(ProveedorModelo proveedor) {
		if (proveedor == null)
			return null;
		javax.swing.JComboBox<ProveedorModelo> combo = this.vista.getCbProveedor();
		for (int i = 0; i < combo.getItemCount(); i++) {
			if (combo.getItemAt(i).getId().equals(proveedor.getId()))
				return combo.getItemAt(i);
		}
		combo.addItem(proveedor);
		return proveedor;
	}

}
