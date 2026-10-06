package reportes;


 //Fila plana para el reporte "Compras por Período" (ver jasper/compras_periodo.jrxml)
 
public class CompraReporteDTO {

	private Integer id;
	private String fecha;
	private String proveedor;
	private String nroFactura;
	private String estado;
	private String total;

	public CompraReporteDTO(Integer id, String fecha, String proveedor, String nroFactura, String estado,
			String total) {
		this.id = id;
		this.fecha = fecha;
		this.proveedor = proveedor;
		this.nroFactura = nroFactura;
		this.estado = estado;
		this.total = total;
	}

	public Integer getId() {
		return id;
	}

	public String getFecha() {
		return fecha;
	}

	public String getProveedor() {
		return proveedor;
	}

	public String getNroFactura() {
		return nroFactura;
	}

	public String getEstado() {
		return estado;
	}

	public String getTotal() {
		return total;
	}

}
