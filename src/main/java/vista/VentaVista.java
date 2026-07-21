package vista;

import java.awt.EventQueue;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import componentes.JDialogGenerico;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import controlador.VentaController;
import modelo.ClienteModelo;
import modelo.ProductoModelo;
import util.FechaUtil;

public class VentaVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;
	private JFormattedTextField tfFecha;
	private JComboBox<ClienteModelo> cbCliente;
	private JtextFieldGenerico tfObservacion;
	private JtextFieldGenerico tfTotal;
	private JCheckBox cbAnulada;
	private JComboBox<ProductoModelo> cbProducto;
	private JtextFieldGenerico tfCantidad;
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
		setTituloFormulario("Gestión de Ventas");

		JLabelGenerico lblgnrcFecha = new JLabelGenerico((String) null);
		lblgnrcFecha.setText("Fecha:");
		lblgnrcFecha.setBounds(10, 20, 142, 25);
		getPanelFormulario().add(lblgnrcFecha);

		JLabelGenerico lblgnrcCliente = new JLabelGenerico((String) null);
		lblgnrcCliente.setText("Cliente:");
		lblgnrcCliente.setBounds(10, 55, 142, 25);
		getPanelFormulario().add(lblgnrcCliente);

		JLabelGenerico lblgnrcObservacion = new JLabelGenerico((String) null);
		lblgnrcObservacion.setText("Observación:");
		lblgnrcObservacion.setBounds(10, 90, 142, 25);
		getPanelFormulario().add(lblgnrcObservacion);

		JLabelGenerico lblgnrcAnulada = new JLabelGenerico((String) null);
		lblgnrcAnulada.setText("Anulada:");
		lblgnrcAnulada.setBounds(360, 90, 100, 25);
		getPanelFormulario().add(lblgnrcAnulada);

		JLabelGenerico lblgnrcDetalle = new JLabelGenerico((String) null);
		lblgnrcDetalle.setText("Detalle de la venta:");
		lblgnrcDetalle.setBounds(10, 128, 200, 20);
		getPanelFormulario().add(lblgnrcDetalle);

		JLabelGenerico lblgnrcProducto = new JLabelGenerico((String) null);
		lblgnrcProducto.setText("Producto:");
		lblgnrcProducto.setBounds(10, 150, 80, 20);
		getPanelFormulario().add(lblgnrcProducto);

		JLabelGenerico lblgnrcCantidad = new JLabelGenerico((String) null);
		lblgnrcCantidad.setText("Cantidad:");
		lblgnrcCantidad.setBounds(295, 150, 80, 20);
		getPanelFormulario().add(lblgnrcCantidad);

		tfFecha = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFecha.setBounds(162, 20, 100, 25);
		getPanelFormulario().add(tfFecha);

		cbCliente = new JComboBox<ClienteModelo>();
		cbCliente.setBounds(162, 55, 343, 25);
		getPanelFormulario().add(cbCliente);

		tfObservacion = new JtextFieldGenerico();
		tfObservacion.setBounds(162, 90, 190, 25);
		getPanelFormulario().add(tfObservacion);

		cbAnulada = new JCheckBox("Sí");
		cbAnulada.setBounds(430, 90, 75, 25);
		getPanelFormulario().add(cbAnulada);

		cbProducto = new JComboBox<ProductoModelo>();
		cbProducto.setBounds(10, 172, 270, 25);
		getPanelFormulario().add(cbProducto);

		tfCantidad = new JtextFieldGenerico();
		tfCantidad.setBounds(295, 172, 70, 25);
		getPanelFormulario().add(tfCantidad);

		btnAgregarItem = new componentes.JButtonABM();
		btnAgregarItem.setBounds(375, 150, 65, 47);
		btnAgregarItem.setText("Agregar Item");
		btnAgregarItem.setToolTipText("Agregar producto al detalle");
		getPanelFormulario().add(btnAgregarItem);

		btnQuitarItem = new componentes.JButtonABM();
		btnQuitarItem.setBounds(445, 150, 65, 47);
		btnQuitarItem.setText("Quitar Item");
		btnQuitarItem.setToolTipText("Quitar producto seleccionado del detalle");
		getPanelFormulario().add(btnQuitarItem);

		JScrollPane scrollDetalle = new JScrollPane();
		scrollDetalle.setBorder(new javax.swing.border.LineBorder(componentes.Tema.BORDE_SUAVE, 1, true));
		scrollDetalle.setBounds(10, 210, 495, 300);
		getPanelFormulario().add(scrollDetalle);

		tablaDetalle = new JTable();
		tablaDetalle.setRowHeight(24);
		tablaDetalle.setFont(componentes.Tema.FUENTE_CAMPO);
		tablaDetalle.setSelectionBackground(componentes.Tema.NARANJA);
		tablaDetalle.setSelectionForeground(java.awt.Color.WHITE);
		tablaDetalle.setGridColor(new java.awt.Color(232, 232, 230));
		tablaDetalle.getTableHeader().setFont(componentes.Tema.FUENTE_BOTON);
		tablaDetalle.getTableHeader().setBackground(componentes.Tema.ACERO);
		tablaDetalle.getTableHeader().setForeground(java.awt.Color.WHITE);
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

	public JComboBox<ClienteModelo> getCbCliente() {
		return cbCliente;
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

	public JComboBox<ProductoModelo> getCbProducto() {
		return cbProducto;
	}

	public JtextFieldGenerico getTfCantidad() {
		return tfCantidad;
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
