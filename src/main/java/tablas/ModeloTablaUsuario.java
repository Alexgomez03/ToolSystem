package tablas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.UsuarioModelo;

public class ModeloTablaUsuario extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnas = { "Código", "Usuario", "Funcionario vinculado", "Rol", "Estado", "Último acceso" };
	private List<UsuarioModelo> lista = new ArrayList<UsuarioModelo>();

	public void setLista(List<UsuarioModelo> lista) {
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
		UsuarioModelo usuario = lista.get(fila);
		switch (columna) {
		case 0:
			return usuario.getId();
		case 1:
			return usuario.getUsuario();
		case 2:
			return usuario.getFuncionario() != null
					? usuario.getFuncionario().getNombre() + " " + usuario.getFuncionario().getApellido()
					: "-";
		case 3:
			return usuario.getRol() == UsuarioModelo.Rol.ADMINISTRADOR ? "Administrador" : "Vendedor";
		case 4:
			return Boolean.FALSE.equals(usuario.getEstado()) ? "Inactivo" : "Activo";
		case 5:
			return usuario.getUltimoAcceso() != null ? util.FechaUtil.fechaHoraAString(usuario.getUltimoAcceso())
					: "Nunca";
		default:
			return null;
		}
	}

	public UsuarioModelo getUsuarioEn(int fila) {
		return lista.get(fila);
	}

}
