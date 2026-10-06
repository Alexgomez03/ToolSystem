package reportes;

/**
 * Fila plana para el reporte "Stock Completo" (ver
 * /jasper/stock_completo.jrxml), pensado para usarse como planilla de
 * inventario físico: a diferencia de {@link ProductoStockBajoDTO}, incluye
 * TODOS los productos (activos e inactivos, cualquiera sea su nivel de
 * stock), más una columna de estado para saber cuáles ya están dados de
 * baja del catálogo pero podrían seguir teniendo unidades físicas en el
 * depósito.
 */
public class ProductoInventarioDTO {

	private String codigo;
	private String descripcion;
	private String categoria;
	private String marca;
	private String stock;
	private String unidadMedida;
	private String estado;

	public ProductoInventarioDTO(String codigo, String descripcion, String categoria, String marca, String stock,
			String unidadMedida, String estado) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.marca = marca;
		this.stock = stock;
		this.unidadMedida = unidadMedida;
		this.estado = estado;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getCategoria() {
		return categoria;
	}

	public String getMarca() {
		return marca;
	}

	public String getStock() {
		return stock;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public String getEstado() {
		return estado;
	}

}
