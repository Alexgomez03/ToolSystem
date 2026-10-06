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
import controlador.MovimientoVentasController;
import modelo.FuncionarioModelo;
import util.FechaUtil;


 // Pantalla de "Movimiento de Ventas": historial administrativo de ventas ya
 // registradas (lista + formulario, patrón ABM estándar), pensada para que
 // un Administrador pueda revisar, corregir la cabecera o anular una venta,
 // y generar el informe correspondiente sin salir de la pantalla.
 
 // No confundir con {@link VentaVista}: esa es la pantalla rápida "de
 // mostrador" para cargar una venta nueva (accesible a Vendedor y
 // Administrador desde el botón de acceso directo); esta es exclusiva para
 // Administrador y se accede solo desde Movimiento → Ventas.
 
public class MovimientoVentasVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;

	private JFormattedTextField tfFecha;
	private JtextFieldGenerico tfCiRuc;
	private JtextFieldGenerico tfRazonSocial;
	private JtextFieldGenerico tfContacto;
	private componentes.JButtonABM btnRegistrarCliente;
	private JComboBox<FuncionarioModelo> cbFuncionario;
	private JCheckBox cbAnulada;
	private JComboBox<modelo.FormaPago> cbFormaPago;
	private JtextFieldGenerico tfMontoPagado;
	private JtextFieldGenerico tfCodigoProducto;
	private componentes.JButtonABM btnBuscarProducto;
	private JtextFieldGenerico tfProducto;
	private JtextFieldGenerico tfCantidad;
	private JtextFieldGenerico tfPrecioVenta;
	private componentes.JButtonABM btnAgregarItem;
	private componentes.JButtonABM btnQuitarItem;
	private JTable tablaDetalle;
	private JtextFieldGenerico tfTotal;
	private componentes.JButtonABM btnGenerarInforme;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MovimientoVentasVista dialog = new MovimientoVentasVista();
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
		new MovimientoVentasController(this);
	}

	public MovimientoVentasVista() {
		setTituloFormulario("Movimiento de Ventas");

		JLabelGenerico lblgnrcFecha = new JLabelGenerico((String) null);
		lblgnrcFecha.setText("Fecha:");
		lblgnrcFecha.setBounds(10, 20, 142, 25);
		getPanelFormulario().add(lblgnrcFecha);

		tfFecha = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFecha.setBounds(162, 20, 100, 25);
		getPanelFormulario().add(tfFecha);

		btnGenerarInforme = new componentes.JButtonABM();
		btnGenerarInforme.setText("Generar Informe");
		btnGenerarInforme.setToolTipText("Abrir el informe de Ventas por Período");
		btnGenerarInforme.setBounds(302, 20, 203, 25);
		getPanelFormulario().add(btnGenerarInforme);

		JLabelGenerico lblgnrcCiRuc = new JLabelGenerico((String) null);
		lblgnrcCiRuc.setText("C.I./RUC Cliente:");
		lblgnrcCiRuc.setBounds(10, 55, 142, 25);
		getPanelFormulario().add(lblgnrcCiRuc);

		tfCiRuc = new JtextFieldGenerico();
		tfCiRuc.setBounds(162, 55, 130, 25);
		getPanelFormulario().add(tfCiRuc);

		btnRegistrarCliente = new componentes.JButtonABM();
		btnRegistrarCliente.setText("Registrar Cliente");
		btnRegistrarCliente.setToolTipText("Registrar un cliente nuevo si no se encontró por su documento");
		btnRegistrarCliente.setBounds(302, 55, 203, 25);
		getPanelFormulario().add(btnRegistrarCliente);

		JLabelGenerico lblgnrcRazonSocial = new JLabelGenerico((String) null);
		lblgnrcRazonSocial.setText("Nombre y Apellido:");
		lblgnrcRazonSocial.setBounds(10, 90, 142, 25);
		getPanelFormulario().add(lblgnrcRazonSocial);

		tfRazonSocial = new JtextFieldGenerico();
		tfRazonSocial.setBounds(162, 90, 343, 25);
		tfRazonSocial.setEnabled(false);
		getPanelFormulario().add(tfRazonSocial);

		JLabelGenerico lblgnrcContacto = new JLabelGenerico((String) null);
		lblgnrcContacto.setText("Contacto:");
		lblgnrcContacto.setBounds(10, 125, 142, 25);
		getPanelFormulario().add(lblgnrcContacto);

		tfContacto = new JtextFieldGenerico();
		tfContacto.setBounds(162, 125, 343, 25);
		tfContacto.setEnabled(false);
		getPanelFormulario().add(tfContacto);

		JLabelGenerico lblgnrcVendedor = new JLabelGenerico((String) null);
		lblgnrcVendedor.setText("Vendedor:");
		lblgnrcVendedor.setBounds(10, 160, 142, 25);
		getPanelFormulario().add(lblgnrcVendedor);

		cbFuncionario = new JComboBox<FuncionarioModelo>();
		cbFuncionario.setBounds(162, 160, 343, 25);
		getPanelFormulario().add(cbFuncionario);

		JLabelGenerico lblgnrcAnulada = new JLabelGenerico((String) null);
		lblgnrcAnulada.setText("Anulada:");
		lblgnrcAnulada.setBounds(10, 195, 100, 25);
		getPanelFormulario().add(lblgnrcAnulada);

		cbAnulada = new JCheckBox("Sí");
		cbAnulada.setBounds(162, 195, 75, 25);
		getPanelFormulario().add(cbAnulada);

		// ---------- Forma de pago (contado o crédito) ----------
		JLabelGenerico lblgnrcFormaPago = new JLabelGenerico((String) null);
		lblgnrcFormaPago.setText("Forma de Pago:");
		lblgnrcFormaPago.setBounds(10, 228, 110, 25);
		getPanelFormulario().add(lblgnrcFormaPago);

		cbFormaPago = new JComboBox<modelo.FormaPago>(modelo.FormaPago.values());
		cbFormaPago.setBounds(120, 228, 130, 25);
		getPanelFormulario().add(cbFormaPago);

		JLabelGenerico lblgnrcMontoPagado = new JLabelGenerico((String) null);
		lblgnrcMontoPagado.setText("Monto Pagado:");
		lblgnrcMontoPagado.setBounds(265, 228, 100, 25);
		getPanelFormulario().add(lblgnrcMontoPagado);

		// Este campo solo se puede tocar cuando la forma de pago es
		// "Crédito" (a Contado se considera pagado por completo y no
		// hace falta escribir nada). Es acá donde un Administrador
		// registra un cobro cuando el cliente va pagando su deuda.
		tfMontoPagado = new JtextFieldGenerico();
		tfMontoPagado.setBounds(370, 228, 115, 25);
		getPanelFormulario().add(tfMontoPagado);

		JLabelGenerico lblgnrcDetalle = new JLabelGenerico((String) null);
		lblgnrcDetalle.setText("Detalle de la venta:");
		lblgnrcDetalle.setBounds(10, 258, 200, 20);
		getPanelFormulario().add(lblgnrcDetalle);

		JLabelGenerico lblgnrcCodigoProducto = new JLabelGenerico((String) null);
		lblgnrcCodigoProducto.setText("Código:");
		lblgnrcCodigoProducto.setBounds(10, 280, 90, 20);
		getPanelFormulario().add(lblgnrcCodigoProducto);

		JLabelGenerico lblgnrcProducto = new JLabelGenerico((String) null);
		lblgnrcProducto.setText("Producto:");
		lblgnrcProducto.setBounds(240, 280, 90, 20);
		getPanelFormulario().add(lblgnrcProducto);

		tfCodigoProducto = new JtextFieldGenerico();
		tfCodigoProducto.setBounds(10, 300, 110, 25);
		getPanelFormulario().add(tfCodigoProducto);

		btnBuscarProducto = new componentes.JButtonABM();
		btnBuscarProducto.setText("Buscar");
		btnBuscarProducto.setToolTipText("Buscar producto por código o descripción");
		btnBuscarProducto.setBounds(125, 300, 105, 25);
		getPanelFormulario().add(btnBuscarProducto);

		tfProducto = new JtextFieldGenerico();
		tfProducto.setBounds(240, 300, 265, 25);
		tfProducto.setEnabled(false);
		getPanelFormulario().add(tfProducto);

		JLabelGenerico lblgnrcCantidad = new JLabelGenerico((String) null);
		lblgnrcCantidad.setText("Cantidad:");
		lblgnrcCantidad.setBounds(10, 332, 90, 20);
		getPanelFormulario().add(lblgnrcCantidad);

		JLabelGenerico lblgnrcPrecioVenta = new JLabelGenerico((String) null);
		lblgnrcPrecioVenta.setText("Precio Unit.:");
		lblgnrcPrecioVenta.setBounds(125, 332, 90, 20);
		getPanelFormulario().add(lblgnrcPrecioVenta);

		tfCantidad = new JtextFieldGenerico();
		tfCantidad.setBounds(10, 352, 105, 25);
		getPanelFormulario().add(tfCantidad);

		tfPrecioVenta = new JtextFieldGenerico();
		tfPrecioVenta.setBounds(125, 352, 105, 25);
		getPanelFormulario().add(tfPrecioVenta);

		btnAgregarItem = new componentes.JButtonABM();
		btnAgregarItem.setText("Agregar Ítem");
		btnAgregarItem.setToolTipText("Agregar producto al detalle");
		btnAgregarItem.setBounds(240, 332, 130, 45);
		getPanelFormulario().add(btnAgregarItem);

		btnQuitarItem = new componentes.JButtonABM();
		btnQuitarItem.setText("Quitar Ítem");
		btnQuitarItem.setToolTipText("Quitar producto seleccionado del detalle");
		btnQuitarItem.setBounds(375, 332, 130, 45);
		getPanelFormulario().add(btnQuitarItem);

		JScrollPane scrollDetalle = new JScrollPane();
		scrollDetalle.setBorder(new LineBorder(Tema.BORDE_SUAVE, 1, true));
		scrollDetalle.setBounds(10, 388, 495, 118);
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

		JLabelGenerico lblgnrcAyuda = new JLabelGenerico((String) null);
		lblgnrcAyuda.setText("El detalle de una venta ya guardada no se modifica; solo se puede anular.");
		lblgnrcAyuda.setFont(Tema.FUENTE_CAMPO);
		lblgnrcAyuda.setForeground(Tema.TEXTO_SECUNDARIO);
		lblgnrcAyuda.setBounds(10, 508, 495, 18);
		getPanelFormulario().add(lblgnrcAyuda);

		JLabelGenerico lblgnrcTotal = new JLabelGenerico((String) null);
		lblgnrcTotal.setText("Total:");
		lblgnrcTotal.setBounds(10, 532, 142, 25);
		getPanelFormulario().add(lblgnrcTotal);

		tfTotal = new JtextFieldGenerico();
		tfTotal.setBounds(162, 532, 172, 25);
		tfTotal.setEnabled(false);
		getPanelFormulario().add(tfTotal);

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

	public componentes.JButtonABM getBtnRegistrarCliente() {
		return btnRegistrarCliente;
	}

	public JComboBox<FuncionarioModelo> getCbFuncionario() {
		return cbFuncionario;
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

	public JtextFieldGenerico getTfCodigoProducto() {
		return tfCodigoProducto;
	}

	public componentes.JButtonABM getBtnBuscarProducto() {
		return btnBuscarProducto;
	}

	public JtextFieldGenerico getTfProducto() {
		return tfProducto;
	}

	public JtextFieldGenerico getTfCantidad() {
		return tfCantidad;
	}

	public JtextFieldGenerico getTfPrecioVenta() {
		return tfPrecioVenta;
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

	public JtextFieldGenerico getTfTotal() {
		return tfTotal;
	}

	public componentes.JButtonABM getBtnGenerarInforme() {
		return btnGenerarInforme;
	}

}
