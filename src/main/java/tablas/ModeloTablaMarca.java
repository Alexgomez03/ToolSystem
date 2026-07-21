package tablas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.MarcaModelo;

public class ModeloTablaMarca extends AbstractTableModel {

	private String[] columnas = {"Código", "Nombre", "Estado"};
	List<MarcaModelo> lista = new ArrayList<MarcaModelo>();

	public void setLista(List<MarcaModelo> lista) {
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

	public MarcaModelo getRegistro(int fila) {
		return lista.get(fila);
	}

}
