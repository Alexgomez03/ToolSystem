package util;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.text.MaskFormatter;

public class FechaUtil {
	
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

	private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
	
	// =================== Creación de Formatos para TextField =================
	
	public static MaskFormatter getFormatoFecha() {
		try {
			MaskFormatter mascara = new MaskFormatter("##/##/####");
			mascara.setPlaceholderCharacter('_');
			return mascara;
		} catch (ParseException e) {
			throw new RuntimeException();
		}
	}
	
	public static MaskFormatter getFormatoHora() {
		try {
			MaskFormatter mascara = new MaskFormatter("##:##");
			mascara.setPlaceholderCharacter('_');
			return mascara;
		} catch (ParseException e) {
			throw new RuntimeException();
		}
	}
	
	public static MaskFormatter getFormatoFechaHora() {
		try {
			MaskFormatter mascara = new MaskFormatter("##/##/#### ##:##");
			mascara.setPlaceholderCharacter('_');
			return mascara;
		} catch (ParseException e) {
			throw new RuntimeException();
		}
	}
	
	// ==================== LocalDate a String ==================
	
	public static String fechaAString(LocalDate fecha) {
		return fecha.format(FORMATO_FECHA);
	}
	
	public static String horaAString(LocalTime hora) {
		return hora.format(FORMATO_HORA);
	}
	
	public static String fechaHoraAString(LocalDateTime fechaHora) {
		return fechaHora.format(FORMATO_FECHA_HORA);
	}
	
	
	// ==================== String a LocalDate ==================

	public static LocalDate stringAFecha(String texto) {
		try {
			return LocalDate.parse(texto, FORMATO_FECHA);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static LocalTime stringAHora(String texto) {
		try {
			return LocalTime.parse(texto, FORMATO_HORA);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static LocalDateTime stringAFechaHora(String texto) {
		try {
			return LocalDateTime.parse(texto, FORMATO_FECHA_HORA);
		} catch (Exception e) {
			return null;
		}
	}
}
