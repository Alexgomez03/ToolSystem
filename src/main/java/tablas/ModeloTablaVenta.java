package tablas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.VentaModelo;
import util.FechaUtil;

public class ModeloTablaVenta extends AbstractTableModel {

	private String[] columnas = {"Código", "Fecha", "Cliente", "Vendedor", "Total", "Estado", "Forma de Pago",
			"Estado de Pago"};
	List<VentaModelo> lista = new ArrayList<VentaModelo>();

	public void setLista(List<VentaModelo> lista) {
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
			return lista.get(fila).getFecha() != null ? FechaUtil.fechaAString(lista.get(fila).getFecha()) : "";
		case 2:
			return lista.get(fila).getCliente() != null
					? lista.get(fila).getCliente().getNombre() + " " + lista.get(fila).getCliente().getApellido()
					: "";
		case 3:
			return lista.get(fila).getFuncionario() != null
					? lista.get(fila).getFuncionario().getNombre() + " " + lista.get(fila).getFuncionario().getApellido()
					: "";
		case 4:
			return lista.get(fila).getTotal();
		case 5:
			return Boolean.TRUE.equals(lista.get(fila).getAnulada()) ? "Anulada" : "Vigente";
		case 6:
			// Los registros viejos, de antes de agregar esta columna, no
			// tienen forma de pago cargada; se muestran como "Contado"
			// para no dejar la celda en blanco sin explicación.
			return lista.get(fila).getFormaPago() == modelo.FormaPago.CREDITO ? "Crédito" : "Contado";
		case 7:
			return lista.get(fila).getEstadoPago().getTextoParaMostrar();
		default:
			return null;
		}
	}

	public VentaModelo getRegistro(int fila) {
		return lista.get(fila);
	}

}
