package reportes;

// Una fila del informe "Cuentas por Cobrar" (ver
// /jasper/cuentas_por_cobrar.jrxml): una venta a crédito que todavía
// no está pagada del todo, con cuánto falta cobrar (el "saldo").
public class CuentaPorCobrarDTO {

	private Integer ventaId;
	private String fecha;
	private String cliente;
	private String total;
	private String pagado;
	private String saldo;
	private String estado;

	public CuentaPorCobrarDTO(Integer ventaId, String fecha, String cliente, String total, String pagado,
			String saldo, String estado) {
		this.ventaId = ventaId;
		this.fecha = fecha;
		this.cliente = cliente;
		this.total = total;
		this.pagado = pagado;
		this.saldo = saldo;
		this.estado = estado;
	}

	public Integer getVentaId() {
		return ventaId;
	}

	public String getFecha() {
		return fecha;
	}

	public String getCliente() {
		return cliente;
	}

	public String getTotal() {
		return total;
	}

	public String getPagado() {
		return pagado;
	}

	public String getSaldo() {
		return saldo;
	}

	public String getEstado() {
		return estado;
	}

}
