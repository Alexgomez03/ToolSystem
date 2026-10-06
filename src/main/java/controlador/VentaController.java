package controlador;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import modelo.ClienteModelo;
import modelo.DetalleVentaModelo;
import modelo.FuncionarioModelo;
import modelo.ProductoModelo;
import modelo.UsuarioModelo;
import modelo.VentaModelo;
import tablas.ModeloTablaDetalleVenta;
import reportes.ItemComprobanteDTO;
import util.ConexionJasper;
import util.FechaUtil;
import util.SesionActual;
import vista.ClienteVista;
import vista.VentaVista;


 // Controla la pantalla de Venta estilo "punto de venta". A diferencia del
 // resto de los controladores del sistema, no implementa InterfaceABM: acá
 // no hay una lista de registros para navegar (Nuevo/Editar/Eliminar), solo
 // el flujo de cargar una venta nueva y guardarla.
 
public class VentaController {

	private VentaVista vista;
	private VentaDAO dao;
	private VentaModelo venta;
	private ModeloTablaDetalleVenta tablaDetalle;
	private ClienteModelo clienteEncontrado;
	private ProductoModelo productoEncontrado;
	// Es el desplegable de sugerencias que aparece debajo del campo de
	// código de producto mientras se escribe. Se guarda como campo para
	// poder cerrarlo (por ejemplo, cuando se elige un producto o cuando
	// se borra el texto).
	private JPopupMenu popupSugerenciasProducto;
	// Esta bandera es para cuando el propio código pone texto en el
	// campo (por ejemplo, al elegir una sugerencia): en ese caso no
	// queremos que se dispare de nuevo la búsqueda, porque volvería a
	// abrir el desplegable apenas se acaba de cerrar.
	private boolean actualizandoCodigoProgramaticamente = false;

	public VentaController(VentaVista ventaVista) {
		super();
		this.vista = ventaVista;
		dao = new VentaDAO();

		tablaDetalle = new ModeloTablaDetalleVenta();
		this.vista.getTablaDetalle().setModel(tablaDetalle);

		cargarFuncionarios();
		setAcciones();
		nuevaVenta();
	}

