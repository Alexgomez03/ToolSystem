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
import reportes.ProductoStockBajoDTO;
import util.ConexionJasper;
import util.FechaUtil;
import vista.InformeStockBajoVista;

public class InformeStockBajoController {

	private InformeStockBajoVista vista;
	private ProductoDAO dao = new ProductoDAO();

	public InformeStockBajoController(InformeStockBajoVista informeStockBajoVista) {
		this.vista = informeStockBajoVista;
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
		List<ProductoModelo> productos = dao.buscarConStockBajo();

		List<ProductoStockBajoDTO> filas = new ArrayList<ProductoStockBajoDTO>();
		for (ProductoModelo p : productos) {
			filas.add(new ProductoStockBajoDTO(
					p.getCodigo(),
					p.getDescripcion(),
					p.getCategoria() != null ? p.getCategoria().getNombre() : "-",
					p.getMarca() != null ? p.getMarca().getNombre() : "-",
					formatearNumero(p.getStock()),
					formatearNumero(p.getStockMinimo()),
					p.getUnidadMedida()));
		}

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fechaGeneracion", FechaUtil.fechaAString(java.time.LocalDate.now()));
		parametros.put("cantidadProductos", String.valueOf(filas.size()));

		try {
			ConexionJasper<ProductoStockBajoDTO> conexion = new ConexionJasper<ProductoStockBajoDTO>();
			conexion.generarReporte(filas, parametros, "stock_bajo");
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
		// Sin decimales si es un número entero (ej. "10" en vez de "10.0"),
		// que es como se maneja el stock en el resto de las pantallas.
		if (numero == Math.floor(numero))
			return String.valueOf(numero.longValue());
		return String.valueOf(numero);
	}

}
