package tablas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.StockMovimientoModelo;
import util.FechaUtil;

public class ModeloTablaMovimientoStock extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnas = { "Fecha", "Producto", "Tipo", "Cantidad", "Stock Resultante", "Motivo" };
	private List<StockMovimientoModelo> lista = new ArrayList<StockMovimientoModelo>();

	public void setLista(List<StockMovimientoModelo> lista) {
		this.lista = lista;
		fireTableDataChanged();
	}

	public List<StockMovimientoModelo> getLista() {
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
		StockMovimientoModelo movimiento = lista.get(fila);
		switch (columna) {
		case 0:
			return movimiento.getFecha() != null ? FechaUtil.fechaHoraAString(movimiento.getFecha()) : "";
		case 1:
			return movimiento.getProducto() != null ? movimiento.getProducto().getDescripcion() : "";
		case 2:
			return movimiento.getTipo() != null ? movimiento.getTipo().name() : "";
		case 3:
			return movimiento.getCantidad();
		case 4:
			return movimiento.getStockResultante();
		case 5:
			return movimiento.getMotivo();
		default:
			return null;
		}
	}

	public StockMovimientoModelo getRegistro(int fila) {
		return lista.get(fila);
	}

}
