package reportes;


 // Fila plana para el reporte "Ventas por Período" (ver
 // /jasper/ventas_periodo.jrxml). Se usa un DTO en vez de pasarle
 // directamente la lista de {@code VentaModelo} a Jasper porque
 // JRBeanCollectionDataSource solo resuelve propiedades de primer nivel del
 // bean (no soporta expresiones anidadas como "cliente.nombre"), y acá
 // necesitamos mostrar datos que en el modelo real están en objetos
 // relacionados (Cliente, Funcionario).
 
public class VentaReporteDTO {

	private Integer id;
	private String fecha;
	private String cliente;
	private String vendedor;
	private String estado;
	private String total;

	public VentaReporteDTO(Integer id, String fecha, String cliente, String vendedor, String estado, String total) {
		this.id = id;
		this.fecha = fecha;
		this.cliente = cliente;
		this.vendedor = vendedor;
		this.estado = estado;
		this.total = total;
	}

	public Integer getId() {
		return id;
	}

	public String getFecha() {
		return fecha;
	}

	public String getCliente() {
		return cliente;
	}

	public String getVendedor() {
		return vendedor;
	}

	public String getEstado() {
		return estado;
	}

	public String getTotal() {
		return total;
	}

}
