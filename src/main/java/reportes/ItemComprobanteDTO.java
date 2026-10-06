package reportes;

// Una línea del comprobante de venta (ver
// /jasper/comprobante_venta.jrxml): un producto vendido, con su
// cantidad, precio unitario y el subtotal ya calculado.
public class ItemComprobanteDTO {

	private String producto;
	private String cantidad;
	private String precioUnitario;
	private String subtotal;

	public ItemComprobanteDTO(String producto, String cantidad, String precioUnitario, String subtotal) {
		this.producto = producto;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.subtotal = subtotal;
	}

	public String getProducto() {
		return producto;
	}

	public String getCantidad() {
		return cantidad;
	}

	public String getPrecioUnitario() {
		return precioUnitario;
	}

	public String getSubtotal() {
		return subtotal;
	}

}
