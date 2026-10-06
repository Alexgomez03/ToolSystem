package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;

import dao.ProductoDAO;
import dao.StockMovimientoDAO;
import modelo.ProductoModelo;
import modelo.StockMovimientoModelo;
import tablas.ModeloTablaMovimientoStock;
import tablas.ModeloTablaStockBajo;
import util.FechaUtil;
import vista.ControlStockVista;

public class ControlStockController {

	private ControlStockVista vista;
	private StockMovimientoDAO dao;
	private ProductoDAO productoDAO;
	private ModeloTablaMovimientoStock tablaKardex;
	private ModeloTablaStockBajo tablaStockBajo;

	public ControlStockController(ControlStockVista controlStockVista) {
		super();
		this.vista = controlStockVista;
		dao = new StockMovimientoDAO();
		productoDAO = new ProductoDAO();

		tablaKardex = new ModeloTablaMovimientoStock();
		this.vista.getTablaKardex().setModel(tablaKardex);

		tablaStockBajo = new ModeloTablaStockBajo();
		this.vista.getTablaStockBajo().setModel(tablaStockBajo);

		setAcciones();
		buscar();
		cargarStockBajo();
	}

	private void setAcciones() {
		this.vista.getBtnBuscar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buscar();
			}
		});

		this.vista.getBtnCerrar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vista.dispose();
			}
		});
	}

	private void buscar() {
		LocalDate desde;
		LocalDate hasta;
		try {
			desde = obtenerFecha(this.vista.getTfDesde().getText());
			hasta = obtenerFecha(this.vista.getTfHasta().getText());
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}
		if (desde != null && hasta != null && desde.isAfter(hasta)) {
			JOptionPane.showMessageDialog(null, "La fecha \"Desde\" no puede ser posterior a la fecha \"Hasta\"");
			return;
		}

		List<StockMovimientoModelo> movimientos = dao.buscarPorFiltro(this.vista.getTfProducto().getText(), desde,
				hasta);
		tablaKardex.setLista(movimientos);
	}

	private void cargarStockBajo() {
		List<ProductoModelo> productos = productoDAO.buscarConStockBajo();
		tablaStockBajo.setLista(productos);
	}

	// Un campo de fecha vacío (solo la máscara sin completar) se considera
	// "sin filtro"; si el usuario cargó algo pero no es una fecha válida, se
	// avisa en vez de ignorarlo silenciosamente.
	private LocalDate obtenerFecha(String texto) {
		if (texto == null || texto.trim().isEmpty() || texto.contains("_"))
			return null;
		LocalDate fecha = FechaUtil.stringAFecha(texto);
		if (fecha == null)
			throw new IllegalArgumentException("La fecha \"" + texto + "\" no es válida");
		return fecha;
	}

}
