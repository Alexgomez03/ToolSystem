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

import dao.VentaDAO;
import modelo.EstadoPago;
import modelo.VentaModelo;
import reportes.CuentaPorCobrarDTO;
import util.ConexionJasper;
import util.FechaUtil;
import vista.InformeCuentasPorCobrarVista;

// Arma el informe "Cuentas por Cobrar": trae todas las ventas a
// crédito que no están anuladas, se queda solo con las que todavía no
// se cobraron del todo (Pendiente o Pago Parcial), y las manda a
// Jasper junto con el saldo total que falta cobrar.
public class InformeCuentasPorCobrarController {

	private InformeCuentasPorCobrarVista vista;
	private VentaDAO dao = new VentaDAO();

	public InformeCuentasPorCobrarController(InformeCuentasPorCobrarVista informeCuentasPorCobrarVista) {
		this.vista = informeCuentasPorCobrarVista;
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
		List<VentaModelo> creditos = dao.buscarCreditos();

		NumberFormat formatoMoneda = NumberFormat.getNumberInstance(Locale.of("es", "PY"));
		formatoMoneda.setMaximumFractionDigits(0);

		List<CuentaPorCobrarDTO> filas = new ArrayList<CuentaPorCobrarDTO>();
		double saldoTotal = 0;

		for (VentaModelo venta : creditos) {
			// Una venta ya pagada del todo no es una "cuenta por
			// cobrar": no aparece en este informe.
			if (venta.getEstadoPago() == EstadoPago.PAGADO)
				continue;

			double total = venta.getTotal() != null ? venta.getTotal() : 0.0;
			double pagado = venta.getMontoPagado() != null ? venta.getMontoPagado() : 0.0;
			double saldo = total - pagado;
			saldoTotal += saldo;

			filas.add(new CuentaPorCobrarDTO(
					venta.getId(),
					FechaUtil.fechaAString(venta.getFecha()),
					venta.getCliente() != null ? venta.getCliente().getNombre() + " " + venta.getCliente().getApellido()
							: "-",
					"Gs. " + formatoMoneda.format(total),
					"Gs. " + formatoMoneda.format(pagado),
					"Gs. " + formatoMoneda.format(saldo),
					venta.getEstadoPago().getTextoParaMostrar()));
		}

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fechaGeneracion", FechaUtil.fechaAString(java.time.LocalDate.now()));
		parametros.put("saldoTotal", "Gs. " + formatoMoneda.format(saldoTotal));

		try {
			ConexionJasper<CuentaPorCobrarDTO> conexion = new ConexionJasper<CuentaPorCobrarDTO>();
			conexion.generarReporte(filas, parametros, "cuentas_por_cobrar");
			conexion.ventanaReporte.setLocationRelativeTo(vista);
			conexion.ventanaReporte.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(vista, "No se pudo generar el informe: " + e.getMessage());
		}
	}

}
