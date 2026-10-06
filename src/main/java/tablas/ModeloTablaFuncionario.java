package tablas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.FuncionarioModelo;

public class ModeloTablaFuncionario extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnas = { "Código", "Nombre y Apellido", "Documento", "Cargo", "Contacto", "Estado" };
	private List<FuncionarioModelo> lista = new ArrayList<FuncionarioModelo>();

	public void setLista(List<FuncionarioModelo> lista) {
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
		FuncionarioModelo funcionario = lista.get(fila);
		switch (columna) {
		case 0:
			return funcionario.getId();
		case 1:
			return funcionario.getNombre() + " " + funcionario.getApellido();
		case 2:
			return funcionario.getDocumento();
		case 3:
			return funcionario.getCargo();
		case 4:
			return funcionario.getTelefono() != null ? funcionario.getTelefono() : funcionario.getCorreo();
		case 5:
			return Boolean.FALSE.equals(funcionario.getEstado()) ? "Inactivo" : "Activo";
		default:
			return null;
		}
	}

	public FuncionarioModelo getRegistro(int fila) {
		return lista.get(fila);
	}

}
