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
import controlador.ControlStockController;
import util.FechaUtil;


 // Diálogo de Control de Stock / Kardex: muestra el historial de entradas y
 // salidas de stock (generado automáticamente por Compras y Ventas) y los
 // productos que están en o por debajo de su stock mínimo.
 
 // A diferencia del resto de las pantallas, no es un CRUD: es una consulta de
 // solo lectura, por lo que no extiende JDialogGenerico ni implementa
 // InterfaceABM.
 
public class ControlStockVista extends JDialog {

	private static final long serialVersionUID = 1L;

	private JtextFieldGenerico tfProducto;
	private JFormattedTextField tfDesde;
	private JFormattedTextField tfHasta;
	private JButton btnBuscar;
	private JButton btnCerrar;
	private JTable tablaKardex;
	private JTable tablaStockBajo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ControlStockVista dialog = new ControlStockVista();
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
		new ControlStockController(this);
	}

	/**
	 * Create the dialog.
	 */
	public ControlStockVista() {
		setBounds(100, 100, 1080, 720);
		getContentPane().setBackground(Tema.FONDO_FORMULARIO);
		getContentPane().setLayout(null);

		try {
			URL url = ControlStockVista.class.getResource("/imagenes/logo32.png");
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

		JLabel lblTitulo = new JLabel("Control de Stock / Kardex");
		lblTitulo.setFont(Tema.FUENTE_TITULO);
		lblTitulo.setForeground(Tema.TEXTO_CLARO);
		lblTitulo.setBounds(20, 8, 500, 34);
		panelHeader.add(lblTitulo);

		// ---------- Filtros ----------
		JLabelGenerico lblgnrcProducto = new JLabelGenerico((String) null);
		lblgnrcProducto.setText("Producto:");
		lblgnrcProducto.setBounds(10, 66, 70, 25);
		getContentPane().add(lblgnrcProducto);

		tfProducto = new JtextFieldGenerico();
		tfProducto.setBounds(85, 66, 250, 27);
		getContentPane().add(tfProducto);

		JLabelGenerico lblgnrcDesde = new JLabelGenerico((String) null);
		lblgnrcDesde.setText("Desde:");
		lblgnrcDesde.setBounds(350, 66, 55, 25);
		getContentPane().add(lblgnrcDesde);

		tfDesde = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfDesde.setBounds(410, 66, 100, 27);
		getContentPane().add(tfDesde);

		JLabelGenerico lblgnrcHasta = new JLabelGenerico((String) null);
		lblgnrcHasta.setText("Hasta:");
		lblgnrcHasta.setBounds(520, 66, 55, 25);
		getContentPane().add(lblgnrcHasta);

		tfHasta = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfHasta.setBounds(580, 66, 100, 27);
		getContentPane().add(tfHasta);

		btnBuscar = crearBoton("Buscar");
		btnBuscar.setBounds(695, 64, 100, 31);
		getContentPane().add(btnBuscar);

		btnCerrar = crearBoton("Cerrar");
		btnCerrar.setBounds(970, 64, 100, 31);
		getContentPane().add(btnCerrar);

		// ---------- Tabla de Kardex (izquierda) ----------
		JLabelGenerico lblgnrcKardex = new JLabelGenerico((String) null);
		lblgnrcKardex.setText("Movimientos de Stock:");
		lblgnrcKardex.setBounds(10, 108, 250, 20);
		getContentPane().add(lblgnrcKardex);

		JPanel panelKardex = new JPanel();
		panelKardex.setBackground(Tema.FONDO_TARJETA);
		panelKardex.setBorder(
				new CompoundBorder(new LineBorder(Tema.BORDE_SUAVE, 1, true), new EmptyBorder(2, 2, 2, 2)));
		panelKardex.setBounds(10, 132, 690, 540);
		getContentPane().add(panelKardex);
		panelKardex.setLayout(null);

		JScrollPane scrollKardex = new JScrollPane();
		scrollKardex.setBorder(null);
		scrollKardex.setBounds(2, 2, 686, 536);
		panelKardex.add(scrollKardex);

		tablaKardex = crearTablaEstilizada();
		scrollKardex.setViewportView(tablaKardex);

		// ---------- Tabla de Stock Bajo (derecha) ----------
		JLabelGenerico lblgnrcStockBajo = new JLabelGenerico((String) null);
		lblgnrcStockBajo.setText("Productos con Stock Bajo:");
		lblgnrcStockBajo.setBounds(715, 108, 250, 20);
		getContentPane().add(lblgnrcStockBajo);

		JPanel panelStockBajo = new JPanel();
		panelStockBajo.setBackground(Tema.FONDO_TARJETA);
		panelStockBajo.setBorder(
				new CompoundBorder(new LineBorder(Tema.BORDE_SUAVE, 1, true), new EmptyBorder(2, 2, 2, 2)));
		panelStockBajo.setBounds(715, 132, 355, 540);
		getContentPane().add(panelStockBajo);
		panelStockBajo.setLayout(null);

		JScrollPane scrollStockBajo = new JScrollPane();
		scrollStockBajo.setBorder(null);
		scrollStockBajo.setBounds(2, 2, 351, 536);
		panelStockBajo.add(scrollStockBajo);

		tablaStockBajo = crearTablaEstilizada();
		scrollStockBajo.setViewportView(tablaStockBajo);
	}

	private JTable crearTablaEstilizada() {
		JTable tabla = new JTable();
		tabla.setRowHeight(26);
		tabla.setFont(Tema.FUENTE_CAMPO);
		tabla.setSelectionBackground(Tema.NARANJA);
		tabla.setSelectionForeground(Color.WHITE);
		tabla.setGridColor(new Color(232, 232, 230));
		tabla.getTableHeader().setFont(Tema.FUENTE_BOTON);
		tabla.getTableHeader().setBackground(Tema.ACERO);
		tabla.getTableHeader().setForeground(Color.WHITE);
		tabla.getTableHeader().setOpaque(true);
		return tabla;
	}

	/**
	 * Este diálogo no sigue el estilo de JButtonABM (que busca un ícono según
	 * el texto del botón); son solo dos acciones (Buscar/Cerrar) sin ícono
	 * propio, así que se estilizan a mano manteniendo la paleta del tema.
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

	public JtextFieldGenerico getTfProducto() {
		return tfProducto;
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

	public JButton getBtnCerrar() {
		return btnCerrar;
	}

	public JTable getTablaKardex() {
		return tablaKardex;
	}

	public JTable getTablaStockBajo() {
		return tablaStockBajo;
	}

}
