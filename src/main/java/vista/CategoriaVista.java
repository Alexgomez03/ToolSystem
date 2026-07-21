package vista;

import java.awt.EventQueue;

import javax.swing.JCheckBox;
import javax.swing.JDialog;

import componentes.JDialogGenerico;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import controlador.CategoriaController;

public class CategoriaVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;
	private JtextFieldGenerico tfNombre;
	private JCheckBox cbEstado;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CategoriaVista dialog = new CategoriaVista();
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
		new CategoriaController(this);
	}

	/**
	 * Create the dialog.
	 */
	public CategoriaVista() {
		setTituloFormulario("Gestión de Categorías");

		JLabelGenerico lblgnrcNombre = new JLabelGenerico((String) null);
		lblgnrcNombre.setText("Nombre:");
		lblgnrcNombre.setBounds(10, 74, 142, 25);
		getPanelFormulario().add(lblgnrcNombre);

		JLabelGenerico lblgnrcEstado = new JLabelGenerico((String) null);
		lblgnrcEstado.setText("Estado:");
		lblgnrcEstado.setBounds(10, 124, 142, 25);
		getPanelFormulario().add(lblgnrcEstado);

		tfNombre = new JtextFieldGenerico();
		tfNombre.setBounds(162, 74, 343, 25);
		getPanelFormulario().add(tfNombre);

		cbEstado = new JCheckBox("Activo");
		cbEstado.setBounds(162, 124, 150, 25);
		getPanelFormulario().add(cbEstado);

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JtextFieldGenerico getTfNombre() {
		return tfNombre;
	}

	public JCheckBox getCbEstado() {
		return cbEstado;
	}

}
