package vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import componentes.Tema;
import controlador.ListadoVentasController;
import util.FechaUtil;

/**
 * Listado de ventas ya registradas: filtrable por cliente y rango de
 * fechas, con opción de ver el detalle de ítems de una venta seleccionada.
 * Es una consulta de solo lectura (no se edita ni elimina nada acá), por
 * eso no extiende JDialogGenerico, igual que ControlStockVista.
 */
public class ListadoVentasVista extends JDialog {

	private static final long serialVersionUID = 1L;

	private JtextFieldGenerico tfCliente;
	private JFormattedTextField tfDesde;
	private JFormattedTextField tfHasta;
	private JButton btnBuscar;
	private JButton btnVerDetalle;
	private JButton btnCerrar;
	private JTable tablaVentas;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListadoVentasVista dialog = new ListadoVentasVista();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setControlador();
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setControlador() {
		new ListadoVentasController(this);
	}

	/**
	 * Create the dialog.
	 */
	public ListadoVentasVista() {
		setTitle("Listado de Ventas");
		setBounds(100, 100, 1080, 720);
		getContentPane().setBackground(Tema.FONDO_FORMULARIO);
		getContentPane().setLayout(null);

		try {
			URL url = ListadoVentasVista.class.getResource("/imagenes/logo32.png");
			if (url != null)
				setIconImage(new ImageIcon(url).getImage());
		} catch (Exception e) {
			// sin icono, no es crítico
		}

		// ---------- Encabezado ----------
		JPanel panelHeader = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Tema.CARBON);
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.setColor(Tema.NARANJA);
				g2.fillRect(0, getHeight() - 4, getWidth(), 4);
				g2.dispose();
			}
		};
		panelHeader.setLayout(null);
		panelHeader.setBounds(0, 0, 1080, 54);
		getContentPane().add(panelHeader);

		JLabel lblTitulo = new JLabel("Listado de Ventas");
		lblTitulo.setFont(Tema.FUENTE_TITULO);
		lblTitulo.setForeground(Tema.TEXTO_CLARO);
		lblTitulo.setBounds(20, 8, 500, 34);
		panelHeader.add(lblTitulo);

		// ---------- Filtros ----------
		JLabelGenerico lblgnrcCliente = new JLabelGenerico((String) null);
		lblgnrcCliente.setText("Cliente:");
		lblgnrcCliente.setBounds(10, 66, 65, 25);
		getContentPane().add(lblgnrcCliente);

		tfCliente = new JtextFieldGenerico();
		tfCliente.setBounds(78, 66, 250, 27);
		getContentPane().add(tfCliente);

		JLabelGenerico lblgnrcDesde = new JLabelGenerico((String) null);
		lblgnrcDesde.setText("Desde:");
		lblgnrcDesde.setBounds(343, 66, 55, 25);
		getContentPane().add(lblgnrcDesde);

		tfDesde = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfDesde.setBounds(403, 66, 100, 27);
		getContentPane().add(tfDesde);

		JLabelGenerico lblgnrcHasta = new JLabelGenerico((String) null);
		lblgnrcHasta.setText("Hasta:");
		lblgnrcHasta.setBounds(513, 66, 55, 25);
		getContentPane().add(lblgnrcHasta);

		tfHasta = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfHasta.setBounds(573, 66, 100, 27);
		getContentPane().add(tfHasta);

		btnBuscar = crearBoton("Buscar");
		btnBuscar.setBounds(688, 64, 100, 31);
		getContentPane().add(btnBuscar);

		btnVerDetalle = crearBoton("Ver Detalle");
		btnVerDetalle.setBounds(798, 64, 130, 31);
		getContentPane().add(btnVerDetalle);

		btnCerrar = crearBoton("Cerrar");
		btnCerrar.setBounds(956, 64, 100, 31);
		getContentPane().add(btnCerrar);

		// ---------- Tabla de ventas ----------
		JPanel panelTabla = new JPanel();
		panelTabla.setBackground(Tema.FONDO_TARJETA);
		panelTabla.setBorder(
				new CompoundBorder(new LineBorder(Tema.BORDE_SUAVE, 1, true), new EmptyBorder(2, 2, 2, 2)));
		panelTabla.setBounds(10, 108, 1046, 564);
		getContentPane().add(panelTabla);
		panelTabla.setLayout(null);

		JScrollPane scrollVentas = new JScrollPane();
		scrollVentas.setBorder(null);
		scrollVentas.setBounds(2, 2, 1042, 560);
		panelTabla.add(scrollVentas);

		tablaVentas = new JTable();
		tablaVentas.setRowHeight(26);
		tablaVentas.setFont(Tema.FUENTE_CAMPO);
		tablaVentas.setSelectionBackground(Tema.NARANJA);
		tablaVentas.setSelectionForeground(Color.WHITE);
		tablaVentas.setGridColor(new Color(232, 232, 230));
		tablaVentas.getTableHeader().setFont(Tema.FUENTE_BOTON);
		tablaVentas.getTableHeader().setBackground(Tema.ACERO);
		tablaVentas.getTableHeader().setForeground(Color.WHITE);
		tablaVentas.getTableHeader().setOpaque(true);
		scrollVentas.setViewportView(tablaVentas);
	}

	/**
	 * Igual que en ControlStockVista: esta pantalla no es un ABM, así que
	 * sus botones no usan JButtonABM (que busca un ícono de 32px según el
	 * texto); se estilizan a mano con la misma paleta del tema.
	 */
	private JButton crearBoton(String texto) {
		JButton boton = new JButton(texto) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getModel().isPressed() ? Tema.NARANJA_OSCURO
						: getModel().isRollover() ? Tema.NARANJA : Tema.ACERO);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
				g2.dispose();
				super.paintComponent(g);
			}
		};
		boton.setFont(Tema.FUENTE_BOTON);
		boton.setForeground(Color.WHITE);
		boton.setHorizontalAlignment(SwingConstants.CENTER);
		boton.setContentAreaFilled(false);
		boton.setFocusPainted(false);
		boton.setBorderPainted(false);
		boton.setOpaque(false);
		boton.setFocusable(false);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return boton;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JtextFieldGenerico getTfCliente() {
		return tfCliente;
	}

	public JFormattedTextField getTfDesde() {
		return tfDesde;
	}

	public JFormattedTextField getTfHasta() {
		return tfHasta;
	}

	public JButton getBtnBuscar() {
		return btnBuscar;
	}

	public JButton getBtnVerDetalle() {
		return btnVerDetalle;
	}

	public JButton getBtnCerrar() {
		return btnCerrar;
	}

	public JTable getTablaVentas() {
		return tablaVentas;
	}

}