	private void cargarFuncionarios() {
		// Solo activos: un funcionario dado de baja no debe poder elegirse
		// como vendedor en una venta nueva (sí puede seguir figurando como
		// vendedor en ventas ya guardadas anteriormente, eso no cambia).
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
		// Enter en C.I./RUC busca el cliente (un JTextField dispara su
		// ActionListener al presionar Enter, no hace falta KeyListener).
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

		// Esto es lo que hace aparecer el desplegable de sugerencias
		// mientras se va escribiendo en el campo de código: cada vez que
		// cambia lo que hay escrito, vuelve a filtrar y mostrar las
		// coincidencias, sin necesidad de apretar "Buscar" ni salir de
		// la ventana. Útil para cuando no se sabe el código exacto pero
		// sí parte del nombre del producto.
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

		// Enter en Cantidad agrega el ítem al detalle (flujo tipo lector de
		// código de barras: código -> Enter -> cantidad -> Enter).
		this.vista.getTfCantidad().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				agregarItem();
			}
		});

		this.vista.getBtnCancelar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelar();
			}
		});

		this.vista.getBtnGuardar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				guardar();
			}
		});

		// Suprimir quita el ítem seleccionado del detalle.
		this.vista.getTablaDetalle().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE)
					quitarItemSeleccionado();
			}
		});
	}

	private void nuevaVenta() {
		venta = new VentaModelo();
		venta.setDetalles(new ArrayList<DetalleVentaModelo>());
		clienteEncontrado = null;
		productoEncontrado = null;

		// El código de venta se asigna recién al guardar (autogenerado por la
		// base de datos), así que mientras se carga una venta nueva el campo
		// queda vacío en vez de mostrar un texto que confunda con un error.
		this.vista.getTfCodigo().setText("");
		this.vista.getTfFecha().setText(FechaUtil.fechaAString(LocalDate.now()));

		this.vista.getTfCiRuc().setText("");
		this.vista.getTfRazonSocial().setText("");
		this.vista.getTfContacto().setText("");
		preseleccionarVendedor();
		// Por defecto arranca como "Contado", que es lo más común en el
		// mostrador; si hace falta, se cambia a "Crédito" a mano.
		this.vista.getCbFormaPago().setSelectedItem(modelo.FormaPago.CONTADO);

		limpiarCamposProducto();
		tablaDetalle.setLista(venta.getDetalles());
		actualizarTotal();
	}

	
	 // Fija quién figura como vendedor en la venta:
	 // - Si el usuario logueado es un Vendedor, se lo deja fijo con su
	 //   propio Funcionario vinculado y se deshabilita el combo: un
	 //   Vendedor no puede facturar a nombre de otra persona.
	 // - Si es Administrador, se preselecciona su Funcionario vinculado
	 //   (si tiene) como comodidad, pero el combo queda editable por si
	 //   necesita cargar la venta a nombre de otro vendedor.
	 
	private void preseleccionarVendedor() {
		this.vista.getCbFuncionario().setSelectedIndex(-1);
		this.vista.getCbFuncionario().setEnabled(true);

		if (SesionActual.getUsuario() == null)
			return;

		boolean esVendedor = SesionActual.getUsuario().getRol() == UsuarioModelo.Rol.VENDEDOR;
		FuncionarioModelo funcionarioLogueado = SesionActual.getUsuario().getFuncionario();

		if (funcionarioLogueado == null) {
			// Un Vendedor sin Funcionario vinculado a su cuenta de usuario
			// es una cuenta mal configurada: no hay a quién fijar. Se
			// avisa y se deja el combo editable como salvedad, para no
			// dejarlo sin poder facturar por un dato faltante que no
			// depende de él.
			if (esVendedor)
				JOptionPane.showMessageDialog(this.vista,
						"Tu usuario no tiene un Funcionario vinculado, así que no se puede fijar automáticamente "
								+ "el vendedor. Pedile a un Administrador que vincule tu cuenta desde el ABM de Usuarios.");
			return;
		}

		for (int i = 0; i < this.vista.getCbFuncionario().getItemCount(); i++) {
			if (this.vista.getCbFuncionario().getItemAt(i).getId().equals(funcionarioLogueado.getId())) {
				this.vista.getCbFuncionario().setSelectedIndex(i);
				break;
			}
		}

		if (esVendedor)
			this.vista.getCbFuncionario().setEnabled(false);
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

		// Solo clientes activos: uno dado de baja no debería poder
		// facturarse en una venta nueva (si el texto no da con ningún
		// activo pero sí existe inactivo, igual se informa "no encontrado"
		// para no revelar detalles de otros clientes por este medio).
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

		// Al cerrar el ABM de Clientes, reintenta la búsqueda con el mismo
		// documento por si el cliente recién se registró.
		buscarCliente();
	}

	// Se ejecuta cada vez que cambia el texto del campo de código: si hay
	// al menos 2 letras escritas, busca productos activos que coincidan
	// (por código o por descripción) y muestra el desplegable con lo que
	// encontró. Si el campo queda corto o vacío, cierra el desplegable.
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
	// encontrados, justo debajo del campo de código. Cada opción muestra
	// el código, la descripción y el precio, y con un clic se completa
	// todo el formulario con ese producto (como si se hubiera escrito el
	// código exacto y apretado "Buscar").
	private void mostrarSugerenciasProducto(List<ProductoModelo> resultados) {
		cerrarSugerencias();

		popupSugerenciasProducto = new JPopupMenu();
		popupSugerenciasProducto.setFocusable(false);

		// Se muestran como máximo 8 para no llenar la pantalla si el
		// texto escrito coincide con demasiados productos; si hace falta
		// afinar más, la persona sigue escribiendo y la lista se acorta.
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

	// Completa el formulario con el producto que se eligió desde el
	// desplegable de sugerencias, igual que si se hubiera encontrado
	// por código exacto.
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

		// Solo productos activos: uno dado de baja no debería poder
		// venderse en una venta nueva.
		List<ProductoModelo> resultados = new ProductoDAO().buscarActivosPorFiltro(texto);
		if (resultados.isEmpty()) {
			limpiarCamposProducto();
			this.vista.getTfCodigoProducto().setText(texto);
			JOptionPane.showMessageDialog(null, "No se encontró un producto activo con ese código.");
			return;
		}

		// Si el texto escrito coincide con varios productos (por ejemplo,
		// una descripción parcial), se deja elegir en vez de quedarse con
		// el primero de la lista sin avisar.
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

		// Suma lo que ya está cargado en el carrito del mismo producto, para
		// no dejar agregar más de lo que hay en stock entre varias líneas.
		double yaEnCarrito = 0.0;
		for (DetalleVentaModelo d : venta.getDetalles())
			if (d.getProducto().getId().equals(productoEncontrado.getId()))
				yaEnCarrito += d.getCantidad();

		if (cantidad + yaEnCarrito > productoEncontrado.getStock()) {
			JOptionPane.showMessageDialog(null,
					"Stock insuficiente para \"" + productoEncontrado.getDescripcion() + "\". Disponible: "
							+ (productoEncontrado.getStock() - yaEnCarrito) + " " + productoEncontrado.getUnidadMedida());
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
		int fila = this.vista.getTablaDetalle().getSelectedRow();
		if (fila < 0)
			return;
		venta.getDetalles().remove(fila);
		tablaDetalle.setLista(venta.getDetalles());
		actualizarTotal();
	}

	private void actualizarTotal() {
		double total = tablaDetalle.getTotal();
		venta.setTotal(total);
		this.vista.getLblTotal().setText("Total ₲: " + formatearGuaranies(total));
	}

	private String formatearGuaranies(double valor) {
		// Separador de miles con punto, sin decimales (como el resto del
		// sistema muestra los montos en guaraníes).
		return String.format("%,.0f", valor).replace(',', '.');
	}

	private void guardar() {
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
		venta.setTotal(tablaDetalle.getTotal());

		// Si es al contado, se considera cobrada por completo en el
		// momento (monto pagado = total). Si es a crédito, arranca sin
		// nada pagado; el cobro se va registrando después desde el
		// historial administrativo (Movimiento → Ventas).
		modelo.FormaPago formaPago = (modelo.FormaPago) this.vista.getCbFormaPago().getSelectedItem();
		venta.setFormaPago(formaPago);
		venta.setMontoPagado(formaPago == modelo.FormaPago.CREDITO ? 0.0 : venta.getTotal());

		try {
			List<ProductoModelo> productosConStockBajo = dao.guardarVenta(venta);
			this.vista.getTfCodigo().setText(String.valueOf(venta.getId()));
			JOptionPane.showMessageDialog(null, "Venta N° " + venta.getId() + " registrada correctamente.");
			if (JOptionPane.showConfirmDialog(null, "¿Desea imprimir el comprobante?", "Comprobante",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				imprimirComprobante(venta);
			}
			avisarSiQuedoStockBajo(productosConStockBajo);
			nuevaVenta();
		} catch (StockInsuficienteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se pudo guardar la venta.");
		}
	}

	// Si la venta que se acaba de guardar dejó algún producto en su
	// stock mínimo (o por debajo), se lo hace saber enseguida, con un
	// cuadro aparte después del mensaje de "venta registrada" — así no
	// hace falta ir a revisar el informe de Stock Bajo para enterarse.
	// Si la lista viene vacía (nadie quedó bajo), no se muestra nada.
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

	// Arma el comprobante de esta venta (un ticket simple, no una
	// factura timbrada) y lo abre en el mismo visor que usan los
	// informes — que ya trae su propio botón de imprimir, así que no
	// hace falta programar nada especial para eso.
	private void imprimirComprobante(VentaModelo venta) {
		List<ItemComprobanteDTO> items = new ArrayList<ItemComprobanteDTO>();
		for (DetalleVentaModelo detalle : venta.getDetalles()) {
			double subtotal = detalle.getCantidad() * detalle.getPrecio();
			items.add(new ItemComprobanteDTO(detalle.getProducto().getDescripcion(),
					String.valueOf(detalle.getCantidad()), String.valueOf(detalle.getPrecio()),
					String.valueOf(subtotal)));
		}

		boolean esCredito = venta.getFormaPago() == modelo.FormaPago.CREDITO;
		double total = venta.getTotal() != null ? venta.getTotal() : 0.0;
		double pagado = venta.getMontoPagado() != null ? venta.getMontoPagado() : 0.0;
		double saldo = total - pagado;

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ventaId", String.valueOf(venta.getId()));
		parametros.put("fecha", FechaUtil.fechaAString(venta.getFecha()));
		parametros.put("cliente",
				venta.getCliente() != null ? venta.getCliente().getNombre() + " " + venta.getCliente().getApellido()
						: "-");
		parametros.put("vendedor",
				venta.getFuncionario() != null
						? venta.getFuncionario().getNombre() + " " + venta.getFuncionario().getApellido()
						: "-");
		parametros.put("formaPago", esCredito ? "Crédito" : "Contado");
		parametros.put("total", "Gs. " + String.valueOf(total));
		parametros.put("pagado", "Gs. " + String.valueOf(pagado));
		parametros.put("saldo", "Gs. " + String.valueOf(saldo));
		// Jasper no maneja booleanos de forma directa en
		// printWhenExpression con parámetros String, así que se manda
		// "S"/"N" y el reporte compara contra eso.
		parametros.put("mostrarSaldo", esCredito ? "S" : "N");

		try {
			ConexionJasper<ItemComprobanteDTO> conexion = new ConexionJasper<ItemComprobanteDTO>();
			conexion.generarReporte(items, parametros, "comprobante_venta");
			conexion.ventanaReporte.setLocationRelativeTo(this.vista);
			conexion.ventanaReporte.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se pudo generar el comprobante: " + e.getMessage());
		}
	}

	private void cancelar() {
		if (venta.getDetalles().isEmpty()) {
			this.vista.dispose();
			return;
		}
		int confirmacion = JOptionPane.showConfirmDialog(null,
				"Hay una venta en curso con ítems cargados. ¿Descartarla?", "Atención", JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION)
			nuevaVenta();
	}

}
