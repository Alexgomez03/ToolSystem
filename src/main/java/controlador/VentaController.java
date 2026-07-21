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

import dao.ClienteDAO;
import dao.ProductoDAO;
import dao.VentaDAO;
import interfaces.InterfaceABM;
import modelo.ClienteModelo;
import modelo.DetalleVentaModelo;
import modelo.ProductoModelo;
import modelo.VentaModelo;
import tablas.ModeloTablaDetalleVenta;
import tablas.ModeloTablaVenta;
import util.FechaUtil;
import vista.VentaVista;

public class VentaController implements InterfaceABM {

	private VentaVista vista;
	private VentaModelo venta;
	private VentaDAO dao;
	private List<VentaModelo> ventas;
	private ModeloTablaVenta tabla;
	private ModeloTablaDetalleVenta tablaDetalle;

	public VentaController(VentaVista ventaVista) {
		super();
		this.vista = ventaVista;
		this.vista.setInterfaceABM(this);
		dao = new VentaDAO();
		tabla = new ModeloTablaVenta();
		this.vista.getTabla().setModel(tabla);
		tablaDetalle = new ModeloTablaDetalleVenta();
		this.vista.getTablaDetalle().setModel(tablaDetalle);
		cargarCombos();
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}

	private void cargarCombos() {
		List<ClienteModelo> clientes = new ClienteDAO().buscarPorFiltro("");
		DefaultComboBoxModel<ClienteModelo> modeloCliente = new DefaultComboBoxModel<ClienteModelo>();
		for (ClienteModelo c : clientes)
			modeloCliente.addElement(c);
		this.vista.getCbCliente().setModel(modeloCliente);
		this.vista.getCbCliente().setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof ClienteModelo)
					setText(((ClienteModelo) value).getNombre() + " " + ((ClienteModelo) value).getApellido());
				return this;
			}
		});

		List<ProductoModelo> productos = new ProductoDAO().buscarPorFiltro("");
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
					setText(((ProductoModelo) value).getDescripcion() + " - Gs. "
							+ ((ProductoModelo) value).getPrecioVenta());
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
	}

	private void cargarTabla(String filtro) {
		ventas = dao.buscarPorFiltro(filtro);
		tabla.setLista(ventas);
	}

	private void estadoInicial() {
		this.vista.getBtnNuevo().setEnabled(true);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(false);

		this.vista.getTfFecha().setEnabled(false);
		this.vista.getCbCliente().setEnabled(false);
		this.vista.getTfObservacion().setEnabled(false);
		this.vista.getCbAnulada().setEnabled(false);
		this.vista.getCbProducto().setEnabled(false);
		this.vista.getTfCantidad().setEnabled(false);
		this.vista.getBtnAgregarItem().setEnabled(false);
		this.vista.getBtnQuitarItem().setEnabled(false);

		this.vista.getTfFecha().setValue(null);
		this.vista.getTfObservacion().setText("");
		this.vista.getCbAnulada().setSelected(false);
		this.vista.getTfCantidad().setText("");
		this.vista.getTfTotal().setText("");

		tablaDetalle.setLista(new ArrayList<DetalleVentaModelo>());
		venta = null;
	}

	@Override
	public void nuevo() {
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(true);

		this.vista.getTfFecha().setEnabled(false);
		this.vista.getCbCliente().setEnabled(true);
		this.vista.getTfObservacion().setEnabled(true);
		this.vista.getCbAnulada().setEnabled(false);
		this.vista.getCbProducto().setEnabled(true);
		this.vista.getTfCantidad().setEnabled(true);
		this.vista.getBtnAgregarItem().setEnabled(true);
		this.vista.getBtnQuitarItem().setEnabled(true);

		venta = new VentaModelo();
		venta.setDetalles(new ArrayList<DetalleVentaModelo>());
		this.vista.getTfFecha().setText(FechaUtil.fechaAString(LocalDate.now()));
		tablaDetalle.setLista(venta.getDetalles());
		actualizarTotal();
	}

	private void agregarItem() {
		if (venta == null)
			return;
		ProductoModelo productoSeleccionado = (ProductoModelo) this.vista.getCbProducto().getSelectedItem();
		if (productoSeleccionado == null) {
			JOptionPane.showMessageDialog(null, "Seleccione un producto");
			return;
		}
		Double cantidad;
		try {
			cantidad = Double.valueOf(this.vista.getTfCantidad().getText().replace(",", "."));
			if (cantidad <= 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida");
			return;
		}

		DetalleVentaModelo detalle = new DetalleVentaModelo();
		detalle.setProducto(productoSeleccionado);
		detalle.setCantidad(cantidad);
		detalle.setPrecio(productoSeleccionado.getPrecioVenta());
		detalle.setVenta(venta);

		venta.getDetalles().add(detalle);
		tablaDetalle.setLista(venta.getDetalles());
		this.vista.getTfCantidad().setText("");
		actualizarTotal();
	}

	private void quitarItem() {
		if (venta == null)
			return;
		int fila = this.vista.getTablaDetalle().getSelectedRow();
		if (fila < 0) {
			JOptionPane.showMessageDialog(null, "Seleccione un item del detalle para quitar");
			return;
		}
		venta.getDetalles().remove(fila);
		tablaDetalle.setLista(venta.getDetalles());
		actualizarTotal();
	}

	private void actualizarTotal() {
		double total = tablaDetalle.getTotal();
		venta.setTotal(total);
		this.vista.getTfTotal().setText(String.valueOf(total));
	}

	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0)
			return;
		venta = ventas.get(fila);

		this.vista.getTfFecha().setText(venta.getFecha() != null ? FechaUtil.fechaAString(venta.getFecha()) : "");
		this.vista.getCbCliente().setSelectedItem(buscarClientePorId(venta.getCliente()));
		this.vista.getTfObservacion().setText(venta.getObservacion());
		this.vista.getCbAnulada().setSelected(Boolean.TRUE.equals(venta.getAnulada()));
		this.vista.getTfTotal().setText(String.valueOf(venta.getTotal()));
		tablaDetalle.setLista(venta.getDetalles() != null ? venta.getDetalles() : new ArrayList<DetalleVentaModelo>());

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(true);
	}

	@Override
	public void editar() {
		// El detalle de una venta ya registrada no se modifica, solo los
		// datos de cabecera, para mantener la consistencia del total facturado.
		this.vista.getCbCliente().setEnabled(true);
		this.vista.getTfObservacion().setEnabled(true);
		this.vista.getCbAnulada().setEnabled(true);

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(false);
	}

	@Override
	public void guardar() {
		if (this.vista.getCbCliente().getSelectedItem() == null) {
			JOptionPane.showMessageDialog(null, "Seleccione un cliente");
			return;
		}
		if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Agregue al menos un producto al detalle de la venta");
			return;
		}

		venta.setFecha(FechaUtil.stringAFecha(this.vista.getTfFecha().getText()));
		venta.setFechaRegistro(LocalDate.now());
		venta.setCliente((ClienteModelo) this.vista.getCbCliente().getSelectedItem());
		venta.setObservacion(this.vista.getTfObservacion().getText());
		venta.setAnulada(this.vista.getCbAnulada().isSelected());
		venta.setTotal(tablaDetalle.getTotal());

		try {
			dao.guardar(venta);
			cargarTabla("");
			estadoInicial();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void eliminar() {
		if (venta == null)
			return;
		int confirmacion = JOptionPane.showConfirmDialog(null,
				"Estas seguro que deseas eliminar la venta N° " + venta.getId() + "?", "Atención",
				JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(venta);
				estadoInicial();
				cargarTabla("");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void cancelar() {
		if (venta == null)
			this.vista.dispose();
		else
			estadoInicial();
	}

	@Override
	public void buscar() {
		cargarTabla(vista.getTfBuscador().getText());
	}

	// Hibernate puede entregar una instancia de ClienteModelo distinta a la
	// cargada en el combo para el mismo registro, por eso se busca por ID.
	private ClienteModelo buscarClientePorId(ClienteModelo cliente) {
		if (cliente == null)
			return null;
		javax.swing.JComboBox<ClienteModelo> combo = this.vista.getCbCliente();
		for (int i = 0; i < combo.getItemCount(); i++) {
			if (combo.getItemAt(i).getId().equals(cliente.getId()))
				return combo.getItemAt(i);
		}
		return null;
	}

}
