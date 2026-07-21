package vista;

import java.awt.EventQueue;
import java.awt.Font;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
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
	private JMenuIntemPersonalizado mVentas;
	private JButtonAccesoDirecto btnCliente;
	private JButtonAccesoDirecto btnProducto;
	private JButtonAccesoDirecto btnVentas;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaPrincipalVista frame = new PantallaPrincipalVista();
					frame.setControlador();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setControlador() {
		new controlador.PantallaPrincipalController(this);
	}

	/**
	 * Create the frame.
	 */
	public PantallaPrincipalVista() {
		setTitle("ToolSystem - Ferretería San Martín");
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
		
		JMenu mnNewMenu_1 = new JMenu("Movimiento");
		estilarMenu(mnNewMenu_1);
		menuBar.add(mnNewMenu_1);
		
		mVentas = new JMenuIntemPersonalizado();
		mVentas.setText("Ventas");
		mnNewMenu_1.add(mVentas);
		
		JMenu mnNewMenu_2 = new JMenu("Listados");
		estilarMenu(mnNewMenu_2);
		menuBar.add(mnNewMenu_2);
		
		JMenu mnNewMenu_3 = new JMenu("Informes");
		estilarMenu(mnNewMenu_3);
		menuBar.add(mnNewMenu_3);
		
		JMenu mnNewMenu_4 = new JMenu("Utilidades");
		estilarMenu(mnNewMenu_4);
		menuBar.add(mnNewMenu_4);
		
		setContentPane(panelPersonalizado);
		panelPersonalizado.setLayout(null);

		JLabel lblBienvenida = new JLabel("ToolSystem", SwingConstants.CENTER);
		lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 42));
		lblBienvenida.setForeground(java.awt.Color.WHITE);
		lblBienvenida.setBounds(0, 130, 1920, 55);
		panelPersonalizado.add(lblBienvenida);

		JLabel lblSubtitulo = new JLabel("Ferretería San Martín · Sistema de Ventas", SwingConstants.CENTER);
		lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblSubtitulo.setForeground(Tema.NARANJA);
		lblSubtitulo.setBounds(0, 190, 1920, 30);
		panelPersonalizado.add(lblSubtitulo);

		int anchoBoton = 160;
		int espacio = 70;
		int totalAncho = anchoBoton * 3 + espacio * 2;
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
		
		btnVentas = new JButtonAccesoDirecto();
		btnVentas.setText("Ventas");
		btnVentas.setBounds(xInicial + (anchoBoton + espacio) * 2, y, anchoBoton, anchoBoton);
		panelPersonalizado.add(btnVentas);

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

	public JMenuIntemPersonalizado getmVentas() {
		return mVentas;
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
	
}

