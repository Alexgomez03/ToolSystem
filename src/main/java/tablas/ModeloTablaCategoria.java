package tablas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.CategoriaModelo;

public class ModeloTablaCategoria extends AbstractTableModel {

	private String[] columnas = {"Código", "Nombre", "Estado"};
	List<CategoriaModelo> lista = new ArrayList<CategoriaModelo>();

	public void setLista(List<CategoriaModelo> lista) {
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
			return lista.get(fila).getId();
		case 1:
			return lista.get(fila).getNombre();
		case 2:
			return Boolean.TRUE.equals(lista.get(fila).getEstado()) ? "Activo" : "Inactivo";
		default:
			return null;
		}
	}

	public CategoriaModelo getRegistro(int fila) {
		return lista.get(fila);
	}

}
