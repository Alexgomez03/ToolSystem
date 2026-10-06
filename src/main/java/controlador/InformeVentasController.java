package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.text.NumberFormat;

import javax.swing.JOptionPane;

import dao.VentaDAO;
import modelo.VentaModelo;
import reportes.VentaReporteDTO;
import util.ConexionJasper;
import util.FechaUtil;
import vista.InformeVentasVista;

public class InformeVentasController {

	private InformeVentasVista vista;
	private VentaDAO dao = new VentaDAO();

	public InformeVentasController(InformeVentasVista informeVentasVista) {
		this.vista = informeVentasVista;
		setAcciones();
	}

	private void setAcciones() {
		this.vista.getBtnGenerar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generar();
			}
		});
		this.vista.getBtnCerrar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vista.dispose();
			}
		});
	}

	private void generar() {
		LocalDate desde;
		LocalDate hasta;
		try {
			desde = obtenerFecha(this.vista.getTfDesde().getText());
			hasta = obtenerFecha(this.vista.getTfHasta().getText());
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(vista, e.getMessage());
			return;
		}
		if (desde != null && hasta != null && desde.isAfter(hasta)) {
			JOptionPane.showMessageDialog(vista, "La fecha \"Desde\" no puede ser posterior a la fecha \"Hasta\"");
			return;
		}

		List<VentaModelo> ventas = dao.buscarPorFiltro("", desde, hasta);

		NumberFormat formatoMoneda = NumberFormat.getNumberInstance(Locale.of("es", "PY"));
		formatoMoneda.setMinimumFractionDigits(0);
		formatoMoneda.setMaximumFractionDigits(0);

		List<VentaReporteDTO> filas = new ArrayList<VentaReporteDTO>();
		double totalGeneral = 0;
		for (VentaModelo v : ventas) {
			boolean anulada = Boolean.TRUE.equals(v.getAnulada());
			filas.add(new VentaReporteDTO(
					v.getId(),
					FechaUtil.fechaAString(v.getFecha()),
					v.getCliente() != null ? v.getCliente().getNombre() + " " + v.getCliente().getApellido() : "-",
					v.getFuncionario() != null
							? v.getFuncionario().getNombre() + " " + v.getFuncionario().getApellido()
							: "-",
					// Una vez guardada, una venta ya está completa (el
					// producto salió del local); "Activa" daba a entender
					// que seguía en curso. "Anulada" se mantiene igual.
					anulada ? "Anulada" : "Concluida",
					"Gs. " + formatoMoneda.format(v.getTotal() != null ? v.getTotal() : 0)));
			// Una venta anulada no debería sumar al total facturado del período.
			if (!anulada && v.getTotal() != null)
				totalGeneral += v.getTotal();
		}

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fechaDesde", desde != null ? FechaUtil.fechaAString(desde) : "(inicio)");
		parametros.put("fechaHasta", hasta != null ? FechaUtil.fechaAString(hasta) : "(hoy)");
		parametros.put("cantidadVentas", String.valueOf(filas.size()));
		parametros.put("totalGeneral", "Gs. " + formatoMoneda.format(totalGeneral));

		try {
			ConexionJasper<VentaReporteDTO> conexion = new ConexionJasper<VentaReporteDTO>();
			conexion.generarReporte(filas, parametros, "ventas_periodo");
			conexion.ventanaReporte.setLocationRelativeTo(vista);
			conexion.ventanaReporte.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(vista, "No se pudo generar el informe: " + e.getMessage());
		}
	}

	private LocalDate obtenerFecha(String texto) {
		if (texto == null || texto.trim().isEmpty() || texto.contains("_"))
			return null;
		LocalDate fecha = FechaUtil.stringAFecha(texto);
		if (fecha == null)
			throw new IllegalArgumentException("La fecha \"" + texto + "\" no es válida");
		return fecha;
	}

}
