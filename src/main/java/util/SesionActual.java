package util;

import modelo.UsuarioModelo;


 // Guarda qué usuario está logueado en esta ejecución de la aplicación. Como
 // es un sistema de escritorio de un solo usuario por instancia (no hay
 // concepto de "varias sesiones concurrentes" en un mismo proceso), alcanza
 // con un holder estático simple en vez de un mecanismo de sesión más
 // elaborado (que sí tendría sentido en una app web).
 
public class SesionActual {

	private static UsuarioModelo usuario;

	private SesionActual() {
	}

	public static void iniciar(UsuarioModelo usuarioLogueado) {
		usuario = usuarioLogueado;
	}

	public static void cerrar() {
		usuario = null;
	}

	public static UsuarioModelo getUsuario() {
		return usuario;
	}

	public static boolean hayUsuarioLogueado() {
		return usuario != null;
	}

	public static boolean esAdministrador() {
		return usuario != null && usuario.getRol() == UsuarioModelo.Rol.ADMINISTRADOR;
	}

}
