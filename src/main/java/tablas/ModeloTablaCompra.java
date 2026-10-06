package tablas;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.CompraModelo;
import util.FechaUtil;

public class ModeloTablaCompra extends AbstractTableModel {

	private String[] columnas = {"Código", "Fecha", "Proveedor", "N° Factura", "Total", "Estado", "Forma de Pago",
			"Estado de Pago"};
	List<CompraModelo> lista = new ArrayList<CompraModelo>();

	public void setLista(List<CompraModelo> lista) {
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
		CompraModelo compra = lista.get(fila);
		switch (columna) {
		case 0:
			return compra.getId();
		case 1:
			return compra.getFecha() != null ? FechaUtil.fechaAString(compra.getFecha()) : "";
		case 2:
			return compra.getProveedor() != null
					? (compra.getProveedor().getNombreFantasia() != null ? compra.getProveedor().getNombreFantasia()
							: compra.getProveedor().getRazonSocial())
					: "";
		case 3:
			return compra.getNroFactura();
		case 4:
			return compra.getTotal();
		case 5:
			return Boolean.TRUE.equals(compra.getAnulada()) ? "Anulada" : "Vigente";
		case 6:
			return compra.getFormaPago() == modelo.FormaPago.CREDITO ? "Crédito" : "Contado";
		case 7:
			return compra.getEstadoPago().getTextoParaMostrar();
		default:
			return null;
		}
	}

	public CompraModelo getRegistro(int fila) {
		return lista.get(fila);
	}

}
