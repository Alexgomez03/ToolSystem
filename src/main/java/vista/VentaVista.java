package vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import componentes.JButtonABM;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import componentes.Tema;
import controlador.VentaController;
import modelo.FuncionarioModelo;
import util.FechaUtil;

/**
 * Pantalla de Venta estilo "punto de venta": registra una venta nueva por
 * vez (código de producto -> cantidad -> se agrega al detalle -> guardar),
 * en vez del patrón ABM de lista+formulario que usa el resto del sistema.
 * Por eso, al igual que ControlStockVista, no extiende JDialogGenerico.
 */
public class VentaVista extends JDialog {

	private static final long serialVersionUID = 1L;

	private JtextFieldGenerico tfCodigo;
	private JFormattedTextField tfFecha;
	private JtextFieldGenerico tfCiRuc;
	private JtextFieldGenerico tfRazonSocial;
	private JtextFieldGenerico tfContacto;
	private JComboBox<FuncionarioModelo> cbFuncionario;
	private JComboBox<modelo.FormaPago> cbFormaPago;
	private JButton btnRegistrarCliente;
	private JtextFieldGenerico tfCodigoProducto;
	private JtextFieldGenerico tfProducto;
	private JButton btnBuscarProducto;
	private JtextFieldGenerico tfCantidad;
	private JtextFieldGenerico tfPrecioVenta;
	private JTable tablaDetalle;
	private JButtonABM btnCancelar;
	private JButtonABM btnGuardar;
	private JLabel lblTotal;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentaVista dialog = new VentaVista();
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
		new VentaController(this);
	}

	/**
	 * Create the dialog.
	 */
	public VentaVista() {
		setTitle("Venta de Productos");
		setBounds(100, 100, 1080, 755);
		getContentPane().setBackground(Tema.FONDO_FORMULARIO);
		getContentPane().setLayout(null);

		try {
			URL url = VentaVista.class.getResource("/imagenes/logo32.png");
			if (url != null)
				setIconImage(new ImageIcon(url).getImage());
		} catch (Exception e) {
			// sin icono, no es crítico
		}

		// ---------- Encabezado (mismo estilo que JDialogGenerico) ----------
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

		JLabel lblTitulo = new JLabel("Venta de Productos");
		lblTitulo.setFont(Tema.FUENTE_TITULO);
		lblTitulo.setForeground(Tema.TEXTO_CLARO);
		lblTitulo.setBounds(20, 8, 500, 34);
		panelHeader.add(lblTitulo);

		// ---------- Fila 1: Código de venta / Fecha ----------
		JLabelGenerico lblCodigo = new JLabelGenerico("Código:");
		lblCodigo.setBounds(10, 64, 70, 22);
		getContentPane().add(lblCodigo);

		tfCodigo = new JtextFieldGenerico();
		tfCodigo.setEditable(false);
		tfCodigo.setBounds(86, 64, 110, 25);
		getContentPane().add(tfCodigo);

		JLabelGenerico lblFecha = new JLabelGenerico("Fecha:");
		lblFecha.setBounds(780, 64, 90, 22);
		getContentPane().add(lblFecha);

		tfFecha = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFecha.setBounds(876, 64, 160, 25);
		getContentPane().add(tfFecha);

		// ---------- Fila 2: Cliente + Vendedor ----------
		JLabelGenerico lblCiRuc = new JLabelGenerico("C.I. / RUC de Cliente:");
		lblCiRuc.setBounds(10, 104, 180, 20);
		getContentPane().add(lblCiRuc);

		tfCiRuc = new JtextFieldGenerico();
		tfCiRuc.setBounds(10, 127, 180, 25);
		getContentPane().add(tfCiRuc);

		JLabelGenerico lblRazonSocial = new JLabelGenerico("Nombre y Apellido:");
		lblRazonSocial.setBounds(202, 104, 220, 20);
		getContentPane().add(lblRazonSocial);

		tfRazonSocial = new JtextFieldGenerico();
		tfRazonSocial.setEditable(false);
		tfRazonSocial.setBounds(202, 127, 220, 25);
		getContentPane().add(tfRazonSocial);

		JLabelGenerico lblContacto = new JLabelGenerico("Contacto:");
		lblContacto.setBounds(434, 104, 190, 20);
		getContentPane().add(lblContacto);

		tfContacto = new JtextFieldGenerico();
		tfContacto.setEditable(false);
		tfContacto.setBounds(434, 127, 190, 25);
		getContentPane().add(tfContacto);

		JLabelGenerico lblVendedor = new JLabelGenerico("Vendedor:");
		lblVendedor.setBounds(636, 104, 220, 20);
		getContentPane().add(lblVendedor);

		cbFuncionario = new JComboBox<FuncionarioModelo>();
		cbFuncionario.setBounds(636, 127, 220, 25);
		getContentPane().add(cbFuncionario);

		btnRegistrarCliente = crearBotonAuxiliar("Registrar Cliente");
		btnRegistrarCliente.setBounds(868, 127, 168, 25);
		getContentPane().add(btnRegistrarCliente);

		// ---------- Forma de pago (contado o crédito) ----------
		JLabelGenerico lblFormaPago = new JLabelGenerico("Forma de Pago:");
		lblFormaPago.setBounds(10, 152, 130, 20);
		getContentPane().add(lblFormaPago);

		cbFormaPago = new JComboBox<modelo.FormaPago>(modelo.FormaPago.values());
		cbFormaPago.setBounds(146, 150, 160, 25);
		getContentPane().add(cbFormaPago);

		// ---------- Separador (doble línea, con el acento del header) ----------
		JPanel separador1 = new JPanel();
		separador1.setBackground(Tema.BORDE_SUAVE);
		separador1.setBounds(10, 199, 1046, 2);
		getContentPane().add(separador1);

		JPanel separador2 = new JPanel();
		separador2.setBackground(Tema.NARANJA);
		separador2.setBounds(10, 203, 1046, 2);
		getContentPane().add(separador2);

		// ---------- Fila 3: Producto ----------
		JLabelGenerico lblCodigoProducto = new JLabelGenerico("Código de Producto:");
		lblCodigoProducto.setBounds(10, 215, 180, 20);
		getContentPane().add(lblCodigoProducto);

		tfCodigoProducto = new JtextFieldGenerico();
		tfCodigoProducto.setBounds(10, 238, 180, 25);
		getContentPane().add(tfCodigoProducto);

		JLabelGenerico lblDescripcion = new JLabelGenerico("Descripción de Producto:");
		lblDescripcion.setBounds(202, 215, 250, 20);
		getContentPane().add(lblDescripcion);

		tfProducto = new JtextFieldGenerico();
		tfProducto.setEditable(false);
		tfProducto.setBounds(202, 238, 380, 25);
		getContentPane().add(tfProducto);

		btnBuscarProducto = crearBotonAuxiliar("Buscar");
		btnBuscarProducto.setBounds(594, 238, 90, 25);
		getContentPane().add(btnBuscarProducto);

		JLabelGenerico lblCantidad = new JLabelGenerico("Cantidad:");
		lblCantidad.setBounds(696, 215, 80, 20);
		getContentPane().add(lblCantidad);

		tfCantidad = new JtextFieldGenerico();
		tfCantidad.setEnabled(false);
		tfCantidad.setBounds(696, 238, 80, 25);
		getContentPane().add(tfCantidad);

		JLabelGenerico lblPrecio = new JLabelGenerico("Precio Unit.:");
		lblPrecio.setBounds(788, 215, 90, 20);
		getContentPane().add(lblPrecio);

		tfPrecioVenta = new JtextFieldGenerico();
		tfPrecioVenta.setBounds(788, 238, 190, 25);
		getContentPane().add(tfPrecioVenta);

		// ---------- Detalle de la venta (carrito) ----------
		JPanel panelTabla = new JPanel();
		panelTabla.setBackground(Tema.FONDO_TARJETA);
		panelTabla.setBorder(new CompoundBorder(new LineBorder(Tema.BORDE_SUAVE, 1, true), new EmptyBorder(2, 2, 2, 2)));
		panelTabla.setBounds(10, 275, 1046, 330);
		getContentPane().add(panelTabla);
		panelTabla.setLayout(null);

		JScrollPane scrollDetalle = new JScrollPane();
		scrollDetalle.setBorder(null);
		scrollDetalle.setBounds(2, 2, 1042, 326);
		panelTabla.add(scrollDetalle);

		tablaDetalle = new JTable();
		tablaDetalle.setRowHeight(26);
		tablaDetalle.setFont(Tema.FUENTE_CAMPO);
		tablaDetalle.setSelectionBackground(Tema.NARANJA);
		tablaDetalle.setSelectionForeground(Color.WHITE);
		tablaDetalle.setGridColor(new Color(232, 232, 230));
		tablaDetalle.getTableHeader().setFont(Tema.FUENTE_BOTON);
		tablaDetalle.getTableHeader().setBackground(Tema.ACERO);
		tablaDetalle.getTableHeader().setForeground(Color.WHITE);
		tablaDetalle.getTableHeader().setOpaque(true);
		scrollDetalle.setViewportView(tablaDetalle);

		JLabel lblAyuda = new JLabel("Seleccioná un ítem y presioná Supr para quitarlo del detalle.");
		lblAyuda.setFont(Tema.FUENTE_CAMPO);
		lblAyuda.setForeground(Tema.TEXTO_SECUNDARIO);
		lblAyuda.setBounds(10, 609, 500, 18);
		getContentPane().add(lblAyuda);

		// ---------- Botonera inferior + total ----------
		btnCancelar = new JButtonABM();
		btnCancelar.setText("Cancelar");
		btnCancelar.setToolTipText("Cancelar la venta en curso");
		btnCancelar.setBounds(10, 633, 97, 76);
		getContentPane().add(btnCancelar);

		btnGuardar = new JButtonABM();
		btnGuardar.setText("Guardar");
		btnGuardar.setToolTipText("Registrar la venta");
		btnGuardar.setBounds(151, 633, 97, 76);
		getContentPane().add(btnGuardar);

		lblTotal = new JLabel("Total ₲: 0");
		lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 30));
		lblTotal.setForeground(Tema.TEXTO_PRINCIPAL);
		lblTotal.setBounds(600, 633, 456, 76);
		getContentPane().add(lblTotal);

	}

	/**
	 * Botón chico auxiliar (Buscar / Registrar Cliente): no forma parte de
	 * la botonera ABM (JButtonABM busca un ícono de 32px según el texto),
	 * así que se estiliza a mano con la misma paleta del tema, igual que en
	 * ControlStockVista.
	 */
	private JButton crearBotonAuxiliar(String texto) {
		JButton boton = new JButton(texto) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getModel().isPressed() ? Tema.NARANJA_OSCURO
						: getModel().isRollover() ? Tema.NARANJA : Tema.ACERO);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
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
		boton.setFocusable(true);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return boton;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JtextFieldGenerico getTfCodigo() {
		return tfCodigo;
	}

	public JFormattedTextField getTfFecha() {
		return tfFecha;
	}

	public JtextFieldGenerico getTfCiRuc() {
		return tfCiRuc;
	}

	public JtextFieldGenerico getTfRazonSocial() {
		return tfRazonSocial;
	}

	public JtextFieldGenerico getTfContacto() {
		return tfContacto;
	}

	public JComboBox<FuncionarioModelo> getCbFuncionario() {
		return cbFuncionario;
	}

	public JComboBox<modelo.FormaPago> getCbFormaPago() {
		return cbFormaPago;
	}

	public JButton getBtnRegistrarCliente() {
		return btnRegistrarCliente;
	}

	public JtextFieldGenerico getTfCodigoProducto() {
		return tfCodigoProducto;
	}

	public JtextFieldGenerico getTfProducto() {
		return tfProducto;
	}

	public JButton getBtnBuscarProducto() {
		return btnBuscarProducto;
	}

	public JtextFieldGenerico getTfCantidad() {
		return tfCantidad;
	}

	public JtextFieldGenerico getTfPrecioVenta() {
		return tfPrecioVenta;
	}

	public JTable getTablaDetalle() {
		return tablaDetalle;
	}

	public JButtonABM getBtnCancelar() {
		return btnCancelar;
	}

	public JButtonABM getBtnGuardar() {
		return btnGuardar;
	}

	public JLabel getLblTotal() {
		return lblTotal;
	}

}
