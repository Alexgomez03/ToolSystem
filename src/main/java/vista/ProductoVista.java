package vista;

import java.awt.EventQueue;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import componentes.JDialogGenerico;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import controlador.ProductoController;
import modelo.CategoriaModelo;
import modelo.MarcaModelo;

public class ProductoVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;
	private JtextFieldGenerico tfCodigo;
	private JtextFieldGenerico tfDescripcion;
	private JtextFieldGenerico tfPrecioVenta;
	private JtextFieldGenerico tfStock;
	private JtextFieldGenerico tfStockMinimo;
	private JtextFieldGenerico tfUnidadMedida;
	private JCheckBox cbEstado;
	private JComboBox<CategoriaModelo> cbCategoria;
	private JComboBox<MarcaModelo> cbMarca;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProductoVista dialog = new ProductoVista();
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
		new ProductoController(this);
	}

	/**
	 * Create the dialog.
	 */
	public ProductoVista() {
		setTituloFormulario("Gestión de Productos");

		JLabelGenerico lblgnrcCodigo = new JLabelGenerico((String) null);
		lblgnrcCodigo.setText("Código:");
		lblgnrcCodigo.setBounds(10, 28, 142, 25);
		getPanelFormulario().add(lblgnrcCodigo);

		JLabelGenerico lblgnrcDescripcion = new JLabelGenerico((String) null);
		lblgnrcDescripcion.setText("Descripción:");
		lblgnrcDescripcion.setBounds(10, 74, 142, 25);
		getPanelFormulario().add(lblgnrcDescripcion);

		JLabelGenerico lblgnrcCategoria = new JLabelGenerico((String) null);
		lblgnrcCategoria.setText("Categoría:");
		lblgnrcCategoria.setBounds(10, 124, 142, 25);
		getPanelFormulario().add(lblgnrcCategoria);

		JLabelGenerico lblgnrcMarca = new JLabelGenerico((String) null);
		lblgnrcMarca.setText("Marca:");
		lblgnrcMarca.setBounds(10, 178, 142, 25);
		getPanelFormulario().add(lblgnrcMarca);

		JLabelGenerico lblgnrcPrecioVenta = new JLabelGenerico((String) null);
		lblgnrcPrecioVenta.setText("Precio Venta:");
		lblgnrcPrecioVenta.setBounds(10, 227, 142, 25);
		getPanelFormulario().add(lblgnrcPrecioVenta);

		JLabelGenerico lblgnrcStock = new JLabelGenerico((String) null);
		lblgnrcStock.setText("Stock:");
		lblgnrcStock.setBounds(10, 285, 142, 25);
		getPanelFormulario().add(lblgnrcStock);

		JLabelGenerico lblgnrcStockMinimo = new JLabelGenerico((String) null);
		lblgnrcStockMinimo.setText("Mínimo:");
		lblgnrcStockMinimo.setBounds(344, 285, 70, 25);
		getPanelFormulario().add(lblgnrcStockMinimo);

		JLabelGenerico lblgnrcUnidadMedida = new JLabelGenerico((String) null);
		lblgnrcUnidadMedida.setText("Unidad de Medida:");
		lblgnrcUnidadMedida.setBounds(10, 340, 142, 25);
		getPanelFormulario().add(lblgnrcUnidadMedida);

		JLabelGenerico lblgnrcEstado = new JLabelGenerico((String) null);
		lblgnrcEstado.setText("Estado:");
		lblgnrcEstado.setBounds(10, 391, 142, 25);
		getPanelFormulario().add(lblgnrcEstado);

		tfCodigo = new JtextFieldGenerico();
		tfCodigo.setBounds(162, 28, 172, 25);
		tfCodigo.setToolTipText("Único e inmodificable: no se puede cambiar una vez guardado el producto");
		getPanelFormulario().add(tfCodigo);

		tfDescripcion = new JtextFieldGenerico();
		tfDescripcion.setBounds(162, 74, 343, 25);
		getPanelFormulario().add(tfDescripcion);

		cbCategoria = new JComboBox<CategoriaModelo>();
		cbCategoria.setBounds(162, 124, 343, 25);
		getPanelFormulario().add(cbCategoria);

		cbMarca = new JComboBox<MarcaModelo>();
		cbMarca.setBounds(162, 178, 343, 25);
		getPanelFormulario().add(cbMarca);

		tfPrecioVenta = new JtextFieldGenerico();
		tfPrecioVenta.setBounds(162, 227, 172, 25);
		getPanelFormulario().add(tfPrecioVenta);

		tfStock = new JtextFieldGenerico();
		tfStock.setBounds(162, 285, 172, 25);
		getPanelFormulario().add(tfStock);

		tfStockMinimo = new JtextFieldGenerico();
		tfStockMinimo.setBounds(414, 285, 90, 25);
		getPanelFormulario().add(tfStockMinimo);

		tfUnidadMedida = new JtextFieldGenerico();
		tfUnidadMedida.setBounds(162, 340, 172, 25);
		getPanelFormulario().add(tfUnidadMedida);

		cbEstado = new JCheckBox("Activo");
		cbEstado.setBounds(162, 391, 150, 25);
		getPanelFormulario().add(cbEstado);

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JtextFieldGenerico getTfCodigo() {
		return tfCodigo;
	}

	public JtextFieldGenerico getTfDescripcion() {
		return tfDescripcion;
	}

	public JtextFieldGenerico getTfPrecioVenta() {
		return tfPrecioVenta;
	}

	public JtextFieldGenerico getTfStock() {
		return tfStock;
	}

	public JtextFieldGenerico getTfStockMinimo() {
		return tfStockMinimo;
	}

	public JtextFieldGenerico getTfUnidadMedida() {
		return tfUnidadMedida;
	}

	public JCheckBox getCbEstado() {
		return cbEstado;
	}

	public JComboBox<CategoriaModelo> getCbCategoria() {
		return cbCategoria;
	}

	public JComboBox<MarcaModelo> getCbMarca() {
		return cbMarca;
	}

}
