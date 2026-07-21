package vista;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import componentes.JDialogGenerico;
import controlador.ClienteController;
import util.FechaUtil;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;

public class ClienteVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;
	private JFormattedTextField tfFechaRegistro;
	private JtextFieldGenerico tfNombre;
	private JtextFieldGenerico tfApellido;
	private JtextFieldGenerico tfDocumento;
	private JFormattedTextField tfFechaNacimiento;
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
					ClienteVista dialog = new ClienteVista();
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
		new ClienteController(this);
	}

	/**
	 * Create the dialog.
	 */
	public ClienteVista() {
		setTituloFormulario("Gestión de Clientes");
		
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
		
		JLabelGenerico lblgnrcTelefono = new JLabelGenerico((String) null);
		lblgnrcTelefono.setText("Telefono:");
		lblgnrcTelefono.setBounds(10, 285, 142, 25);
		getPanelFormulario().add(lblgnrcTelefono);
		
		JLabelGenerico lblgnrcCorreo = new JLabelGenerico((String) null);
		lblgnrcCorreo.setText("Correo:");
		lblgnrcCorreo.setBounds(10, 340, 142, 25);
		getPanelFormulario().add(lblgnrcCorreo);
		
		JLabelGenerico lblgnrcDireccin = new JLabelGenerico((String) null);
		lblgnrcDireccin.setText("Dirección:");
		lblgnrcDireccin.setBounds(10, 391, 142, 25);
		getPanelFormulario().add(lblgnrcDireccin);
		
		JLabelGenerico lblgnrcFechaNacimiento = new JLabelGenerico((String) null);
		lblgnrcFechaNacimiento.setText("Fecha Nacimiento:");
		lblgnrcFechaNacimiento.setBounds(10, 227, 142, 25);
		getPanelFormulario().add(lblgnrcFechaNacimiento);
		
		JLabelGenerico lblgnrcFechaRegistro = new JLabelGenerico((String) null);
		lblgnrcFechaRegistro.setText("Fecha Registro:");
		lblgnrcFechaRegistro.setBounds(10, 28, 142, 25);
		getPanelFormulario().add(lblgnrcFechaRegistro);
		
		tfFechaRegistro = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFechaRegistro.setBounds(162, 28, 100, 25);
		getPanelFormulario().add(tfFechaRegistro);
		
		tfNombre = new JtextFieldGenerico();
		tfNombre.setBounds(162, 74, 343, 25);
		getPanelFormulario().add(tfNombre);
		
		tfApellido = new JtextFieldGenerico();
		tfApellido.setBounds(162, 124, 343, 25);
		getPanelFormulario().add(tfApellido);
		
		tfDocumento = new JtextFieldGenerico();
		tfDocumento.setBounds(162, 178, 172, 25);
		getPanelFormulario().add(tfDocumento);
		
		tfFechaNacimiento = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFechaNacimiento.setBounds(162, 227, 100, 25);
		getPanelFormulario().add(tfFechaNacimiento);
		
		tfTelefono = new JtextFieldGenerico();
		tfTelefono.setBounds(162, 285, 172, 25);
		getPanelFormulario().add(tfTelefono);
		
		tfCorreo = new JtextFieldGenerico();
		tfCorreo.setBounds(162, 340, 343, 25);
		getPanelFormulario().add(tfCorreo);
		
		tfDireccion = new JtextFieldGenerico();
		tfDireccion.setBounds(162, 391, 343, 25);
		getPanelFormulario().add(tfDireccion);
		

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JFormattedTextField getTfFechaRegistro() {
		return tfFechaRegistro;
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

	public JFormattedTextField getTfFechaNacimiento() {
		return tfFechaNacimiento;
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
