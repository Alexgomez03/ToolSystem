package tablas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.ProductoModelo;

public class ModeloTablaProducto extends AbstractTableModel {

	private String[] columnas = {"Código", "Descripción", "Categoría", "Marca", "Precio", "Stock", "Stock Mínimo"};
	List<ProductoModelo> lista = new ArrayList<ProductoModelo>();

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
		switch (columna) {
		case 0:
			return lista.get(fila).getCodigo();
		case 1:
			return lista.get(fila).getDescripcion();
		case 2:
			return lista.get(fila).getCategoria() != null ? lista.get(fila).getCategoria().getNombre() : "";
		case 3:
			return lista.get(fila).getMarca() != null ? lista.get(fila).getMarca().getNombre() : "";
		case 4:
			return lista.get(fila).getPrecioVenta();
		case 5:
			return lista.get(fila).getStock() + " " + lista.get(fila).getUnidadMedida();
		case 6:
			// Si no se cargó un stock mínimo para este producto, se
			// muestra "-" en vez de dejar la celda en blanco sin
			// explicación (para que se note que es un dato faltante, no
			// un stock mínimo de cero).
			return lista.get(fila).getStockMinimo() != null ? String.valueOf(lista.get(fila).getStockMinimo()) : "-";
		default:
			return null;
		}
	}

	public ProductoModelo getRegistro(int fila) {
		return lista.get(fila);
	}

}
