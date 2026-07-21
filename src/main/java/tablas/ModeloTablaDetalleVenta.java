package tablas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.DetalleVentaModelo;

public class ModeloTablaDetalleVenta extends AbstractTableModel {

	private String[] columnas = {"Producto", "Cantidad", "Precio", "Subtotal"};
	List<DetalleVentaModelo> lista = new ArrayList<DetalleVentaModelo>();

	public void setLista(List<DetalleVentaModelo> lista) {
		this.lista = lista;
		fireTableDataChanged();
	}

	public List<DetalleVentaModelo> getLista() {
		return lista;
	}

	@Override
	public int getRowCount() {
		return lista.size();
	}

	@Override
	public int getColumnCount() {
		return columnas.length;
	}

	@Override
	public String getColumnName(int posicion) {
		return columnas[posicion];
	}

	@Override
	public Object getValueAt(int fila, int columna) {
		DetalleVentaModelo detalle = lista.get(fila);
		switch (columna) {
		case 0:
			return detalle.getProducto() != null ? detalle.getProducto().getDescripcion() : "";
		case 1:
			return detalle.getCantidad();
		case 2:
			return detalle.getPrecio();
		case 3:
			return detalle.getCantidad() * detalle.getPrecio();
		default:
			return null;
		}
	}

	public DetalleVentaModelo getRegistro(int fila) {
		return lista.get(fila);
	}

	public double getTotal() {
		double total = 0.0;
		for (DetalleVentaModelo d : lista) {
			total += d.getCantidad() * d.getPrecio();
		}
		return total;
	}

}
