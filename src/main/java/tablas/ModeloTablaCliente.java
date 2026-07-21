package tablas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.ClienteModelo;

public class ModeloTablaCliente extends AbstractTableModel {
	
	private String[] columnas = {"Código", "Nombre y Apellido", "Documento", "Contacto"};
	List<ClienteModelo> lista = new ArrayList<ClienteModelo>();
	
	
	public void setLista(List<ClienteModelo> lista) {
		this.lista = lista;
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return lista.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnas.length;
	}
	
	@Override
	public String getColumnName(int posicion) {
		// TODO Auto-generated method stub
		return columnas[posicion];
	}

	@Override
	public Object getValueAt(int fila, int columna) {
		switch (columna) {
		case 0:
			return lista.get(fila).getId();
		case 1:
			return lista.get(fila).getNombre() + " "+ lista.get(fila).getApellido();
		case 2:
			return lista.get(fila).getDocumento();
		case 3:
			return lista.get(fila).getTelefono() != null ? 
					lista.get(fila).getTelefono() : lista.get(fila).getCorreo();
		default:
			return null;
		}
	}

}
