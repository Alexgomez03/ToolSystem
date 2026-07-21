package vista;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFormattedTextField;

import componentes.JDialogGenerico;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import controlador.ProveedorController;
import util.FechaUtil;

public class ProveedorVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;
	private JFormattedTextField tfFechaRegistro;
	private JtextFieldGenerico tfRazonSocial;
	private JtextFieldGenerico tfNombreFantasia;
	private JtextFieldGenerico tfRuc;
	private JtextFieldGenerico tfTelefono;
	private JtextFieldGenerico tfCorreo;
	private JtextFieldGenerico tfDireccion;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProveedorVista dialog = new ProveedorVista();
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
		new ProveedorController(this);
	}

	/**
	 * Create the dialog.
	 */
	public ProveedorVista() {
		setTituloFormulario("Gestión de Proveedores");

		JLabelGenerico lblgnrcFechaRegistro = new JLabelGenerico((String) null);
		lblgnrcFechaRegistro.setText("Fecha Registro:");
		lblgnrcFechaRegistro.setBounds(10, 28, 142, 25);
		getPanelFormulario().add(lblgnrcFechaRegistro);

		JLabelGenerico lblgnrcRazonSocial = new JLabelGenerico((String) null);
		lblgnrcRazonSocial.setText("Razón Social:");
		lblgnrcRazonSocial.setBounds(10, 74, 142, 25);
		getPanelFormulario().add(lblgnrcRazonSocial);

		JLabelGenerico lblgnrcNombreFantasia = new JLabelGenerico((String) null);
		lblgnrcNombreFantasia.setText("Nombre Fantasía:");
		lblgnrcNombreFantasia.setBounds(10, 124, 142, 25);
		getPanelFormulario().add(lblgnrcNombreFantasia);

		JLabelGenerico lblgnrcRuc = new JLabelGenerico((String) null);
		lblgnrcRuc.setText("RUC:");
		lblgnrcRuc.setBounds(10, 178, 142, 25);
		getPanelFormulario().add(lblgnrcRuc);

		JLabelGenerico lblgnrcTelefono = new JLabelGenerico((String) null);
		lblgnrcTelefono.setText("Telefono:");
		lblgnrcTelefono.setBounds(10, 227, 142, 25);
		getPanelFormulario().add(lblgnrcTelefono);

		JLabelGenerico lblgnrcCorreo = new JLabelGenerico((String) null);
		lblgnrcCorreo.setText("Correo:");
		lblgnrcCorreo.setBounds(10, 285, 142, 25);
		getPanelFormulario().add(lblgnrcCorreo);

		JLabelGenerico lblgnrcDireccion = new JLabelGenerico((String) null);
		lblgnrcDireccion.setText("Dirección:");
		lblgnrcDireccion.setBounds(10, 340, 142, 25);
		getPanelFormulario().add(lblgnrcDireccion);

		tfFechaRegistro = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFechaRegistro.setBounds(162, 28, 100, 25);
		getPanelFormulario().add(tfFechaRegistro);

		tfRazonSocial = new JtextFieldGenerico();
		tfRazonSocial.setBounds(162, 74, 343, 25);
		getPanelFormulario().add(tfRazonSocial);

		tfNombreFantasia = new JtextFieldGenerico();
		tfNombreFantasia.setBounds(162, 124, 343, 25);
		getPanelFormulario().add(tfNombreFantasia);

		tfRuc = new JtextFieldGenerico();
		tfRuc.setBounds(162, 178, 172, 25);
		getPanelFormulario().add(tfRuc);

		tfTelefono = new JtextFieldGenerico();
		tfTelefono.setBounds(162, 227, 172, 25);
		getPanelFormulario().add(tfTelefono);

		tfCorreo = new JtextFieldGenerico();
		tfCorreo.setBounds(162, 285, 343, 25);
		getPanelFormulario().add(tfCorreo);

		tfDireccion = new JtextFieldGenerico();
		tfDireccion.setBounds(162, 340, 343, 25);
		getPanelFormulario().add(tfDireccion);

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JFormattedTextField getTfFechaRegistro() {
		return tfFechaRegistro;
	}

	public JtextFieldGenerico getTfRazonSocial() {
		return tfRazonSocial;
	}

	public JtextFieldGenerico getTfNombreFantasia() {
		return tfNombreFantasia;
	}

	public JtextFieldGenerico getTfRuc() {
		return tfRuc;
	}

	public JtextFieldGenerico getTfTelefono() {
		return tfTelefono;
	}

	public JtextFieldGenerico getTfCorreo() {
		return tfCorreo;
	}

	public JtextFieldGenerico getTfDireccion() {
		return tfDireccion;
	}

}
