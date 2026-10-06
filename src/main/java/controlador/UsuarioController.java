package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import dao.FuncionarioDAO;
import dao.UsuarioDAO;
import interfaces.InterfaceABM;
import modelo.FuncionarioModelo;
import modelo.UsuarioModelo;
import tablas.ModeloTablaUsuario;
import util.PasswordUtil;
import util.SesionActual;
import vista.UsuarioVista;

public class UsuarioController implements InterfaceABM {

	private UsuarioVista vista;
	private UsuarioModelo usuario;
	private UsuarioDAO dao;
	private List<UsuarioModelo> usuarios;
	private ModeloTablaUsuario tabla;

	// Esto se ejecuta apenas se abre la pantalla de Usuarios: prepara la
	// tabla de la izquierda, carga el combo de funcionarios, deja todo en
	// blanco y trae la lista completa de usuarios ya cargados.
	public UsuarioController(UsuarioVista usuarioVista) {
		super();
		this.vista = usuarioVista;
		this.vista.setInterfaceABM(this);
		dao = new UsuarioDAO();
		tabla = new ModeloTablaUsuario();
		this.vista.getTabla().setModel(tabla);
		cargarCombos();
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}

	// Llena el combo "Funcionario" con la lista de funcionarios activos,
	// y agrega una opción "(sin vincular)" para los usuarios que no
	// necesitan estar atados a ningún funcionario en particular.
	private void cargarCombos() {
		this.vista.getCbFuncionario().addItem(null);
		for (FuncionarioModelo f : new FuncionarioDAO().buscarActivos())
			this.vista.getCbFuncionario().addItem(f);
		this.vista.getCbFuncionario().setRenderer(new javax.swing.DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value == null)
					setText("(sin vincular)");
				else if (value instanceof FuncionarioModelo)
					setText(((FuncionarioModelo) value).getNombre() + " " + ((FuncionarioModelo) value).getApellido());
				return this;
			}
		});
	}

	// Acá se conectan los botones y la tabla con lo que tienen que hacer
	// cuando alguien hace clic o doble clic.
	private void setAcciones() {
		this.vista.getTabla().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					seleccionarRegistro();
			}
		});

		this.vista.getBtnRestablecerPassword().addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				restablecerPassword();
			}
		});
	}

	// Va a buscar a la base de datos los usuarios que coincidan con lo
	// que se escribió en el buscador, y los muestra en la tabla.
	private void cargarTabla(String filtro) {
		usuarios = dao.buscarPorFiltro(filtro);
		tabla.setLista(usuarios);
	}

	// Deja la pantalla como recién abierta: los campos bloqueados y
	// vacíos, y solo el botón "Nuevo" disponible. Se usa al abrir la
	// pantalla y también después de guardar o cancelar.
	private void estadoInicial() {
		this.vista.getBtnNuevo().setEnabled(true);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(false);

		habilitarCampos(false);

		this.vista.getTfUsuario().setText("");
		this.vista.getTfPassword().setText("");
		this.vista.getTfConfirmarPassword().setText("");
		this.vista.getCbFuncionario().setSelectedIndex(0);
		this.vista.getCbEstado().setSelected(false);
		usuario = null;
	}

	// Prende o apaga (según el true/false que le pasen) todos los campos
	// del formulario de una sola vez, para no repetir la misma línea en
	// varios lugares.
	private void habilitarCampos(boolean habilitar) {
		this.vista.getTfUsuario().setEnabled(habilitar);
		this.vista.getTfPassword().setEnabled(habilitar);
		this.vista.getTfConfirmarPassword().setEnabled(habilitar);
		this.vista.getCbRol().setEnabled(habilitar);
		this.vista.getCbFuncionario().setEnabled(habilitar);
		this.vista.getCbEstado().setEnabled(habilitar);
	}

	// Se ejecuta cuando tocan el botón "Nuevo": habilita los campos para
	// cargar un usuario desde cero, con el rol Vendedor y Activo tildado
	// como valores de arranque (lo más común al dar de alta a alguien).
	@Override
	public void nuevo() {
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(true);

		habilitarCampos(true);
		usuario = new UsuarioModelo();
		this.vista.getCbRol().setSelectedItem(UsuarioModelo.Rol.VENDEDOR);
		this.vista.getCbEstado().setSelected(true);
	}

	// Se ejecuta al hacer doble clic sobre una fila de la tabla: toma ese
	// usuario y vuelca sus datos en el formulario para poder verlo (y,
	// si se toca "Editar", modificarlo).
	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0)
			return;
		usuario = usuarios.get(fila);

		this.vista.getTfUsuario().setText(usuario.getUsuario());
		this.vista.getTfPassword().setText("");
		this.vista.getTfConfirmarPassword().setText("");
		this.vista.getCbRol().setSelectedItem(usuario.getRol());
		this.vista.getCbEstado().setSelected(!Boolean.FALSE.equals(usuario.getEstado()));
		seleccionarFuncionarioEnCombo(usuario.getFuncionario());

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(true);
	}

	// Busca en el combo al funcionario que corresponde y lo deja
	// seleccionado, para que se vea el mismo funcionario que tiene
	// guardado el usuario.
	private void seleccionarFuncionarioEnCombo(FuncionarioModelo funcionario) {
		if (funcionario == null) {
			this.vista.getCbFuncionario().setSelectedIndex(0);
			return;
		}
		for (int i = 0; i < this.vista.getCbFuncionario().getItemCount(); i++) {
			FuncionarioModelo item = this.vista.getCbFuncionario().getItemAt(i);
			if (item != null && item.getId().equals(funcionario.getId())) {
				this.vista.getCbFuncionario().setSelectedIndex(i);
				return;
			}
		}
		// El funcionario vinculado ya no está activo: se agrega igual para
		// no perder la referencia al mostrar el registro.
		this.vista.getCbFuncionario().addItem(funcionario);
		this.vista.getCbFuncionario().setSelectedItem(funcionario);
	}

	// Le muestra al administrador un cuadrito para escribir una contraseña
	// nueva para el usuario que tiene seleccionado en la lista, sin tener
	// que abrir "Editar" (que también deja tocar el rol y el funcionario).
	private void restablecerPassword() {
		if (usuario == null || usuario.getId() == null) {
			JOptionPane.showMessageDialog(null, "Primero seleccioná un usuario de la lista.");
			return;
		}

		JPasswordField campoNueva = new JPasswordField();
		JPasswordField campoConfirmar = new JPasswordField();
		Object[] contenido = { "Nueva contraseña para \"" + usuario.getUsuario() + "\":", campoNueva,
				"Confirmar contraseña:", campoConfirmar };

		int opcion = JOptionPane.showConfirmDialog(null, contenido, "Restablecer Contraseña",
				JOptionPane.OK_CANCEL_OPTION);
		if (opcion != JOptionPane.OK_OPTION)
			return;

		String nueva = new String(campoNueva.getPassword());
		String confirmar = new String(campoConfirmar.getPassword());

		if (nueva.length() < 4) {
			JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 4 caracteres");
			return;
		}
		if (!nueva.equals(confirmar)) {
			JOptionPane.showMessageDialog(null, "La contraseña y su confirmación no coinciden");
			return;
		}

		String salNueva = PasswordUtil.generarSalt();
		usuario.setSalt(salNueva);
		usuario.setPasswordHash(PasswordUtil.hash(nueva, salNueva));

		try {
			dao.guardar(usuario);
			JOptionPane.showMessageDialog(null, "Contraseña actualizada correctamente.");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se pudo actualizar la contraseña.");
		}
	}

	// Se ejecuta al tocar "Editar": habilita el formulario para poder
	// cambiar los datos del usuario que está seleccionado (menos el
	// nombre de usuario, que queda fijo).
	@Override
	public void editar() {
		habilitarCampos(true);
		// El nombre de usuario de una cuenta ya creada no se permite
		// renombrar desde acá para no romper el vínculo con ventas ya
		// registradas por ese usuario (no se guarda una FK a Usuario en
		// Venta hoy, pero es una salvaguarda simple para el futuro).
		this.vista.getTfUsuario().setEnabled(false);

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(false);
	}

	// Revisa que los datos cargados tengan sentido (usuario no vacío,
	// contraseña completa si es un usuario nuevo, que coincidan las dos
	// contraseñas escritas, que no exista ya ese nombre de usuario) y, si
	// todo está bien, lo guarda en la base de datos.
	@Override
	public void guardar() {
		String nombreUsuario = this.vista.getTfUsuario().getText().trim();
		String password = new String(this.vista.getTfPassword().getPassword());
		String confirmar = new String(this.vista.getTfConfirmarPassword().getPassword());

		if (nombreUsuario.isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre de usuario es un campo obligatorio");
			return;
		}
		if (usuario.getId() == null && password.isEmpty()) {
			JOptionPane.showMessageDialog(null, "La contraseña es obligatoria para un usuario nuevo");
			return;
		}
		if (!password.equals(confirmar)) {
			JOptionPane.showMessageDialog(null, "La contraseña y su confirmación no coinciden");
			return;
		}
		if (!password.isEmpty() && password.length() < 4) {
			JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 4 caracteres");
			return;
		}
		if (dao.existeUsuario(nombreUsuario, usuario.getId())) {
			JOptionPane.showMessageDialog(null, "Ya existe un usuario con ese nombre");
			return;
		}

		usuario.setUsuario(nombreUsuario);
		if (!password.isEmpty()) {
			String salt = PasswordUtil.generarSalt();
			usuario.setSalt(salt);
			usuario.setPasswordHash(PasswordUtil.hash(password, salt));
		}
		usuario.setRol((UsuarioModelo.Rol) this.vista.getCbRol().getSelectedItem());
		usuario.setFuncionario((FuncionarioModelo) this.vista.getCbFuncionario().getSelectedItem());
		usuario.setEstado(this.vista.getCbEstado().isSelected());

		try {
			dao.guardar(usuario);
			cargarTabla("");
			estadoInicial();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se pudo guardar el usuario.");
		}
	}

	// Borra definitivamente al usuario seleccionado, después de pedir
	// confirmación. No deja borrar el usuario con el que se inició sesión
	// (para no quedarse afuera del sistema sin querer).
	@Override
	public void eliminar() {
		if (usuario == null)
			return;
		if (usuario.getId().equals(SesionActual.getUsuario().getId())) {
			JOptionPane.showMessageDialog(null, "No podés eliminar el usuario con el que iniciaste sesión.");
			return;
		}
		int confirmacion = JOptionPane.showConfirmDialog(null,
				"¿Estás seguro que deseas eliminar al usuario " + usuario.getUsuario() + "?", "Atención",
				JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(usuario);
				estadoInicial();
				cargarTabla("");
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"No se pudo eliminar el usuario. Desmarque \"Activo\" y guarde para deshabilitarlo en vez de eliminarlo.");
			}
		}
	}

	// Se ejecuta al tocar "Cancelar": si no había nada seleccionado
	// cierra la ventana, y si había algo cargado en el formulario, lo
	// descarta y vuelve a dejar todo en blanco.
	@Override
	public void cancelar() {
		if (usuario == null)
			this.vista.dispose();
		else
			estadoInicial();
	}

	// Se ejecuta cada vez que se escribe algo en el buscador: vuelve a
	// cargar la tabla filtrada con ese texto.
	@Override
	public void buscar() {
		cargarTabla(vista.getTfBuscador().getText());
	}

}
