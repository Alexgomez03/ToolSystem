package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import dao.ProductoDAO;
import modelo.ProductoModelo;
import reportes.ProductoInventarioDTO;
import util.ConexionJasper;
import util.FechaUtil;
import vista.InformeStockCompletoVista;

public class InformeStockCompletoController {

	private InformeStockCompletoVista vista;
	private ProductoDAO dao = new ProductoDAO();

	public InformeStockCompletoController(InformeStockCompletoVista informeStockCompletoVista) {
		this.vista = informeStockCompletoVista;
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
		List<ProductoModelo> productos = dao.buscarTodosParaInventario();

		List<ProductoInventarioDTO> filas = new ArrayList<ProductoInventarioDTO>();
		for (ProductoModelo p : productos) {
			filas.add(new ProductoInventarioDTO(
					p.getCodigo(),
					p.getDescripcion(),
					p.getCategoria() != null ? p.getCategoria().getNombre() : "-",
					p.getMarca() != null ? p.getMarca().getNombre() : "-",
					formatearNumero(p.getStock()),
					p.getUnidadMedida(),
					Boolean.FALSE.equals(p.getEstado()) ? "Inactivo" : "Activo"));
		}

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fechaGeneracion", FechaUtil.fechaAString(java.time.LocalDate.now()));
		parametros.put("cantidadProductos", String.valueOf(filas.size()));

		try {
			ConexionJasper<ProductoInventarioDTO> conexion = new ConexionJasper<ProductoInventarioDTO>();
			conexion.generarReporte(filas, parametros, "stock_completo");
			conexion.ventanaReporte.setLocationRelativeTo(vista);
			conexion.ventanaReporte.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(vista, "No se pudo generar el informe: " + e.getMessage());
		}
	}

	private String formatearNumero(Double numero) {
		if (numero == null)
			return "-";
		if (numero == Math.floor(numero))
			return String.valueOf(numero.longValue());
		return String.valueOf(numero);
	}

}
