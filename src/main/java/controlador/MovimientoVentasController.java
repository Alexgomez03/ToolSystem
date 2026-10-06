package controlador;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dao.ClienteDAO;
import dao.FuncionarioDAO;
import dao.ProductoDAO;
import dao.StockInsuficienteException;
import dao.VentaDAO;
import interfaces.InterfaceABM;
import modelo.ClienteModelo;
import modelo.DetalleVentaModelo;
import modelo.FuncionarioModelo;
import modelo.ProductoModelo;
import modelo.UsuarioModelo;
import modelo.VentaModelo;
import tablas.ModeloTablaDetalleVenta;
import tablas.ModeloTablaVenta;
import util.FechaUtil;
import util.SesionActual;
import vista.ClienteVista;
import vista.InformeVentasVista;
import vista.MovimientoVentasVista;


 // Controla "Movimiento de Ventas": el historial administrativo de ventas
 // (lista + formulario, patrón ABM), exclusivo para el rol Administrador.
 // No debe confundirse con {@link VentaController}, que es el que atiende
 // la pantalla rápida "de mostrador" para cargar una venta nueva.
 
 // La lógica de negocio (búsqueda de cliente/producto, validación de stock,
 // bloqueo de vendedor) es la misma que en esa pantalla; lo que cambia es
 // el "shell": acá hay una lista de ventas ya registradas, y una vez
 // guardada una venta su detalle no se puede modificar, solo anularla.
 
public class MovimientoVentasController implements InterfaceABM {

	private MovimientoVentasVista vista;
	private VentaDAO dao;
	private VentaModelo venta;
	private List<VentaModelo> ventas;
	private ModeloTablaVenta tabla;
	private ModeloTablaDetalleVenta tablaDetalle;
	private ClienteModelo clienteEncontrado;
	private ProductoModelo productoEncontrado;
	// Mismo mecanismo de sugerencias que en VentaController: un
	// desplegable que aparece debajo del campo de código mientras se
	// escribe, para poder elegir el producto sin saber el código exacto.
	private JPopupMenu popupSugerenciasProducto;
	private boolean actualizandoCodigoProgramaticamente = false;

	public MovimientoVentasController(MovimientoVentasVista movimientoVentasVista) {
		super();
		this.vista = movimientoVentasVista;
		this.vista.setInterfaceABM(this);
		dao = new VentaDAO();

		tabla = new ModeloTablaVenta();
		this.vista.getTabla().setModel(tabla);
		tablaDetalle = new ModeloTablaDetalleVenta();
		this.vista.getTablaDetalle().setModel(tablaDetalle);

		cargarFuncionarios();
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}

	private void cargarFuncionarios() {
		List<FuncionarioModelo> funcionarios = new FuncionarioDAO().buscarActivos();
		DefaultComboBoxModel<FuncionarioModelo> modelo = new DefaultComboBoxModel<FuncionarioModelo>();
		for (FuncionarioModelo f : funcionarios)
			modelo.addElement(f);
		this.vista.getCbFuncionario().setModel(modelo);
		this.vista.getCbFuncionario().setSelectedIndex(-1);
		this.vista.getCbFuncionario().setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof FuncionarioModelo)
					setText(((FuncionarioModelo) value).getNombre() + " " + ((FuncionarioModelo) value).getApellido());
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

