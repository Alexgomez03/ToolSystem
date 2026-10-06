package controlador;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;

import dao.CategoriaDAO;
import dao.MarcaDAO;
import dao.ProductoDAO;
import interfaces.InterfaceABM;
import modelo.CategoriaModelo;
import modelo.MarcaModelo;
import modelo.ProductoModelo;
import tablas.ModeloTablaProducto;
import vista.ProductoVista;

public class ProductoController implements InterfaceABM {

	private ProductoVista vista;
	private ProductoModelo producto;
	private ProductoDAO dao;
	private List<ProductoModelo> productos;
	private ModeloTablaProducto tabla;

	public ProductoController(ProductoVista productoVista) {
		super();
		this.vista = productoVista;
		this.vista.setInterfaceABM(this);
		dao = new ProductoDAO();
		tabla = new ModeloTablaProducto();
		this.vista.getTabla().setModel(tabla);
		cargarCombos();
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}

	private void cargarCombos() {
		List<CategoriaModelo> categorias = new CategoriaDAO().buscarActivas();
		DefaultComboBoxModel<CategoriaModelo> modeloCategoria = new DefaultComboBoxModel<CategoriaModelo>();
		for (CategoriaModelo c : categorias)
			modeloCategoria.addElement(c);
		this.vista.getCbCategoria().setModel(modeloCategoria);
		this.vista.getCbCategoria().setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof CategoriaModelo)
					setText(((CategoriaModelo) value).getNombre());
				return this;
			}
		});

		List<MarcaModelo> marcas = new MarcaDAO().buscarActivas();
		DefaultComboBoxModel<MarcaModelo> modeloMarca = new DefaultComboBoxModel<MarcaModelo>();
		for (MarcaModelo m : marcas)
			modeloMarca.addElement(m);
		this.vista.getCbMarca().setModel(modeloMarca);
		this.vista.getCbMarca().setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof MarcaModelo)
					setText(((MarcaModelo) value).getNombre());
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
	}

	private void cargarTabla(String filtro) {
		productos = dao.buscarPorFiltro(filtro);
		tabla.setLista(productos);
	}

	private void estadoInicial() {
		this.vista.getBtnNuevo().setEnabled(true);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(false);

		this.vista.getTfCodigo().setEnabled(false);
		this.vista.getTfDescripcion().setEnabled(false);
		this.vista.getCbCategoria().setEnabled(false);
		this.vista.getCbMarca().setEnabled(false);
		this.vista.getTfPrecioVenta().setEnabled(false);
		this.vista.getTfStock().setEnabled(false);
		this.vista.getTfStockMinimo().setEnabled(false);
		this.vista.getTfUnidadMedida().setEnabled(false);
		this.vista.getCbEstado().setEnabled(false);

		this.vista.getTfCodigo().setText("");
		this.vista.getTfDescripcion().setText("");
		this.vista.getTfPrecioVenta().setText("");
		this.vista.getTfStock().setText("");
		this.vista.getTfStockMinimo().setText("");
		this.vista.getTfUnidadMedida().setText("");
		this.vista.getCbEstado().setSelected(false);
		producto = null;
	}

	@Override
	public void nuevo() {
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(true);

		this.vista.getTfCodigo().setEnabled(true);
		this.vista.getTfDescripcion().setEnabled(true);
		this.vista.getCbCategoria().setEnabled(true);
		this.vista.getCbMarca().setEnabled(true);
		this.vista.getTfPrecioVenta().setEnabled(true);
		this.vista.getTfStock().setEnabled(true);
		this.vista.getTfStockMinimo().setEnabled(true);
		this.vista.getTfUnidadMedida().setEnabled(true);
		this.vista.getCbEstado().setEnabled(true);

		producto = new ProductoModelo();
		this.vista.getCbEstado().setSelected(true);
	}

	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0)
			return;
		producto = productos.get(fila);

		this.vista.getTfCodigo().setText(producto.getCodigo());
		this.vista.getTfDescripcion().setText(producto.getDescripcion());
		this.vista.getCbCategoria().setSelectedItem(buscarPorId(this.vista.getCbCategoria(), producto.getCategoria()));
		this.vista.getCbMarca().setSelectedItem(buscarPorId(this.vista.getCbMarca(), producto.getMarca()));
		this.vista.getTfPrecioVenta().setText(String.valueOf(producto.getPrecioVenta()));
		this.vista.getTfStock().setText(String.valueOf(producto.getStock()));
		this.vista.getTfStockMinimo().setText(producto.getStockMinimo() != null ? String.valueOf(producto.getStockMinimo()) : "");
		this.vista.getTfUnidadMedida().setText(producto.getUnidadMedida());
		this.vista.getCbEstado().setSelected(Boolean.TRUE.equals(producto.getEstado()));

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(true);
	}

	@Override
	public void editar() {
		// El código no se puede tocar acá a propósito: una vez creado un
		// producto, su código queda fijo para siempre (es su identidad
		// única). Si hiciera falta cambiarlo, la forma correcta es dar
		// de baja este producto y cargar uno nuevo con el código
		// correcto.
		this.vista.getTfDescripcion().setEnabled(true);
		this.vista.getCbCategoria().setEnabled(true);
		this.vista.getCbMarca().setEnabled(true);
		this.vista.getTfPrecioVenta().setEnabled(true);
		this.vista.getTfStock().setEnabled(true);
		this.vista.getTfStockMinimo().setEnabled(true);
		this.vista.getTfUnidadMedida().setEnabled(true);
		this.vista.getCbEstado().setEnabled(true);

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(false);
	}

	@Override
	public void guardar() {
		String descripcion = this.vista.getTfDescripcion().getText().trim();
		String codigo = this.vista.getTfCodigo().getText().trim();

		if (descripcion.isEmpty()) {
			JOptionPane.showMessageDialog(null, "La descripción es un campo obligatorio");
			return;
		}
		if (codigo.isEmpty()) {
			JOptionPane.showMessageDialog(null, "El código es un campo obligatorio");
			return;
		}
		// El código es único y, una vez creado el producto, no se puede
		// volver a cambiar (el campo queda bloqueado al editar), así que
		// esta validación de duplicado solo puede dispararse al cargar
		// un producto nuevo.
		if (producto.getId() == null && dao.existeCodigo(codigo, null)) {
			JOptionPane.showMessageDialog(null, "Ya existe un producto con ese código");
			return;
		}

		Double precio;
		Double stock;
		Double stockMinimo = null;
		try {
			precio = Double.valueOf(this.vista.getTfPrecioVenta().getText().replace(",", "."));
			stock = Double.valueOf(this.vista.getTfStock().getText().replace(",", "."));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "El precio y el stock deben ser numéricos");
			return;
		}
		if (precio < 0) {
			JOptionPane.showMessageDialog(null, "El precio de venta no puede ser negativo");
			return;
		}
		if (stock < 0) {
			JOptionPane.showMessageDialog(null, "El stock no puede ser negativo");
			return;
		}
		String textoStockMinimo = this.vista.getTfStockMinimo().getText().trim();
		if (!textoStockMinimo.isEmpty()) {
			try {
				stockMinimo = Double.valueOf(textoStockMinimo.replace(",", "."));
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "El stock mínimo debe ser numérico");
				return;
			}
			if (stockMinimo < 0) {
				JOptionPane.showMessageDialog(null, "El stock mínimo no puede ser negativo");
				return;
			}
		}

		producto.setCodigo(codigo);
		producto.setDescripcion(descripcion);
		producto.setCategoria((CategoriaModelo) this.vista.getCbCategoria().getSelectedItem());
		producto.setMarca((MarcaModelo) this.vista.getCbMarca().getSelectedItem());
		producto.setPrecioVenta(precio);
		producto.setStock(stock);
		producto.setStockMinimo(stockMinimo);
		producto.setUnidadMedida(this.vista.getTfUnidadMedida().getText());
		producto.setEstado(this.vista.getCbEstado().isSelected());

		try {
			dao.guardar(producto);
			cargarTabla("");
			estadoInicial();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se pudo guardar el producto.");
		}
	}

	@Override
	public void eliminar() {
		if (producto == null)
			return;
		int confirmacion = JOptionPane.showConfirmDialog(null,
				"Estas seguro que deseas eliminar el producto " + producto.getDescripcion() + "?", "Atención",
				JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(producto);
				estadoInicial();
				cargarTabla("");
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"No se pudo eliminar el producto. Puede que tenga ventas asociadas.");
			}
		}
	}

	@Override
	public void cancelar() {
		if (producto == null)
			this.vista.dispose();
		else
			estadoInicial();
	}

	@Override
	public void buscar() {
		cargarTabla(vista.getTfBuscador().getText());
	}

	// Los combos se cargan una sola vez al abrir la ventana; como Hibernate
	// puede entregar una instancia distinta a la del combo para el mismo
	// registro, se busca por ID en lugar de por igualdad de instancia. Si
	// el producto seleccionado tiene una categoría/marca que fue dada de
	// baja después de cargado el combo (que solo trae activas), se agrega
	// temporalmente para poder mostrarla igual.
	private CategoriaModelo buscarPorId(javax.swing.JComboBox<CategoriaModelo> combo, CategoriaModelo categoria) {
		if (categoria == null)
			return null;
		for (int i = 0; i < combo.getItemCount(); i++) {
			if (combo.getItemAt(i).getId().equals(categoria.getId()))
				return combo.getItemAt(i);
		}
		combo.addItem(categoria);
		return categoria;
	}

	private MarcaModelo buscarPorId(javax.swing.JComboBox<MarcaModelo> combo, MarcaModelo marca) {
		if (marca == null)
			return null;
		for (int i = 0; i < combo.getItemCount(); i++) {
			if (combo.getItemAt(i).getId().equals(marca.getId()))
				return combo.getItemAt(i);
		}
		combo.addItem(marca);
		return marca;
	}

}
