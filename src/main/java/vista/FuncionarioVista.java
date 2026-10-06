package vista;

import java.awt.EventQueue;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;

import componentes.JDialogGenerico;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import controlador.FuncionarioController;
import util.FechaUtil;

public class FuncionarioVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;
	private JFormattedTextField tfFechaIngreso;
	private JtextFieldGenerico tfNombre;
	private JtextFieldGenerico tfApellido;
	private JtextFieldGenerico tfDocumento;
	private JtextFieldGenerico tfCargo;
	private JtextFieldGenerico tfTelefono;
	private JtextFieldGenerico tfCorreo;
	private JtextFieldGenerico tfDireccion;
	private JCheckBox cbEstado;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FuncionarioVista dialog = new FuncionarioVista();
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
		new FuncionarioController(this);
	}

	/**
	 * Create the dialog.
	 */
	public FuncionarioVista() {
		setTituloFormulario("Gestión de Funcionarios");

		JLabelGenerico lblgnrcFechaIngreso = new JLabelGenerico((String) null);
		lblgnrcFechaIngreso.setText("Fecha Ingreso:");
		lblgnrcFechaIngreso.setBounds(10, 28, 142, 25);
		getPanelFormulario().add(lblgnrcFechaIngreso);

		JLabelGenerico lblgnrcNombre = new JLabelGenerico((String) null);
		lblgnrcNombre.setText("Nombre:");
		lblgnrcNombre.setBounds(10, 74, 142, 25);
		getPanelFormulario().add(lblgnrcNombre);

		JLabelGenerico lblgnrcApellido = new JLabelGenerico((String) null);
		lblgnrcApellido.setText("Apellido:");
		lblgnrcApellido.setBounds(10, 124, 142, 25);
		getPanelFormulario().add(lblgnrcApellido);

		JLabelGenerico lblgnrcDocumento = new JLabelGenerico((String) null);
		lblgnrcDocumento.setText("Documento:");
		lblgnrcDocumento.setBounds(10, 178, 142, 25);
		getPanelFormulario().add(lblgnrcDocumento);

		JLabelGenerico lblgnrcCargo = new JLabelGenerico((String) null);
		lblgnrcCargo.setText("Cargo:");
		lblgnrcCargo.setBounds(10, 227, 142, 25);
		getPanelFormulario().add(lblgnrcCargo);

		JLabelGenerico lblgnrcTelefono = new JLabelGenerico((String) null);
		lblgnrcTelefono.setText("Telefono:");
		lblgnrcTelefono.setBounds(10, 285, 142, 25);
		getPanelFormulario().add(lblgnrcTelefono);

		JLabelGenerico lblgnrcCorreo = new JLabelGenerico((String) null);
		lblgnrcCorreo.setText("Correo:");
		lblgnrcCorreo.setBounds(10, 340, 142, 25);
		getPanelFormulario().add(lblgnrcCorreo);

		JLabelGenerico lblgnrcDireccion = new JLabelGenerico((String) null);
		lblgnrcDireccion.setText("Dirección:");
		lblgnrcDireccion.setBounds(10, 391, 142, 25);
		getPanelFormulario().add(lblgnrcDireccion);

		JLabelGenerico lblgnrcEstado = new JLabelGenerico((String) null);
		lblgnrcEstado.setText("Estado:");
		lblgnrcEstado.setBounds(10, 440, 142, 25);
		getPanelFormulario().add(lblgnrcEstado);

		tfFechaIngreso = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFechaIngreso.setBounds(162, 28, 100, 25);
		getPanelFormulario().add(tfFechaIngreso);

		tfNombre = new JtextFieldGenerico();
		tfNombre.setBounds(162, 74, 343, 25);
		getPanelFormulario().add(tfNombre);

		tfApellido = new JtextFieldGenerico();
		tfApellido.setBounds(162, 124, 343, 25);
		getPanelFormulario().add(tfApellido);

		tfDocumento = new JtextFieldGenerico();
		tfDocumento.setBounds(162, 178, 172, 25);
		getPanelFormulario().add(tfDocumento);

		tfCargo = new JtextFieldGenerico();
		tfCargo.setBounds(162, 227, 343, 25);
		getPanelFormulario().add(tfCargo);

		tfTelefono = new JtextFieldGenerico();
		tfTelefono.setBounds(162, 285, 172, 25);
		getPanelFormulario().add(tfTelefono);

		tfCorreo = new JtextFieldGenerico();
		tfCorreo.setBounds(162, 340, 343, 25);
		getPanelFormulario().add(tfCorreo);

		tfDireccion = new JtextFieldGenerico();
		tfDireccion.setBounds(162, 391, 343, 25);
		getPanelFormulario().add(tfDireccion);

		cbEstado = new JCheckBox("Activo");
		cbEstado.setBounds(162, 440, 150, 25);
		getPanelFormulario().add(cbEstado);

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JFormattedTextField getTfFechaIngreso() {
		return tfFechaIngreso;
	}

	public JtextFieldGenerico getTfNombre() {
		return tfNombre;
	}

	public JtextFieldGenerico getTfApellido() {
		return tfApellido;
	}

	public JtextFieldGenerico getTfDocumento() {
		return tfDocumento;
	}

	public JtextFieldGenerico getTfCargo() {
		return tfCargo;
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

	public JCheckBox getCbEstado() {
		return cbEstado;
	}

}
