package tablas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.DetalleCompraModelo;

public class ModeloTablaDetalleCompra extends AbstractTableModel {

	private String[] columnas = {"Producto", "Cantidad", "Precio Costo", "Subtotal"};
	List<DetalleCompraModelo> lista = new ArrayList<DetalleCompraModelo>();

	public void setLista(List<DetalleCompraModelo> lista) {
		this.lista = lista;
		fireTableDataChanged();
	}

	public List<DetalleCompraModelo> getLista() {
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
		DetalleCompraModelo detalle = lista.get(fila);
		switch (columna) {
		case 0:
			return detalle.getProducto() != null ? detalle.getProducto().getDescripcion() : "";
		case 1:
			return detalle.getCantidad();
		case 2:
			return detalle.getPrecioCosto();
		case 3:
			return detalle.getCantidad() * detalle.getPrecioCosto();
		default:
			return null;
		}
	}

	public DetalleCompraModelo getRegistro(int fila) {
		return lista.get(fila);
	}

	public double getTotal() {
		double total = 0.0;
		for (DetalleCompraModelo d : lista) {
			total += d.getCantidad() * d.getPrecioCosto();
		}
		return total;
	}

}
