package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import modelo.UsuarioModelo;
import util.SesionActual;
import vista.CategoriaVista;
import vista.ClienteVista;
import vista.CompraVista;
import vista.ControlStockVista;
import vista.InformeComprasVista;
import vista.InformeCuentasPorCobrarVista;
import vista.InformeCuentasPorPagarVista;
import vista.InformeStockBajoVista;
import vista.InformeStockCompletoVista;
import vista.InformeVentasVista;
import vista.ListadoVentasVista;
import vista.FuncionarioVista;
import vista.LoginVista;
import vista.MarcaVista;
import vista.MovimientoVentasVista;
import vista.PantallaPrincipalVista;
import vista.ProductoVista;
import vista.ProveedorVista;
import vista.UsuarioVista;
import vista.VentaVista;

public class PantallaPrincipalController {

	private PantallaPrincipalVista vista;

	public PantallaPrincipalController(PantallaPrincipalVista pantallaPrincipalVista) {
		super();
		this.vista = pantallaPrincipalVista;
		setAcciones();
		mostrarUsuarioConectado();
		aplicarRestriccionesDeRol();
		cargarIndicadores();
	}

	// Muestra en la esquina superior quién inició sesión.
	private void mostrarUsuarioConectado() {
		UsuarioModelo usuario = SesionActual.getUsuario();
		if (usuario != null) {
			String rol = usuario.getRol() == UsuarioModelo.Rol.ADMINISTRADOR ? "Administrador" : "Vendedor";
			this.vista.getLblUsuarioConectado().setText(usuario.getNombreParaMostrar() + "  ·  " + rol);
		}
	}

	// Un Vendedor no debería poder tocar cosas fuera de su función diaria:
	// dar de alta funcionarios, cambiar proveedores, registrar compras
	// (afecta costos) o crear/borrar usuarios del sistema. Se ocultan esos
	// ítems de menú en vez de solo deshabilitarlos, para no dar pistas de
	// funciones que no puede usar.
	
	// Esto es un control de UI, no reemplaza una verificación de permisos
	// en la capa de datos; para este sistema de escritorio de una sola
	// base de usuarios internos se considera suficiente, pero si en algún
	// momento se expone por red convendría validar también del lado del
	// DAO/servicio.
	private void aplicarRestriccionesDeRol() {
		if (SesionActual.esAdministrador())
			return;

		this.vista.getmFuncionarios().setVisible(false);
		this.vista.getmProveedores().setVisible(false);
		this.vista.getmUsuarios().setVisible(false);
		this.vista.getmCompras().setVisible(false);
		this.vista.getBtnCompras().setVisible(false);
		this.vista.getmInformeCompras().setVisible(false);
		this.vista.getmVentas().setVisible(false);
		this.vista.getmInformeCuentasPorCobrar().setVisible(false);
		this.vista.getmInformeCuentasPorPagar().setVisible(false);
		this.vista.getmRespaldarBaseDatos().setVisible(false);
		// El dashboard con los números del día (ventas de hoy, stock
		// bajo, clientes activos) es información gerencial: a un
		// Vendedor no le sirve para vender y solo le ocuparía espacio en
		// la pantalla, así que se lo ocultamos.
		this.vista.getPanelIndicadores().setVisible(false);
	}

	// Va a buscar a la base de datos los 3 números que se muestran en el
	// panel de indicadores de la pantalla principal: cuánto se vendió
	// hoy, cuántos productos están con poco stock, y cuántos clientes
	// activos hay cargados. Se ejecuta una sola vez, al abrir la
	// pantalla (los valores no se actualizan solos mientras la pantalla
	// queda abierta; para verlos al día hay que volver a entrar).
	private void cargarIndicadores() {
		// Si no es Administrador, ni siquiera vale la pena ir a buscar
		// estos datos a la base: el panel ya está oculto para Vendedor
		// (ver aplicarRestriccionesDeRol), así que sería trabajo de más
		// para nada.
		if (!SesionActual.esAdministrador())
			return;

		try {
			java.time.LocalDate hoy = java.time.LocalDate.now();
			java.util.List<modelo.VentaModelo> ventasDeHoy = new dao.VentaDAO().buscarPorFiltro("", hoy, hoy);

			int cantidadVentas = 0;
			double totalVentas = 0;
			for (modelo.VentaModelo venta : ventasDeHoy) {
				// Una venta anulada no cuenta como venta real del día.
				if (Boolean.TRUE.equals(venta.getAnulada()))
					continue;
				cantidadVentas++;
				if (venta.getTotal() != null)
					totalVentas += venta.getTotal();
			}

			java.text.NumberFormat formatoMoneda = java.text.NumberFormat
					.getNumberInstance(java.util.Locale.of("es", "PY"));
			formatoMoneda.setMaximumFractionDigits(0);

			this.vista.getLblValorVentasHoy()
					.setText(cantidadVentas + (cantidadVentas == 1 ? " venta" : " ventas"));
			this.vista.getLblValorVentasHoy()
					.setToolTipText("Total facturado hoy: Gs. " + formatoMoneda.format(totalVentas));

			int cantidadStockBajo = new dao.ProductoDAO().buscarConStockBajo().size();
			this.vista.getLblValorStockBajo().setText(String.valueOf(cantidadStockBajo));

			int cantidadClientes = new dao.ClienteDAO().buscarActivos("").size();
			this.vista.getLblValorClientesActivos().setText(String.valueOf(cantidadClientes));
		} catch (Exception e) {
			// Si por algo falla la consulta (por ejemplo, la base todavía
			// no está lista), no tiene sentido tirar abajo toda la
			// pantalla principal por esto: se deja "-" en las tarjetas y
			// se sigue con el resto de la aplicación con normalidad.
			e.printStackTrace();
		}
	}

