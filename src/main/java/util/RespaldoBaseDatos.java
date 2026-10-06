package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// Esto genera una copia de seguridad completa de la base de datos,
// usando "pg_dump" (una herramienta que viene instalada junto con
// PostgreSQL, no algo que haya que instalar aparte). El archivo que
// genera es un ".sql" con todas las instrucciones necesarias para
// recrear la base entera desde cero si hiciera falta.

// Los datos de conexión (dirección, usuario, contraseña) NO están
// escritos de nuevo acá: se leen directamente del mismo
// hibernate.cfg.xml que ya usa el resto del sistema, así este archivo
// nunca queda desactualizado si en algún momento cambia la
// configuración de la base.
public class RespaldoBaseDatos {

	private RespaldoBaseDatos() {
	}

	// Hace el respaldo y lo guarda en el archivo indicado. Si algo
	// sale mal (no se encuentra pg_dump, la contraseña está mal, no
	// hay conexión, etc.) tira una excepción con un mensaje que se le
	// pueda mostrar directamente a la persona.
	public static void respaldar(File archivoDestino) throws Exception {
		String[] datosConexion = leerDatosDeConexion();
		String host = datosConexion[0];
		String puerto = datosConexion[1];
		String baseDeDatos = datosConexion[2];
		String usuario = datosConexion[3];
		String contraseña = datosConexion[4];

		ProcessBuilder builder = new ProcessBuilder("pg_dump", "-h", host, "-p", puerto, "-U", usuario, "-F", "p",
				"-f", archivoDestino.getAbsolutePath(), baseDeDatos);
		// pg_dump pide la contraseña por una variable de entorno (si no,
		// se queda esperando que alguien la escriba en una consola que
		// acá no existe, y el programa parecería colgado para siempre).
		builder.environment().put("PGPASSWORD", contraseña);
		builder.redirectErrorStream(true);

		Process proceso;
		try {
			proceso = builder.start();
		} catch (Exception e) {
			throw new Exception(
					"No se encontró el comando \"pg_dump\". Verificá que PostgreSQL esté instalado y que "
							+ "su carpeta \"bin\" esté agregada al PATH del sistema.");
		}

		// Se junta toda la salida del proceso, para poder mostrarla si
		// algo sale mal (pg_dump explica el motivo por ahí).
		StringBuilder salida = new StringBuilder();
		try (BufferedReader lector = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
			String linea;
			while ((linea = lector.readLine()) != null)
				salida.append(linea).append("\n");
		}

		int codigoSalida = proceso.waitFor();
		if (codigoSalida != 0) {
			throw new Exception("pg_dump terminó con un error:\n" + salida.toString());
		}
	}

	// Va al hibernate.cfg.xml (el mismo que usa toda la aplicación) y
	// saca de ahí la dirección, el usuario y la contraseña de la base,
	// para no tener una segunda copia de estos datos dando vueltas por
	// el código.
	private static String[] leerDatosDeConexion() throws Exception {
		URL recurso = RespaldoBaseDatos.class.getResource("/hibernate.cfg.xml");
		if (recurso == null)
			throw new Exception("No se encontró hibernate.cfg.xml");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document documento = builder.parse(recurso.toString());

		String url = leerPropiedad(documento, "connection.url");
		String usuario = leerPropiedad(documento, "connection.username");
		String contraseña = leerPropiedad(documento, "connection.password");

		// La URL tiene la forma jdbc:postgresql://host:puerto/base
		Pattern patron = Pattern.compile("jdbc:postgresql://([^:/]+):(\\d+)/([^?]+)");
		Matcher matcher = patron.matcher(url);
		if (!matcher.find())
			throw new Exception("No se pudo interpretar la URL de conexión: " + url);

		return new String[] { matcher.group(1), matcher.group(2), matcher.group(3), usuario, contraseña };
	}

	private static String leerPropiedad(Document documento, String nombre) {
		NodeList propiedades = documento.getElementsByTagName("property");
		for (int i = 0; i < propiedades.getLength(); i++) {
			Element propiedad = (Element) propiedades.item(i);
			if (nombre.equals(propiedad.getAttribute("name")))
				return propiedad.getTextContent().trim();
		}
		return null;
	}

}
