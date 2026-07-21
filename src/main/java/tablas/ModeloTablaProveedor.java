package tablas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.ProveedorModelo;

public class ModeloTablaProveedor extends AbstractTableModel {

	private String[] columnas = {"Código", "Razón Social", "RUC", "Contacto"};
	List<ProveedorModelo> lista = new ArrayList<ProveedorModelo>();

	public void setLista(List<ProveedorModelo> lista) {
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
			return lista.get(fila).getNombreFantasia() != null ? lista.get(fila).getNombreFantasia()
					: lista.get(fila).getRazonSocial();
		case 2:
			return lista.get(fila).getRuc();
		case 3:
			return lista.get(fila).getTelefono() != null ? lista.get(fila).getTelefono()
					: lista.get(fila).getCorreo();
		default:
			return null;
		}
	}

	public ProveedorModelo getRegistro(int fila) {
		return lista.get(fila);
	}

}
