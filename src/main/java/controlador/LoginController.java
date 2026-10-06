package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dao.UsuarioDAO;
import modelo.UsuarioModelo;
import util.PasswordUtil;
import util.SesionActual;
import vista.LoginVista;
import vista.PantallaPrincipalVista;

public class LoginController {

	private LoginVista vista;
	private UsuarioDAO dao = new UsuarioDAO();

	public LoginController(LoginVista vista) {
		this.vista = vista;
		this.vista.getBtnIngresar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intentarIngresar();
			}
		});
	}

	private void intentarIngresar() {
		String usuario = vista.getTfUsuario().getText().trim();
		String password = new String(vista.getTfPassword().getPassword());

		if (usuario.isEmpty() || password.isEmpty()) {
			vista.getLblMensaje().setText("Ingresá tu usuario y contraseña");
			return;
		}

		UsuarioModelo encontrado;
		try {
			encontrado = dao.buscarPorUsuario(usuario);
		} catch (Exception e) {
			e.printStackTrace();
			vista.getLblMensaje().setText("No se pudo conectar con la base de datos");
			return;
		}

		if (encontrado == null || !PasswordUtil.verificar(password, encontrado.getSalt(), encontrado.getPasswordHash())) {
			// Mensaje deliberadamente genérico: no distingue "usuario no
			// existe" de "contraseña incorrecta", para no ayudar a alguien
			// a adivinar qué usuarios existen en el sistema.
			vista.getLblMensaje().setText("Usuario o contraseña incorrectos");
			vista.getTfPassword().setText("");
			return;
		}

		if (Boolean.FALSE.equals(encontrado.getEstado())) {
			vista.getLblMensaje().setText("Este usuario está deshabilitado. Consultá con un administrador.");
			return;
		}

		SesionActual.iniciar(encontrado);
		dao.registrarAcceso(encontrado);
		abrirPantallaPrincipal();
	}

	private void abrirPantallaPrincipal() {
		PantallaPrincipalVista principal = new PantallaPrincipalVista();
		principal.setControlador();
		principal.setVisible(true);
		vista.dispose();
	}

}
