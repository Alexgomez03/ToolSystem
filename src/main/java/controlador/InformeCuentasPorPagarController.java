package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JOptionPane;

import dao.CompraDAO;
import modelo.CompraModelo;
import modelo.EstadoPago;
import reportes.CuentaPorPagarDTO;
import util.ConexionJasper;
import util.FechaUtil;
import vista.InformeCuentasPorPagarVista;

// Mismo esquema que InformeCuentasPorCobrarController, pero para las
// compras a crédito que todavía se le deben a un proveedor.
public class InformeCuentasPorPagarController {

	private InformeCuentasPorPagarVista vista;
	private CompraDAO dao = new CompraDAO();

	public InformeCuentasPorPagarController(InformeCuentasPorPagarVista informeCuentasPorPagarVista) {
		this.vista = informeCuentasPorPagarVista;
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
		List<CompraModelo> creditos = dao.buscarCreditos();

		NumberFormat formatoMoneda = NumberFormat.getNumberInstance(Locale.of("es", "PY"));
		formatoMoneda.setMaximumFractionDigits(0);

		List<CuentaPorPagarDTO> filas = new ArrayList<CuentaPorPagarDTO>();
		double saldoTotal = 0;

		for (CompraModelo compra : creditos) {
			// Una compra ya pagada del todo no es una "cuenta por
			// pagar": no aparece en este informe.
			if (compra.getEstadoPago() == EstadoPago.PAGADO)
				continue;

			double total = compra.getTotal() != null ? compra.getTotal() : 0.0;
			double pagado = compra.getMontoPagado() != null ? compra.getMontoPagado() : 0.0;
			double saldo = total - pagado;
			saldoTotal += saldo;

			filas.add(new CuentaPorPagarDTO(
					compra.getId(),
					FechaUtil.fechaAString(compra.getFecha()),
					compra.getProveedor() != null
							? (compra.getProveedor().getNombreFantasia() != null ? compra.getProveedor().getNombreFantasia()
									: compra.getProveedor().getRazonSocial())
							: "-",
					"Gs. " + formatoMoneda.format(total),
					"Gs. " + formatoMoneda.format(pagado),
					"Gs. " + formatoMoneda.format(saldo),
					compra.getEstadoPago().getTextoParaMostrar()));
		}

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fechaGeneracion", FechaUtil.fechaAString(java.time.LocalDate.now()));
		parametros.put("saldoTotal", "Gs. " + formatoMoneda.format(saldoTotal));

		try {
			ConexionJasper<CuentaPorPagarDTO> conexion = new ConexionJasper<CuentaPorPagarDTO>();
			conexion.generarReporte(filas, parametros, "cuentas_por_pagar");
			conexion.ventanaReporte.setLocationRelativeTo(vista);
			conexion.ventanaReporte.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(vista, "No se pudo generar el informe: " + e.getMessage());
		}
	}

}
