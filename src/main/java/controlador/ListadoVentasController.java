package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dao.VentaDAO;
import modelo.VentaModelo;
import tablas.ModeloTablaDetalleVenta;
import tablas.ModeloTablaVenta;
import util.FechaUtil;
import vista.ListadoVentasVista;

public class ListadoVentasController {

	private ListadoVentasVista vista;
	private VentaDAO dao;
	private ModeloTablaVenta tablaVentas;
	private List<VentaModelo> ventas;

	public ListadoVentasController(ListadoVentasVista listadoVentasVista) {
		super();
		this.vista = listadoVentasVista;
		dao = new VentaDAO();

		tablaVentas = new ModeloTablaVenta();
		this.vista.getTablaVentas().setModel(tablaVentas);

		setAcciones();
		buscar();
	}

	private void setAcciones() {
		this.vista.getBtnBuscar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buscar();
			}
		});

		this.vista.getBtnVerDetalle().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				verDetalle();
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

		ventas = dao.buscarPorFiltro(this.vista.getTfCliente().getText(), desde, hasta);
		tablaVentas.setLista(ventas);
	}

	private void verDetalle() {
		int fila = this.vista.getTablaVentas().getSelectedRow();
		if (fila < 0) {
			JOptionPane.showMessageDialog(null, "Seleccione una venta de la lista para ver su detalle.");
			return;
		}

		VentaModelo venta = ventas.get(fila);

		ModeloTablaDetalleVenta modeloDetalle = new ModeloTablaDetalleVenta();
		modeloDetalle.setLista(venta.getDetalles());

		JTable tablaDetalle = new JTable(modeloDetalle);
		tablaDetalle.setRowHeight(24);
		JScrollPane scroll = new JScrollPane(tablaDetalle);
		scroll.setPreferredSize(new java.awt.Dimension(560, 220));

		String encabezado = "Venta N° " + venta.getId() + " — "
				+ (venta.getCliente() != null ? venta.getCliente().getNombre() + " " + venta.getCliente().getApellido() : "")
				+ " — Vendedor: "
				+ (venta.getFuncionario() != null
						? venta.getFuncionario().getNombre() + " " + venta.getFuncionario().getApellido()
						: "(sin registrar)");

		JOptionPane.showMessageDialog(this.vista,
				new Object[] { encabezado, scroll }, "Detalle de la venta", JOptionPane.PLAIN_MESSAGE);
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
