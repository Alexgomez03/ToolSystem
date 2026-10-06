package tablas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.ProductoModelo;

public class ModeloTablaStockBajo extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnas = { "Código", "Descripción", "Stock", "Stock Mínimo" };
	private List<ProductoModelo> lista = new ArrayList<ProductoModelo>();

	public void setLista(List<ProductoModelo> lista) {
		this.lista = lista;
		fireTableDataChanged();
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
		ProductoModelo producto = lista.get(fila);
		switch (columna) {
		case 0:
			return producto.getCodigo();
		case 1:
			return producto.getDescripcion();
		case 2:
			return producto.getStock() + " " + producto.getUnidadMedida();
		case 3:
			return producto.getStockMinimo();
		default:
			return null;
		}
	}

	public ProductoModelo getRegistro(int fila) {
		return lista.get(fila);
	}

}