		// Cuando se cambia entre Contado y Crédito, se habilita o
		// deshabilita el campo de Monto Pagado según corresponda (a
		// Contado no hace falta escribir nada, se considera pagado del
		// todo).
		this.vista.getCbFormaPago().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actualizarHabilitadoMontoPagado();
			}
		});

		this.vista.getTfCiRuc().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buscarCliente();
			}
		});

		this.vista.getBtnRegistrarCliente().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				registrarCliente();
			}
		});

		this.vista.getBtnBuscarProducto().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buscarProducto();
			}
		});
		this.vista.getTfCodigoProducto().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buscarProducto();
			}
		});

		// Desplegable de sugerencias mientras se escribe (ver la misma
		// idea explicada en VentaController).
		this.vista.getTfCodigoProducto().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				filtrarMientrasEscribe();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filtrarMientrasEscribe();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				filtrarMientrasEscribe();
			}
		});

		this.vista.getTfCantidad().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				agregarItem();
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
				quitarItemSeleccionado();
			}
		});

		this.vista.getTablaDetalle().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE)
					quitarItemSeleccionado();
			}
		});

		this.vista.getBtnGenerarInforme().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirInformeVentas();
			}
		});
	}

	/** Abre el informe de Ventas por Período directo desde el historial. */
	private void abrirInformeVentas() {
		InformeVentasVista dialog = new InformeVentasVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(this.vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void cargarTabla(String filtro) {
		ventas = dao.buscarPorFiltro(filtro, null, null);
		tabla.setLista(ventas);
	}

	private void estadoInicial() {
		this.vista.getBtnNuevo().setEnabled(true);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(false);

		habilitarCamposCabecera(false);
		habilitarCamposDetalle(false);
		this.vista.getCbAnulada().setEnabled(false);

		this.vista.getTfFecha().setValue(null);
		this.vista.getTfCiRuc().setText("");
		this.vista.getTfRazonSocial().setText("");
		this.vista.getTfContacto().setText("");
		this.vista.getCbAnulada().setSelected(false);
		limpiarCamposProducto();
		this.vista.getTfTotal().setText("");

		clienteEncontrado = null;
		venta = null;
		tablaDetalle.setLista(new ArrayList<DetalleVentaModelo>());
	}

	private void habilitarCamposCabecera(boolean habilitar) {
		this.vista.getTfCiRuc().setEnabled(habilitar);
		this.vista.getBtnRegistrarCliente().setEnabled(habilitar);
		this.vista.getCbFuncionario().setEnabled(habilitar);
		this.vista.getCbFormaPago().setEnabled(habilitar);
		// El Monto Pagado depende de dos cosas a la vez: que se esté
		// editando/cargando (habilitar) Y que la forma de pago sea
		// Crédito. Si no se está editando, se deshabilita sin importar
		// qué forma de pago tenga elegida.
		this.vista.getTfMontoPagado()
				.setEnabled(habilitar && this.vista.getCbFormaPago().getSelectedItem() == modelo.FormaPago.CREDITO);
	}

	private void habilitarCamposDetalle(boolean habilitar) {
		this.vista.getTfCodigoProducto().setEnabled(habilitar);
		this.vista.getBtnBuscarProducto().setEnabled(habilitar);
		this.vista.getTfCantidad().setEnabled(false); // se habilita recién al encontrar un producto
		this.vista.getBtnAgregarItem().setEnabled(habilitar);
		this.vista.getBtnQuitarItem().setEnabled(habilitar);
	}

	// El campo "Monto Pagado" solo tiene sentido cuando la forma de
	// pago es "Crédito"; a "Contado" se considera pagado por completo
	// automáticamente, así que ese campo queda deshabilitado (y se
	// limpia, para no dejar un número viejo dando vueltas sin usarse).
	private void actualizarHabilitadoMontoPagado() {
		boolean esCredito = this.vista.getCbFormaPago().getSelectedItem() == modelo.FormaPago.CREDITO;
		this.vista.getTfMontoPagado().setEnabled(esCredito);
		if (!esCredito)
			this.vista.getTfMontoPagado().setText("");
	}

	@Override
	public void nuevo() {
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(true);

		habilitarCamposCabecera(true);
		habilitarCamposDetalle(true);
		this.vista.getCbAnulada().setEnabled(false);

		venta = new VentaModelo();
		venta.setDetalles(new ArrayList<DetalleVentaModelo>());
		clienteEncontrado = null;
		productoEncontrado = null;

		this.vista.getTfFecha().setText(FechaUtil.fechaAString(LocalDate.now()));
		this.vista.getTfCiRuc().setText("");
		this.vista.getTfRazonSocial().setText("");
		this.vista.getTfContacto().setText("");
		preseleccionarVendedor();
		// Por defecto arranca como "Contado".
		this.vista.getCbFormaPago().setSelectedItem(modelo.FormaPago.CONTADO);
		this.vista.getTfMontoPagado().setText("");
		actualizarHabilitadoMontoPagado();

		limpiarCamposProducto();
		tablaDetalle.setLista(venta.getDetalles());
		actualizarTotal();
	}

	
	 // Como esta pantalla es exclusiva de Administrador, el combo de
	 // vendedor siempre queda editable (no aplica el bloqueo que sí tiene
	 // la pantalla rápida de mostrador para el rol Vendedor).
	 
	private void preseleccionarVendedor() {
		this.vista.getCbFuncionario().setSelectedIndex(-1);
		this.vista.getCbFuncionario().setEnabled(true);

		if (SesionActual.getUsuario() == null)
			return;
		FuncionarioModelo funcionarioLogueado = SesionActual.getUsuario().getFuncionario();
		if (funcionarioLogueado == null)
			return;

		for (int i = 0; i < this.vista.getCbFuncionario().getItemCount(); i++) {
			if (this.vista.getCbFuncionario().getItemAt(i).getId().equals(funcionarioLogueado.getId())) {
				this.vista.getCbFuncionario().setSelectedIndex(i);
				break;
			}
		}
	}

	private void limpiarCamposProducto() {
		cerrarSugerencias();
		this.vista.getTfCodigoProducto().setText("");
		this.vista.getTfProducto().setText("");
		this.vista.getTfCantidad().setText("");
		this.vista.getTfCantidad().setEnabled(false);
		this.vista.getTfPrecioVenta().setText("");
		productoEncontrado = null;
	}

	private void buscarCliente() {
		String texto = this.vista.getTfCiRuc().getText().trim();
		if (texto.isEmpty())
			return;

		List<ClienteModelo> resultados = new ClienteDAO().buscarActivos(texto);
		if (resultados.isEmpty()) {
			clienteEncontrado = null;
			this.vista.getTfRazonSocial().setText("");
			this.vista.getTfContacto().setText("");
			JOptionPane.showMessageDialog(null,
					"No se encontró un cliente activo con ese documento. Podés registrarlo con el botón \"Registrar Cliente\", "
							+ "o verificar que no esté dado de baja.");
			return;
		}

		clienteEncontrado = resultados.get(0);
		this.vista.getTfRazonSocial().setText(clienteEncontrado.getNombre() + " " + clienteEncontrado.getApellido());
		this.vista.getTfContacto()
				.setText(clienteEncontrado.getTelefono() != null ? clienteEncontrado.getTelefono()
						: clienteEncontrado.getCorreo());
	}

	private void registrarCliente() {
		ClienteVista dialog = new ClienteVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(this.vista);
		dialog.setControlador();
		dialog.setVisible(true);

		buscarCliente();
	}

	// Se ejecuta cada vez que cambia el texto del campo de código: si hay
	// al menos 2 letras escritas, busca productos activos que coincidan
	// (por código o por descripción) y muestra el desplegable con lo que
	// encontró.
	private void filtrarMientrasEscribe() {
		if (actualizandoCodigoProgramaticamente)
			return;

		String texto = this.vista.getTfCodigoProducto().getText().trim();
		if (texto.length() < 2) {
			cerrarSugerencias();
			return;
		}

		List<ProductoModelo> resultados = new ProductoDAO().buscarActivosPorFiltro(texto);
		if (resultados.isEmpty()) {
			cerrarSugerencias();
			return;
		}
		mostrarSugerenciasProducto(resultados);
	}

	// Arma y muestra el desplegable con la lista de productos
	// encontrados, justo debajo del campo de código.
	private void mostrarSugerenciasProducto(List<ProductoModelo> resultados) {
		cerrarSugerencias();

		popupSugerenciasProducto = new JPopupMenu();
		popupSugerenciasProducto.setFocusable(false);

		int maximoAMostrar = Math.min(resultados.size(), 8);
		for (int i = 0; i < maximoAMostrar; i++) {
			ProductoModelo producto = resultados.get(i);
			String texto = producto.getCodigo() + " — " + producto.getDescripcion() + "  (Gs. "
					+ String.valueOf(producto.getPrecioVenta()) + ")";
			JMenuItem opcion = new JMenuItem(texto);
			opcion.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					seleccionarProductoDesdeSugerencia(producto);
				}
			});
			popupSugerenciasProducto.add(opcion);
		}

		popupSugerenciasProducto.show(this.vista.getTfCodigoProducto(), 0,
				this.vista.getTfCodigoProducto().getHeight());
	}

	// Completa el formulario con el producto elegido desde el
	// desplegable de sugerencias.
	private void seleccionarProductoDesdeSugerencia(ProductoModelo producto) {
		cerrarSugerencias();

		actualizandoCodigoProgramaticamente = true;
		this.vista.getTfCodigoProducto().setText(producto.getCodigo());
		actualizandoCodigoProgramaticamente = false;

		productoEncontrado = producto;
		this.vista.getTfProducto().setText(producto.getDescripcion());
		this.vista.getTfPrecioVenta().setText(String.valueOf(producto.getPrecioVenta()));
		this.vista.getTfCantidad().setEnabled(true);
		this.vista.getTfCantidad().setText("");
		this.vista.getTfCantidad().requestFocusInWindow();
	}

	// Cierra el desplegable de sugerencias si estaba abierto.
	private void cerrarSugerencias() {
		if (popupSugerenciasProducto != null && popupSugerenciasProducto.isVisible())
			popupSugerenciasProducto.setVisible(false);
	}

	private void buscarProducto() {
		String texto = this.vista.getTfCodigoProducto().getText().trim();
		if (texto.isEmpty())
			return;
		cerrarSugerencias();

		List<ProductoModelo> resultados = new ProductoDAO().buscarActivosPorFiltro(texto);
		if (resultados.isEmpty()) {
			limpiarCamposProducto();
			this.vista.getTfCodigoProducto().setText(texto);
			JOptionPane.showMessageDialog(null, "No se encontró un producto activo con ese código.");
			return;
		}

		// Si el texto escrito coincide con varios productos, se deja
		// elegir en vez de quedarse con el primero sin avisar.
		if (resultados.size() > 1) {
			mostrarSugerenciasProducto(resultados);
			return;
		}

		productoEncontrado = resultados.get(0);
		this.vista.getTfProducto().setText(productoEncontrado.getDescripcion());
		this.vista.getTfPrecioVenta().setText(String.valueOf(productoEncontrado.getPrecioVenta()));
		this.vista.getTfCantidad().setEnabled(true);
		this.vista.getTfCantidad().setText("");
		this.vista.getTfCantidad().requestFocusInWindow();
	}

	private void agregarItem() {
		if (venta == null)
			return;
		if (productoEncontrado == null) {
			JOptionPane.showMessageDialog(null, "Busque un producto por su código antes de agregar la cantidad.");
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

		Double precio;
		try {
			precio = Double.valueOf(this.vista.getTfPrecioVenta().getText().replace(",", "."));
			if (precio < 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Ingrese un precio válido");
			return;
		}

		double yaEnDetalle = 0.0;
		for (DetalleVentaModelo d : venta.getDetalles())
			if (d.getProducto().getId().equals(productoEncontrado.getId()))
				yaEnDetalle += d.getCantidad();

		if (cantidad + yaEnDetalle > productoEncontrado.getStock()) {
			JOptionPane.showMessageDialog(null,
					"Stock insuficiente para \"" + productoEncontrado.getDescripcion() + "\". Disponible: "
							+ (productoEncontrado.getStock() - yaEnDetalle) + " " + productoEncontrado.getUnidadMedida());
			return;
		}

		DetalleVentaModelo detalle = new DetalleVentaModelo();
		detalle.setProducto(productoEncontrado);
		detalle.setCantidad(cantidad);
		detalle.setPrecio(precio);
		detalle.setVenta(venta);

		venta.getDetalles().add(detalle);
		tablaDetalle.setLista(venta.getDetalles());
		actualizarTotal();

		limpiarCamposProducto();
		this.vista.getTfCodigoProducto().requestFocusInWindow();
	}

	private void quitarItemSeleccionado() {
		if (venta == null || !this.vista.getBtnQuitarItem().isEnabled())
			return;
		int fila = this.vista.getTablaDetalle().getSelectedRow();
		if (fila < 0) {
			JOptionPane.showMessageDialog(null, "Seleccione un ítem del detalle para quitar");
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
		productoEncontrado = null;
		clienteEncontrado = venta.getCliente();

		this.vista.getTfFecha().setText(venta.getFecha() != null ? FechaUtil.fechaAString(venta.getFecha()) : "");
		this.vista.getTfCiRuc().setText(venta.getCliente() != null ? venta.getCliente().getDocumento() : "");
		this.vista.getTfRazonSocial()
				.setText(venta.getCliente() != null
						? venta.getCliente().getNombre() + " " + venta.getCliente().getApellido()
						: "");
		this.vista.getTfContacto().setText(venta.getCliente() != null
				? (venta.getCliente().getTelefono() != null ? venta.getCliente().getTelefono()
						: venta.getCliente().getCorreo())
				: "");
		this.vista.getCbFuncionario().setSelectedItem(buscarFuncionarioPorId(venta.getFuncionario()));
		this.vista.getCbAnulada().setSelected(Boolean.TRUE.equals(venta.getAnulada()));
		this.vista.getCbFormaPago()
				.setSelectedItem(venta.getFormaPago() != null ? venta.getFormaPago() : modelo.FormaPago.CONTADO);
		this.vista.getTfMontoPagado()
				.setText(venta.getMontoPagado() != null ? String.valueOf(venta.getMontoPagado()) : "0.0");
		// Recién ahora, con la venta ya cargada en el formulario, se
		// decide si el campo de Monto Pagado queda habilitado. Como
		// todavía no se tocó "Editar", va a quedar deshabilitado (es
		// solo para ver los datos), y se habilita recién al editar.
		actualizarHabilitadoMontoPagado();
		this.vista.getTfMontoPagado().setEnabled(false);
		this.vista.getTfTotal().setText(String.valueOf(venta.getTotal()));
		limpiarCamposProducto();
		tablaDetalle.setLista(venta.getDetalles() != null ? venta.getDetalles() : new ArrayList<DetalleVentaModelo>());

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(true);
	}

	private FuncionarioModelo buscarFuncionarioPorId(FuncionarioModelo funcionario) {
		if (funcionario == null)
			return null;
		javax.swing.JComboBox<FuncionarioModelo> combo = this.vista.getCbFuncionario();
		for (int i = 0; i < combo.getItemCount(); i++) {
			if (combo.getItemAt(i).getId().equals(funcionario.getId()))
				return combo.getItemAt(i);
		}
		combo.addItem(funcionario);
		return funcionario;
	}

	@Override
	public void editar() {
		// El detalle de una venta ya registrada no se modifica: el stock ya
		// fue descontado de los productos al guardarla la primera vez, así
		// que solo se permite marcarla como anulada. También se puede
		// cambiar la forma de pago y registrar cuánto se cobró, para
		// poder ir marcando una venta a crédito como pagada con el
		// tiempo.
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
		if (venta.getId() == null) {
			if (clienteEncontrado == null) {
				JOptionPane.showMessageDialog(null, "Busque o registre un cliente válido antes de guardar.");
				return;
			}
			if (this.vista.getCbFuncionario().getSelectedItem() == null) {
				JOptionPane.showMessageDialog(null, "Seleccione un vendedor.");
				return;
			}
			if (venta.getDetalles().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Agregue al menos un producto al detalle de la venta.");
				return;
			}
			LocalDate fecha = FechaUtil.stringAFecha(this.vista.getTfFecha().getText());
			if (fecha == null) {
				JOptionPane.showMessageDialog(null, "La fecha ingresada no es válida.");
				return;
			}

			venta.setFecha(fecha);
			venta.setFechaRegistro(LocalDate.now());
			venta.setCliente(clienteEncontrado);
			venta.setFuncionario((FuncionarioModelo) this.vista.getCbFuncionario().getSelectedItem());
			venta.setAnulada(false);
		} else {
			// Venta ya existente: lo único editable es si está anulada.
			venta.setAnulada(this.vista.getCbAnulada().isSelected());
		}
		venta.setTotal(tablaDetalle.getTotal());

		// La forma de pago se puede elegir/cambiar tanto al cargar una
		// venta nueva como al editar una ya existente (para poder ir
		// registrando cobros con el tiempo).
		modelo.FormaPago formaPago = (modelo.FormaPago) this.vista.getCbFormaPago().getSelectedItem();
		venta.setFormaPago(formaPago);

		if (formaPago == modelo.FormaPago.CONTADO) {
			// Al contado se considera cobrada por completo.
			venta.setMontoPagado(venta.getTotal());
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
			if (montoPagado > venta.getTotal()) {
				JOptionPane.showMessageDialog(null, "El monto pagado no puede ser mayor al total de la venta.");
				return;
			}
			venta.setMontoPagado(montoPagado);
		}

		try {
			List<ProductoModelo> productosConStockBajo = dao.guardarVenta(venta);
			JOptionPane.showMessageDialog(null,
					venta.getId() == null ? "Venta registrada correctamente." : "Venta actualizada.");
			avisarSiQuedoStockBajo(productosConStockBajo);
			cargarTabla("");
			estadoInicial();
		} catch (StockInsuficienteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se pudo guardar la venta.");
		}
	}

	// Si la venta que se acaba de guardar dejó algún producto en su
	// stock mínimo (o por debajo), se lo hace saber con un cuadro
	// aparte después del mensaje de confirmación.
	private void avisarSiQuedoStockBajo(List<ProductoModelo> productosConStockBajo) {
		if (productosConStockBajo == null || productosConStockBajo.isEmpty())
			return;

		StringBuilder mensaje = new StringBuilder("Atención: con esta venta, los siguientes productos quedaron "
				+ "en su stock mínimo o por debajo:\n\n");
		for (ProductoModelo producto : productosConStockBajo) {
			mensaje.append("• ").append(producto.getDescripcion()).append(": quedan ").append(producto.getStock())
					.append(" ").append(producto.getUnidadMedida()).append(" (mínimo: ")
					.append(producto.getStockMinimo()).append(")\n");
		}
		JOptionPane.showMessageDialog(null, mensaje.toString(), "Stock bajo", JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public void eliminar() {
		if (venta == null)
			return;
		int confirmacion = JOptionPane.showConfirmDialog(null,
				"¿Estás seguro que deseas eliminar la venta N° " + venta.getId() + "?\n"
						+ "El stock descontado por esta venta NO se revertirá automáticamente (a diferencia de "
						+ "anular, eliminar borra el registro sin tocar el stock). Si lo que buscás es devolver "
						+ "el stock, marcá \"Anulada\" y guardá, en vez de eliminar.",
				"Atención", JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(venta);
				estadoInicial();
				cargarTabla("");
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "No se pudo eliminar la venta.");
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

}
