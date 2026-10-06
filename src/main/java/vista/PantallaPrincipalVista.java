package vista;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import componentes.JButtonAccesoDirecto;
import componentes.JMenuIntemPersonalizado;
import componentes.JPanelPersonalizado;
import componentes.Tema;

public class PantallaPrincipalVista extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanelPersonalizado panelPersonalizado = new JPanelPersonalizado("fondo_pantalla.jpg");
	private JMenuIntemPersonalizado mCategorias;
	private JMenuIntemPersonalizado mClientes;
	private JMenuIntemPersonalizado mMarcas;
	private JMenuIntemPersonalizado mProductos;
	private JMenuIntemPersonalizado mProveedores;
	private JMenuIntemPersonalizado mFuncionarios;
	private JMenuIntemPersonalizado mUsuarios;
	private JMenuIntemPersonalizado mVentas;
	private JMenuIntemPersonalizado mCompras;
	private JMenuIntemPersonalizado mControlStock;
	private JMenuIntemPersonalizado mListadoVentas;
	private JMenuIntemPersonalizado mInformeVentas;
	private JMenuIntemPersonalizado mInformeStockBajo;
	private JMenuIntemPersonalizado mInformeCompras;
	private JMenuIntemPersonalizado mInformeStockCompleto;
	private JMenuIntemPersonalizado mInformeCuentasPorCobrar;
	private JMenuIntemPersonalizado mInformeCuentasPorPagar;
	private JMenuIntemPersonalizado mCerrarSesion;
	private JMenuIntemPersonalizado mRespaldarBaseDatos;
	private JButtonAccesoDirecto btnCliente;
	private JButtonAccesoDirecto btnProducto;
	private JButtonAccesoDirecto btnVentas;
	private JButtonAccesoDirecto btnCompras;
	private JLabel lblUsuarioConectado;
	// Estas tres etiquetas son las que muestran los números grandes del
	// panel de indicadores (dashboard) de la pantalla principal. El
	// controlador las va a actualizar con datos reales apenas se abre
	// la pantalla.
	private JLabel lblValorVentasHoy;
	private JLabel lblValorStockBajo;
	private JLabel lblValorClientesActivos;
	// Este es el panel que agrupa a las 3 tarjetas de arriba, para poder
	// ocultarlas todas juntas de una sola vez cuando hace falta.
	private JPanel panelIndicadores;

	// Esta es la puerta de entrada al programa. Ahora arranca por el
	// login: recién si el usuario y la contraseña son correctos se abre
	// esta pantalla principal (ver LoginController).
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					vista.LoginVista login = new vista.LoginVista();
					login.setControlador();
					login.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setControlador() {
		new controlador.PantallaPrincipalController(this);
	}

	// Acá se arma toda la ventana principal: el menú de arriba, el
	// fondo, el saludo, las tarjetas de indicadores y los botones
	// grandes de acceso directo.
	public PantallaPrincipalVista() {
		setTitle("ToolSystem");
		setBounds(100, 100, 1920, 1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		try {
			URL url = PantallaPrincipalVista.class.getResource("/imagenes/logo32.png");
			if (url != null)
				setIconImage(new ImageIcon(url).getImage());
		} catch (Exception e) {
			// sin icono, no es critico
		}

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Tema.CARBON);
		menuBar.setBorderPainted(false);
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Registros");
		estilarMenu(mnNewMenu);
		menuBar.add(mnNewMenu);
		
		mCategorias = new JMenuIntemPersonalizado();
		mCategorias.setText("Categorias");
		mnNewMenu.add(mCategorias);
		
		mClientes = new JMenuIntemPersonalizado();
		mClientes.setText("Clientes");
		mnNewMenu.add(mClientes);
		
		mMarcas = new JMenuIntemPersonalizado();
		mMarcas.setText("Marcas");
		mnNewMenu.add(mMarcas);
		
		mProductos = new JMenuIntemPersonalizado();
		mProductos.setText("Productos");
		mnNewMenu.add(mProductos);
		
		mProveedores = new JMenuIntemPersonalizado();
		mProveedores.setText("Proveedores");
		mnNewMenu.add(mProveedores);
		
		mFuncionarios = new JMenuIntemPersonalizado();
		mFuncionarios.setText("Funcionarios");
		mnNewMenu.add(mFuncionarios);

		mUsuarios = new JMenuIntemPersonalizado();
		mUsuarios.setText("Usuarios");
		mnNewMenu.add(mUsuarios);
		
		JMenu mnNewMenu_1 = new JMenu("Movimiento");
		estilarMenu(mnNewMenu_1);
		menuBar.add(mnNewMenu_1);
		
		mVentas = new JMenuIntemPersonalizado();
		mVentas.setText("Ventas");
		mnNewMenu_1.add(mVentas);
		
		mCompras = new JMenuIntemPersonalizado();
		mCompras.setText("Compras");
		mnNewMenu_1.add(mCompras);
		
		JMenu mnNewMenu_2 = new JMenu("Listados");
		estilarMenu(mnNewMenu_2);
		menuBar.add(mnNewMenu_2);
		
		mControlStock = new JMenuIntemPersonalizado();
		mControlStock.setText("Control de Stock");
		mnNewMenu_2.add(mControlStock);
		
		mListadoVentas = new JMenuIntemPersonalizado();
		mListadoVentas.setText("Listado de Ventas");
		mnNewMenu_2.add(mListadoVentas);
		
		JMenu mnNewMenu_3 = new JMenu("Informes");
		estilarMenu(mnNewMenu_3);
		menuBar.add(mnNewMenu_3);

		mInformeVentas = new JMenuIntemPersonalizado();
		mInformeVentas.setText("Ventas por Período");
		mnNewMenu_3.add(mInformeVentas);

		mInformeStockBajo = new JMenuIntemPersonalizado();
		mInformeStockBajo.setText("Stock Bajo");
		mnNewMenu_3.add(mInformeStockBajo);

		mInformeCompras = new JMenuIntemPersonalizado();
		mInformeCompras.setText("Compras por Período");
		mnNewMenu_3.add(mInformeCompras);

		mInformeStockCompleto = new JMenuIntemPersonalizado();
		mInformeStockCompleto.setText("Stock Completo (Inventario)");
		mnNewMenu_3.add(mInformeStockCompleto);

		mInformeCuentasPorCobrar = new JMenuIntemPersonalizado();
		mInformeCuentasPorCobrar.setText("Cuentas por Cobrar");
		mnNewMenu_3.add(mInformeCuentasPorCobrar);

		mInformeCuentasPorPagar = new JMenuIntemPersonalizado();
		mInformeCuentasPorPagar.setText("Cuentas por Pagar");
		mnNewMenu_3.add(mInformeCuentasPorPagar);
		
		JMenu mnNewMenu_4 = new JMenu("Utilidades");
		estilarMenu(mnNewMenu_4);
		menuBar.add(mnNewMenu_4);

		mCerrarSesion = new JMenuIntemPersonalizado();
		mCerrarSesion.setText("Cerrar Sesión");
		mnNewMenu_4.add(mCerrarSesion);

		mRespaldarBaseDatos = new JMenuIntemPersonalizado();
		mRespaldarBaseDatos.setText("Respaldar Base de Datos");
		mnNewMenu_4.add(mRespaldarBaseDatos);
		
		setContentPane(panelPersonalizado);
		panelPersonalizado.setLayout(null);

		JLabel lblBienvenida = new JLabel("ToolSystem", SwingConstants.CENTER);
		lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 42));
		lblBienvenida.setForeground(java.awt.Color.WHITE);
		lblBienvenida.setBounds(0, 130, 1920, 55);
		panelPersonalizado.add(lblBienvenida);

		
		lblUsuarioConectado = new JLabel("", SwingConstants.RIGHT);
		lblUsuarioConectado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblUsuarioConectado.setForeground(java.awt.Color.WHITE);
		lblUsuarioConectado.setBounds(1920 - 420, 8, 400, 20);
		panelPersonalizado.add(lblUsuarioConectado);

		// Acá arman las 3 tarjetas del panel de indicadores (dashboard):
		// "Ventas de Hoy", "Stock Bajo" y "Clientes Activos". Van entre el
		// subtítulo y los botones grandes de acceso directo. Los números
		// que muestran no se cargan acá (arrancan en "-"); es el
		// controlador el que después va a buscar los datos reales a la
		// base y los pone con los "setters" de las etiquetas.
		
		// Las 3 tarjetas van adentro de un panel contenedor (sin fondo ni
		// borde propio, solo para agruparlas) y no directo sobre el fondo,
		// para que el controlador las pueda ocultar a todas juntas con un
		// solo "setVisible(false)" cuando el que inició sesión es un
		// Vendedor (a un vendedor esta información no le hace falta para
		// vender, y solo le ocuparía espacio en pantalla).
		int anchoTarjeta = 280;
		int espacioTarjeta = 40;
		int totalAnchoTarjetas = anchoTarjeta * 3 + espacioTarjeta * 2;
		int xInicialTarjetas = (1920 - totalAnchoTarjetas) / 2;
		int yTarjetas = 250;

		panelIndicadores = new JPanel();
		panelIndicadores.setOpaque(false);
		panelIndicadores.setLayout(null);
		panelIndicadores.setBounds(0, 0, 1920, yTarjetas + 130);
		panelPersonalizado.add(panelIndicadores);

		JPanel tarjetaVentasHoy = crearTarjetaIndicador("Ventas de Hoy", Tema.NARANJA);
		tarjetaVentasHoy.setBounds(xInicialTarjetas, yTarjetas, anchoTarjeta, 130);
		panelIndicadores.add(tarjetaVentasHoy);
		lblValorVentasHoy = agregarValorATarjeta(tarjetaVentasHoy);

		JPanel tarjetaStockBajo = crearTarjetaIndicador("Productos con Stock Bajo", Tema.ROJO);
		tarjetaStockBajo.setBounds(xInicialTarjetas + anchoTarjeta + espacioTarjeta, yTarjetas, anchoTarjeta, 130);
		panelIndicadores.add(tarjetaStockBajo);
		lblValorStockBajo = agregarValorATarjeta(tarjetaStockBajo);

		JPanel tarjetaClientes = crearTarjetaIndicador("Clientes Activos", Tema.ACERO);
		tarjetaClientes.setBounds(xInicialTarjetas + (anchoTarjeta + espacioTarjeta) * 2, yTarjetas, anchoTarjeta,
				130);
		panelIndicadores.add(tarjetaClientes);
		lblValorClientesActivos = agregarValorATarjeta(tarjetaClientes);

		int anchoBoton = 160;
		int espacio = 60;
		int totalAncho = anchoBoton * 4 + espacio * 3;
		int xInicial = (1920 - totalAncho) / 2;
		int y = 420;

		btnCliente = new JButtonAccesoDirecto();
		btnCliente.setText("Clientes");
		btnCliente.setBounds(xInicial, y, anchoBoton, anchoBoton);
		panelPersonalizado.add(btnCliente);
		
		btnProducto = new JButtonAccesoDirecto();
		btnProducto.setText("Productos");
		btnProducto.setBounds(xInicial + (anchoBoton + espacio), y, anchoBoton, anchoBoton);
		panelPersonalizado.add(btnProducto);
		
		btnCompras = new JButtonAccesoDirecto();
		btnCompras.setText("Compras");
		btnCompras.setBounds(xInicial + (anchoBoton + espacio) * 2, y, anchoBoton, anchoBoton);
		panelPersonalizado.add(btnCompras);
		
		btnVentas = new JButtonAccesoDirecto();
		btnVentas.setText("Ventas");
		btnVentas.setBounds(xInicial + (anchoBoton + espacio) * 3, y, anchoBoton, anchoBoton);
		panelPersonalizado.add(btnVentas);

	}

	// Arma una tarjeta vacía para el panel de indicadores: un
	// rectángulo redondeado, blanco, con una tirita de color arriba (el
	// "acento") y el título abajo del todo. El número grande se agrega
	// aparte, con agregarValorATarjeta, para no repetir código entre
	// las 3 tarjetas.
	private JPanel crearTarjetaIndicador(String titulo, Color colorAcento) {
		JPanel tarjeta = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(255, 255, 255, 235));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
				g2.setColor(colorAcento);
				g2.fillRoundRect(0, 0, getWidth(), 6, 16, 16);
				g2.dispose();
			}
		};
		tarjeta.setOpaque(false);
		tarjeta.setLayout(null);

		JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblTitulo.setForeground(Tema.TEXTO_SECUNDARIO);
		lblTitulo.setBounds(10, 95, 260, 25);
		tarjeta.add(lblTitulo);

		return tarjeta;
	}

	// Le pega a una tarjeta ya armada la etiqueta con el número grande
	// (todavía en "-", el controlador lo llena después) y devuelve esa
	// etiqueta para que el controlador la pueda ir actualizando.
	private JLabel agregarValorATarjeta(JPanel tarjeta) {
		JLabel lblValor = new JLabel("-", SwingConstants.CENTER);
		lblValor.setFont(new Font("Segoe UI", Font.BOLD, 40));
		lblValor.setForeground(Tema.CARBON);
		lblValor.setBounds(10, 25, 260, 60);
		tarjeta.add(lblValor);
		return lblValor;
	}

	private void estilarMenu(JMenu menu) {
		menu.setForeground(java.awt.Color.WHITE);
		menu.setFont(new Font("Segoe UI", Font.BOLD, 13));
		menu.setOpaque(false);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JPanelPersonalizado getPanelPersonalizado() {
		return panelPersonalizado;
	}

	public JMenuIntemPersonalizado getmCategorias() {
		return mCategorias;
	}

	public JMenuIntemPersonalizado getmClientes() {
		return mClientes;
	}

	public JMenuIntemPersonalizado getmMarcas() {
		return mMarcas;
	}

	public JMenuIntemPersonalizado getmProductos() {
		return mProductos;
	}

	public JMenuIntemPersonalizado getmProveedores() {
		return mProveedores;
	}

	public JMenuIntemPersonalizado getmFuncionarios() {
		return mFuncionarios;
	}

	public JMenuIntemPersonalizado getmUsuarios() {
		return mUsuarios;
	}

	public JMenuIntemPersonalizado getmVentas() {
		return mVentas;
	}

	public JMenuIntemPersonalizado getmCompras() {
		return mCompras;
	}

	public JMenuIntemPersonalizado getmControlStock() {
		return mControlStock;
	}

	public JMenuIntemPersonalizado getmListadoVentas() {
		return mListadoVentas;
	}

	public JMenuIntemPersonalizado getmInformeVentas() {
		return mInformeVentas;
	}

	public JMenuIntemPersonalizado getmInformeStockBajo() {
		return mInformeStockBajo;
	}

	public JMenuIntemPersonalizado getmInformeCompras() {
		return mInformeCompras;
	}

	public JMenuIntemPersonalizado getmInformeStockCompleto() {
		return mInformeStockCompleto;
	}

	public JMenuIntemPersonalizado getmInformeCuentasPorCobrar() {
		return mInformeCuentasPorCobrar;
	}

	public JMenuIntemPersonalizado getmInformeCuentasPorPagar() {
		return mInformeCuentasPorPagar;
	}

	public JMenuIntemPersonalizado getmCerrarSesion() {
		return mCerrarSesion;
	}

	public JMenuIntemPersonalizado getmRespaldarBaseDatos() {
		return mRespaldarBaseDatos;
	}

	public JLabel getLblUsuarioConectado() {
		return lblUsuarioConectado;
	}

	public JLabel getLblValorVentasHoy() {
		return lblValorVentasHoy;
	}

	public JLabel getLblValorStockBajo() {
		return lblValorStockBajo;
	}

	public JLabel getLblValorClientesActivos() {
		return lblValorClientesActivos;
	}

	public JPanel getPanelIndicadores() {
		return panelIndicadores;
	}

	public JButtonAccesoDirecto getBtnCliente() {
		return btnCliente;
	}

	public JButtonAccesoDirecto getBtnProducto() {
		return btnProducto;
	}

	public JButtonAccesoDirecto getBtnVentas() {
		return btnVentas;
	}

	public JButtonAccesoDirecto getBtnCompras() {
		return btnCompras;
	}
	
}

