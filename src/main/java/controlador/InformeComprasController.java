package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JOptionPane;

import dao.CompraDAO;
import modelo.CompraModelo;
import reportes.CompraReporteDTO;
import util.ConexionJasper;
import util.FechaUtil;
import vista.InformeComprasVista;

public class InformeComprasController {

	private InformeComprasVista vista;
	private CompraDAO dao = new CompraDAO();

	public InformeComprasController(InformeComprasVista informeComprasVista) {
		this.vista = informeComprasVista;
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

		List<CompraModelo> compras = dao.buscarPorFiltro("", desde, hasta);

		NumberFormat formatoMoneda = NumberFormat.getNumberInstance(Locale.of("es", "PY"));
		formatoMoneda.setMinimumFractionDigits(0);
		formatoMoneda.setMaximumFractionDigits(0);

		List<CompraReporteDTO> filas = new ArrayList<CompraReporteDTO>();
		double totalGeneral = 0;
		for (CompraModelo c : compras) {
			boolean anulada = Boolean.TRUE.equals(c.getAnulada());
			filas.add(new CompraReporteDTO(
					c.getId(),
					FechaUtil.fechaAString(c.getFecha()),
					c.getProveedor() != null
							? (c.getProveedor().getNombreFantasia() != null ? c.getProveedor().getNombreFantasia()
									: c.getProveedor().getRazonSocial())
							: "-",
					c.getNroFactura(),
					anulada ? "Anulada" : "Vigente",
					"Gs. " + formatoMoneda.format(c.getTotal() != null ? c.getTotal() : 0)));
			if (!anulada && c.getTotal() != null)
				totalGeneral += c.getTotal();
		}

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fechaDesde", desde != null ? FechaUtil.fechaAString(desde) : "(inicio)");
		parametros.put("fechaHasta", hasta != null ? FechaUtil.fechaAString(hasta) : "(hoy)");
		parametros.put("cantidadCompras", String.valueOf(filas.size()));
		parametros.put("totalGeneral", "Gs. " + formatoMoneda.format(totalGeneral));

		try {
			ConexionJasper<CompraReporteDTO> conexion = new ConexionJasper<CompraReporteDTO>();
			conexion.generarReporte(filas, parametros, "compras_periodo");
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
