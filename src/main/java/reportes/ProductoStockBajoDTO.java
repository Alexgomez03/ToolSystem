package reportes;


 // Fila plana para el reporte "Stock Bajo" (ver /jasper/stock_bajo.jrxml).
 // Mismo motivo que {@link VentaReporteDTO}: aplanar las relaciones
 // (Categoría, Marca) a texto para que Jasper las pueda resolver.
 
public class ProductoStockBajoDTO {

	private String codigo;
	private String descripcion;
	private String categoria;
	private String marca;
	private String stock;
	private String stockMinimo;
	private String unidadMedida;

	public ProductoStockBajoDTO(String codigo, String descripcion, String categoria, String marca, String stock,
			String stockMinimo, String unidadMedida) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.marca = marca;
		this.stock = stock;
		this.stockMinimo = stockMinimo;
		this.unidadMedida = unidadMedida;
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

	public String getStockMinimo() {
		return stockMinimo;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

}