	private void setAcciones() {

		this.vista.getmCategorias().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirCategorias();
			}
		});

		this.vista.getmClientes().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirClientes();
			}
		});

		this.vista.getmMarcas().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirMarcas();
			}
		});

		this.vista.getmProductos().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirProductos();
			}
		});

		this.vista.getmProveedores().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirProveedores();
			}
		});

		this.vista.getmFuncionarios().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirFuncionarios();
			}
		});

		this.vista.getmUsuarios().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirUsuarios();
			}
		});

		this.vista.getmVentas().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirMovimientoVentas();
			}
		});

		this.vista.getmCompras().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirCompras();
			}
		});

		this.vista.getmControlStock().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirControlStock();
			}
		});

		this.vista.getmListadoVentas().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirListadoVentas();
			}
		});

		this.vista.getmInformeVentas().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirInformeVentas();
			}
		});

		this.vista.getmInformeStockBajo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirInformeStockBajo();
			}
		});

		this.vista.getmInformeCompras().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirInformeCompras();
			}
		});

		this.vista.getmInformeStockCompleto().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirInformeStockCompleto();
			}
		});

		this.vista.getmInformeCuentasPorCobrar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirInformeCuentasPorCobrar();
			}
		});

		this.vista.getmInformeCuentasPorPagar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirInformeCuentasPorPagar();
			}
		});

		this.vista.getmCerrarSesion().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cerrarSesion();
			}
		});

		this.vista.getmRespaldarBaseDatos().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				respaldarBaseDatos();
			}
		});

		this.vista.getBtnCliente().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirClientes();
			}
		});

		this.vista.getBtnProducto().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirProductos();
			}
		});

		this.vista.getBtnVentas().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirVentas();
			}
		});

		this.vista.getBtnCompras().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirCompras();
			}
		});
	}

	private void abrirCategorias() {
		CategoriaVista dialog = new CategoriaVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirClientes() {
		ClienteVista dialog = new ClienteVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirMarcas() {
		MarcaVista dialog = new MarcaVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirProductos() {
		ProductoVista dialog = new ProductoVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirProveedores() {
		ProveedorVista dialog = new ProveedorVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirFuncionarios() {
		FuncionarioVista dialog = new FuncionarioVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirVentas() {
		VentaVista dialog = new VentaVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirMovimientoVentas() {
		MovimientoVentasVista dialog = new MovimientoVentasVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirCompras() {
		CompraVista dialog = new CompraVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirControlStock() {
		ControlStockVista dialog = new ControlStockVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirListadoVentas() {
		ListadoVentasVista dialog = new ListadoVentasVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirUsuarios() {
		UsuarioVista dialog = new UsuarioVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirInformeVentas() {
		InformeVentasVista dialog = new InformeVentasVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirInformeStockBajo() {
		InformeStockBajoVista dialog = new InformeStockBajoVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirInformeCompras() {
		InformeComprasVista dialog = new InformeComprasVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirInformeStockCompleto() {
		InformeStockCompletoVista dialog = new InformeStockCompletoVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirInformeCuentasPorCobrar() {
		InformeCuentasPorCobrarVista dialog = new InformeCuentasPorCobrarVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirInformeCuentasPorPagar() {
		InformeCuentasPorPagarVista dialog = new InformeCuentasPorPagarVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void cerrarSesion() {
		int confirmar = JOptionPane.showConfirmDialog(vista, "¿Cerrar la sesión actual?", "Cerrar Sesión",
				JOptionPane.YES_NO_OPTION);
		if (confirmar != JOptionPane.YES_OPTION)
			return;

		SesionActual.cerrar();
		vista.dispose();

		LoginVista login = new LoginVista();
		login.setControlador();
		login.setVisible(true);
	}

	// Le pide a la persona dónde guardar el respaldo, y genera el
	// archivo en segundo plano (con un SwingWorker) para que la
	// pantalla no se quede congelada mientras se hace la copia,
	// sobre todo si la base ya tiene bastantes datos cargados.
	private void respaldarBaseDatos() {
		javax.swing.JFileChooser selector = new javax.swing.JFileChooser();
		selector.setDialogTitle("Guardar respaldo de la base de datos");
		String nombreSugerido = "respaldo_ferreteria_"
				+ java.time.LocalDateTime.now()
						.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm"))
				+ ".sql";
		selector.setSelectedFile(new java.io.File(nombreSugerido));

		if (selector.showSaveDialog(vista) != javax.swing.JFileChooser.APPROVE_OPTION)
			return;

		java.io.File archivoDestino = selector.getSelectedFile();

		vista.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
		new javax.swing.SwingWorker<Void, Void>() {
			private Exception error;

			@Override
			protected Void doInBackground() {
				try {
					util.RespaldoBaseDatos.respaldar(archivoDestino);
				} catch (Exception e) {
					error = e;
				}
				return null;
			}

			@Override
			protected void done() {
				vista.setCursor(java.awt.Cursor.getDefaultCursor());
				if (error == null)
					JOptionPane.showMessageDialog(vista,
							"Respaldo guardado correctamente en:\n" + archivoDestino.getAbsolutePath());
				else
					JOptionPane.showMessageDialog(vista, "No se pudo generar el respaldo.\n\n" + error.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
			}
		}.execute();
	}

}
