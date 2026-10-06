package vista;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.DefaultListCellRenderer;

import componentes.JDialogGenerico;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import componentes.Tema;
import controlador.UsuarioController;
import modelo.FuncionarioModelo;
import modelo.UsuarioModelo;

public class UsuarioVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;

	private JtextFieldGenerico tfUsuario;
	private JPasswordField tfPassword;
	private JPasswordField tfConfirmarPassword;
	private JComboBox<UsuarioModelo.Rol> cbRol;
	private JComboBox<FuncionarioModelo> cbFuncionario;
	private JCheckBox cbEstado;
	private componentes.JButtonABM btnRestablecerPassword;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UsuarioVista dialog = new UsuarioVista();
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
		new UsuarioController(this);
	}

	public UsuarioVista() {
		setTituloFormulario("Gestión de Usuarios");

		JLabelGenerico lblUsuario = new JLabelGenerico((String) null);
		lblUsuario.setText("Usuario:");
		lblUsuario.setBounds(10, 28, 142, 25);
		getPanelFormulario().add(lblUsuario);

		tfUsuario = new JtextFieldGenerico();
		tfUsuario.setBounds(162, 28, 250, 25);
		getPanelFormulario().add(tfUsuario);

		JLabelGenerico lblPassword = new JLabelGenerico((String) null);
		lblPassword.setText("Contraseña:");
		lblPassword.setBounds(10, 80, 142, 25);
		getPanelFormulario().add(lblPassword);

		tfPassword = new JPasswordField();
		tfPassword.setFont(Tema.FUENTE_CAMPO);
		tfPassword.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory.createLineBorder(Tema.BORDE_SUAVE, 1, true),
				javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)));
		tfPassword.setBounds(162, 80, 250, 25);
		getPanelFormulario().add(tfPassword);

		JLabelGenerico lblAyudaPassword = new JLabelGenerico((String) null);
		lblAyudaPassword.setText("(vacío = no cambiar)");
		lblAyudaPassword.setFont(Tema.FUENTE_CAMPO);
		lblAyudaPassword.setForeground(Tema.TEXTO_SECUNDARIO);
		lblAyudaPassword.setBounds(162, 106, 250, 20);
		getPanelFormulario().add(lblAyudaPassword);

		JLabelGenerico lblConfirmar = new JLabelGenerico((String) null);
		lblConfirmar.setText("Confirmar Contraseña:");
		lblConfirmar.setBounds(10, 140, 142, 25);
		getPanelFormulario().add(lblConfirmar);

		tfConfirmarPassword = new JPasswordField();
		tfConfirmarPassword.setFont(Tema.FUENTE_CAMPO);
		tfConfirmarPassword.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory.createLineBorder(Tema.BORDE_SUAVE, 1, true),
				javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)));
		tfConfirmarPassword.setBounds(162, 140, 250, 25);
		getPanelFormulario().add(tfConfirmarPassword);

		JLabelGenerico lblRol = new JLabelGenerico((String) null);
		lblRol.setText("Rol:");
		lblRol.setBounds(10, 192, 142, 25);
		getPanelFormulario().add(lblRol);

		cbRol = new JComboBox<UsuarioModelo.Rol>(UsuarioModelo.Rol.values());
		cbRol.setFont(Tema.FUENTE_CAMPO);
		cbRol.setBounds(162, 192, 250, 25);
		cbRol.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value == UsuarioModelo.Rol.ADMINISTRADOR)
					setText("Administrador");
				else if (value == UsuarioModelo.Rol.VENDEDOR)
					setText("Vendedor");
				return this;
			}
		});
		getPanelFormulario().add(cbRol);

		JLabelGenerico lblFuncionario = new JLabelGenerico((String) null);
		lblFuncionario.setText("Funcionario:");
		lblFuncionario.setBounds(10, 244, 142, 25);
		getPanelFormulario().add(lblFuncionario);

		cbFuncionario = new JComboBox<FuncionarioModelo>();
		cbFuncionario.setFont(Tema.FUENTE_CAMPO);
		cbFuncionario.setBounds(162, 244, 343, 25);
		getPanelFormulario().add(cbFuncionario);

		JLabelGenerico lblEstado = new JLabelGenerico((String) null);
		lblEstado.setText("Estado:");
		lblEstado.setBounds(10, 296, 142, 25);
		getPanelFormulario().add(lblEstado);

		cbEstado = new JCheckBox("Activo");
		cbEstado.setBounds(162, 296, 150, 25);
		getPanelFormulario().add(cbEstado);

		// Este botón es para cuando alguien ya tiene su usuario creado pero
		// se olvidó la contraseña: en vez de tener que tocar "Editar" (que
		// también habilita el rol y el funcionario, con el riesgo de
		// cambiar algo sin querer), este botón deja tocar SOLO la
		// contraseña de la persona que está seleccionada en la lista.
		btnRestablecerPassword = new componentes.JButtonABM();
		btnRestablecerPassword.setText("Restablecer Contraseña");
		btnRestablecerPassword.setToolTipText("Cambiar solo la contraseña del usuario seleccionado");
		btnRestablecerPassword.setBounds(162, 340, 250, 30);
		getPanelFormulario().add(btnRestablecerPassword);
	}

	public JtextFieldGenerico getTfUsuario() {
		return tfUsuario;
	}

	public JPasswordField getTfPassword() {
		return tfPassword;
	}

	public JPasswordField getTfConfirmarPassword() {
		return tfConfirmarPassword;
	}

	public JComboBox<UsuarioModelo.Rol> getCbRol() {
		return cbRol;
	}

	public JComboBox<FuncionarioModelo> getCbFuncionario() {
		return cbFuncionario;
	}

	public JCheckBox getCbEstado() {
		return cbEstado;
	}

	public componentes.JButtonABM getBtnRestablecerPassword() {
		return btnRestablecerPassword;
	}

}
