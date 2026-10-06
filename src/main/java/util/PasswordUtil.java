package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


 // Hashing de contraseñas para el login del sistema.
 
 // Usa SHA-256 con un salt aleatorio por usuario (PBKDF-like: se aplica el
 // hash muchas veces para encarecer un ataque de fuerza bruta). No es
 // bcrypt/argon2 (no se agregó una librería externa para no arriesgar la
 // compilación en un entorno sin acceso a internet), pero es muchísimo más
 // seguro que guardar la contraseña en texto plano o un solo hash sin salt,
 // que es lo mínimo aceptable para un sistema con login real. Si en algún
 // momento se agrega una dependencia como jBCrypt, esta clase es el único
 // lugar que habría que tocar.
 
public class PasswordUtil {

	private static final int ITERACIONES = 100_000;
	private static final int LARGO_SALT = 16;

	private PasswordUtil() {
	}

	public static String generarSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[LARGO_SALT];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	public static String hash(String password, String saltBase64) {
		try {
			byte[] salt = Base64.getDecoder().decode(saltBase64);
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] resultado = password.getBytes("UTF-8");
			digest.update(salt);
			for (int i = 0; i < ITERACIONES; i++) {
				digest.reset();
				digest.update(salt);
				resultado = digest.digest(resultado);
			}
			return Base64.getEncoder().encodeToString(resultado);
		} catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
			throw new RuntimeException("No se pudo calcular el hash de la contraseña", e);
		}
	}

	public static boolean verificar(String password, String saltBase64, String hashEsperado) {
		if (password == null || saltBase64 == null || hashEsperado == null)
			return false;
		String hashCalculado = hash(password, saltBase64);
		return constantTimeEquals(hashCalculado, hashEsperado);
	}

	
	 // Comparación en tiempo constante para no filtrar por timing cuánto de
	 // la contraseña ingresada coincide con la real.
	 
	private static boolean constantTimeEquals(String a, String b) {
		if (a.length() != b.length())
			return false;
		int resultado = 0;
		for (int i = 0; i < a.length(); i++)
			resultado |= a.charAt(i) ^ b.charAt(i);
		return resultado == 0;
	}

}
