package componentes;

import java.awt.Color;
import java.awt.Font;

/**
 * Paleta de colores y tipografías del sistema "ToolSystem".
 * Estilo industrial/ferretería: acero + naranja de seguridad.
 *
 * Se centraliza acá para que todos los componentes personalizados
 * (botones, labels, campos, diálogos) compartan una misma identidad visual.
 */
public class Tema {

	// Colores principales
	public static final Color NARANJA = new Color(255, 106, 19);
	public static final Color NARANJA_OSCURO = new Color(214, 84, 12);
	public static final Color ACERO = new Color(43, 92, 122);
	public static final Color ACERO_OSCURO = new Color(31, 68, 92);
	public static final Color CARBON = new Color(37, 46, 56);
	public static final Color CARBON_CLARO = new Color(52, 63, 75);
	public static final Color GRIS = new Color(108, 118, 128);
	public static final Color VERDE = new Color(56, 150, 96);
	public static final Color ROJO = new Color(206, 68, 68);
	public static final Color DORADO = new Color(196, 149, 46);

	// Neutros / superficies
	public static final Color FONDO_TARJETA = Color.WHITE;
	public static final Color FONDO_FORMULARIO = new Color(250, 249, 246);
	public static final Color BORDE_SUAVE = new Color(214, 214, 214);
	public static final Color TEXTO_PRINCIPAL = new Color(37, 46, 56);
	public static final Color TEXTO_SECUNDARIO = new Color(108, 118, 128);
	public static final Color TEXTO_CLARO = Color.WHITE;

	// Tipografías
	public static final Font FUENTE_LABEL = new Font("Segoe UI", Font.BOLD, 13);
	public static final Font FUENTE_CAMPO = new Font("Segoe UI", Font.PLAIN, 13);
	public static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 12);
	public static final Font FUENTE_BOTON_ACCESO = new Font("Segoe UI", Font.BOLD, 15);
	public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);

}
