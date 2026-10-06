package vista;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import componentes.JDialogGenerico;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import componentes.Tema;
import controlador.CompraController;
import modelo.ProductoModelo;
import modelo.ProveedorModelo;
import util.FechaUtil;

public class CompraVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;
	private JFormattedTextField tfFecha;
	private JComboBox<ProveedorModelo> cbProveedor;
	private JtextFieldGenerico tfNroFactura;
	private JtextFieldGenerico tfObservacion;
	private JtextFieldGenerico tfTotal;
	private JCheckBox cbAnulada;
	private JComboBox<modelo.FormaPago> cbFormaPago;
	private JtextFieldGenerico tfMontoPagado;
	private JComboBox<ProductoModelo> cbProducto;
	private JtextFieldGenerico tfCantidad;
	private JtextFieldGenerico tfPrecioCosto;
	private componentes.JButtonABM btnAgregarItem;
	private componentes.JButtonABM btnQuitarItem;
	private JTable tablaDetalle;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompraVista dialog = new CompraVista();
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
		new CompraController(this);
	}

	/**
	 * Create the dialog.
	 */
	public CompraVista() {
		setTituloFormulario("Gestión de Compras");

		JLabelGenerico lblgnrcFecha = new JLabelGenerico((String) null);
		lblgnrcFecha.setText("Fecha:");
		lblgnrcFecha.setBounds(10, 20, 142, 25);
		getPanelFormulario().add(lblgnrcFecha);

		JLabelGenerico lblgnrcProveedor = new JLabelGenerico((String) null);
		lblgnrcProveedor.setText("Proveedor:");
		lblgnrcProveedor.setBounds(10, 55, 142, 25);
		getPanelFormulario().add(lblgnrcProveedor);

		JLabelGenerico lblgnrcNroFactura = new JLabelGenerico((String) null);
		lblgnrcNroFactura.setText("N° Factura:");
		lblgnrcNroFactura.setBounds(10, 90, 142, 25);
		getPanelFormulario().add(lblgnrcNroFactura);

		JLabelGenerico lblgnrcObservacion = new JLabelGenerico((String) null);
		lblgnrcObservacion.setText("Observación:");
		lblgnrcObservacion.setBounds(295, 90, 100, 25);
		getPanelFormulario().add(lblgnrcObservacion);

		JLabelGenerico lblgnrcAnulada = new JLabelGenerico((String) null);
		lblgnrcAnulada.setText("Anulada:");
		lblgnrcAnulada.setBounds(10, 125, 100, 25);
		getPanelFormulario().add(lblgnrcAnulada);

		// ---------- Forma de pago (contado o crédito) ----------
		JLabelGenerico lblgnrcFormaPago = new JLabelGenerico((String) null);
		lblgnrcFormaPago.setText("Forma de Pago:");
		lblgnrcFormaPago.setBounds(10, 160, 110, 25);
		getPanelFormulario().add(lblgnrcFormaPago);

		cbFormaPago = new JComboBox<modelo.FormaPago>(modelo.FormaPago.values());
		cbFormaPago.setBounds(120, 160, 130, 25);
		getPanelFormulario().add(cbFormaPago);

		JLabelGenerico lblgnrcMontoPagado = new JLabelGenerico((String) null);
		lblgnrcMontoPagado.setText("Monto Pagado:");
		lblgnrcMontoPagado.setBounds(265, 160, 100, 25);
		getPanelFormulario().add(lblgnrcMontoPagado);

		// Este campo solo se puede tocar cuando la forma de pago es
		// "Crédito" (a Contado se considera pagado por completo). Acá
		// es donde se registra un pago al proveedor cuando se le va
		// cancelando la deuda.
		tfMontoPagado = new JtextFieldGenerico();
		tfMontoPagado.setBounds(370, 160, 115, 25);
		getPanelFormulario().add(tfMontoPagado);

		JLabelGenerico lblgnrcDetalle = new JLabelGenerico((String) null);
		lblgnrcDetalle.setText("Detalle de la compra:");
		lblgnrcDetalle.setBounds(10, 190, 200, 20);
		getPanelFormulario().add(lblgnrcDetalle);

		JLabelGenerico lblgnrcProducto = new JLabelGenerico((String) null);
		lblgnrcProducto.setText("Producto:");
		lblgnrcProducto.setBounds(10, 212, 80, 20);
		getPanelFormulario().add(lblgnrcProducto);

		JLabelGenerico lblgnrcCantidad = new JLabelGenerico((String) null);
		lblgnrcCantidad.setText("Cantidad:");
		lblgnrcCantidad.setBounds(230, 212, 65, 20);
		getPanelFormulario().add(lblgnrcCantidad);

		JLabelGenerico lblgnrcPrecioCosto = new JLabelGenerico((String) null);
		lblgnrcPrecioCosto.setText("Costo Unit.:");
		lblgnrcPrecioCosto.setBounds(305, 212, 70, 20);
		getPanelFormulario().add(lblgnrcPrecioCosto);

		tfFecha = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFecha.setBounds(162, 20, 100, 25);
		getPanelFormulario().add(tfFecha);

		cbProveedor = new JComboBox<ProveedorModelo>();
		cbProveedor.setBounds(162, 55, 343, 25);
		getPanelFormulario().add(cbProveedor);

		tfNroFactura = new JtextFieldGenerico();
		tfNroFactura.setBounds(162, 90, 120, 25);
		getPanelFormulario().add(tfNroFactura);

		tfObservacion = new JtextFieldGenerico();
		tfObservacion.setBounds(385, 90, 120, 25);
		getPanelFormulario().add(tfObservacion);

		cbAnulada = new JCheckBox("Sí");
		cbAnulada.setBounds(162, 125, 75, 25);
		getPanelFormulario().add(cbAnulada);

		cbProducto = new JComboBox<ProductoModelo>();
		cbProducto.setBounds(10, 234, 210, 25);
		getPanelFormulario().add(cbProducto);

		tfCantidad = new JtextFieldGenerico();
		tfCantidad.setBounds(230, 234, 65, 25);
		getPanelFormulario().add(tfCantidad);

		tfPrecioCosto = new JtextFieldGenerico();
		tfPrecioCosto.setBounds(305, 234, 70, 25);
		getPanelFormulario().add(tfPrecioCosto);

		btnAgregarItem = new componentes.JButtonABM();
		btnAgregarItem.setBounds(378, 212, 65, 47);
		btnAgregarItem.setText("Agregar Item");
		btnAgregarItem.setToolTipText("Agregar producto al detalle");
		getPanelFormulario().add(btnAgregarItem);

		btnQuitarItem = new componentes.JButtonABM();
		btnQuitarItem.setBounds(448, 212, 65, 47);
		btnQuitarItem.setText("Quitar Item");
		btnQuitarItem.setToolTipText("Quitar producto seleccionado del detalle");
		getPanelFormulario().add(btnQuitarItem);

		JScrollPane scrollDetalle = new JScrollPane();
		scrollDetalle.setBorder(new LineBorder(Tema.BORDE_SUAVE, 1, true));
		scrollDetalle.setBounds(10, 270, 495, 240);
		getPanelFormulario().add(scrollDetalle);

		tablaDetalle = new JTable();
		tablaDetalle.setRowHeight(24);
		tablaDetalle.setFont(Tema.FUENTE_CAMPO);
		tablaDetalle.setSelectionBackground(Tema.NARANJA);
		tablaDetalle.setSelectionForeground(Color.WHITE);
		tablaDetalle.setGridColor(new Color(232, 232, 230));
		tablaDetalle.getTableHeader().setFont(Tema.FUENTE_BOTON);
		tablaDetalle.getTableHeader().setBackground(Tema.ACERO);
		tablaDetalle.getTableHeader().setForeground(Color.WHITE);
		tablaDetalle.getTableHeader().setOpaque(true);
		scrollDetalle.setViewportView(tablaDetalle);

		JLabelGenerico lblgnrcTotal = new JLabelGenerico((String) null);
		lblgnrcTotal.setText("Total:");
		lblgnrcTotal.setBounds(10, 520, 142, 25);
		getPanelFormulario().add(lblgnrcTotal);

		tfTotal = new JtextFieldGenerico();
		tfTotal.setBounds(162, 520, 172, 25);
		tfTotal.setEnabled(false);
		getPanelFormulario().add(tfTotal);

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JFormattedTextField getTfFecha() {
		return tfFecha;
	}

	public JComboBox<ProveedorModelo> getCbProveedor() {
		return cbProveedor;
	}

	public JtextFieldGenerico getTfNroFactura() {
		return tfNroFactura;
	}

	public JtextFieldGenerico getTfObservacion() {
		return tfObservacion;
	}

	public JtextFieldGenerico getTfTotal() {
		return tfTotal;
	}

	public JCheckBox getCbAnulada() {
		return cbAnulada;
	}

	public JComboBox<modelo.FormaPago> getCbFormaPago() {
		return cbFormaPago;
	}

	public JtextFieldGenerico getTfMontoPagado() {
		return tfMontoPagado;
	}

	public JComboBox<ProductoModelo> getCbProducto() {
		return cbProducto;
	}

	public JtextFieldGenerico getTfCantidad() {
		return tfCantidad;
	}

	public JtextFieldGenerico getTfPrecioCosto() {
		return tfPrecioCosto;
	}

	public componentes.JButtonABM getBtnAgregarItem() {
		return btnAgregarItem;
	}

	public componentes.JButtonABM getBtnQuitarItem() {
		return btnQuitarItem;
	}

	public JTable getTablaDetalle() {
		return tablaDetalle;
	}

}
