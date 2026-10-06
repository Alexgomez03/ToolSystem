package reportes;

// Lo mismo que CuentaPorCobrarDTO, pero para el informe "Cuentas por
// Pagar": compras a crédito que todavía se le deben a un proveedor.
public class CuentaPorPagarDTO {

	private Integer compraId;
	private String fecha;
	private String proveedor;
	private String total;
	private String pagado;
	private String saldo;
	private String estado;

	public CuentaPorPagarDTO(Integer compraId, String fecha, String proveedor, String total, String pagado,
			String saldo, String estado) {
		this.compraId = compraId;
		this.fecha = fecha;
		this.proveedor = proveedor;
		this.total = total;
		this.pagado = pagado;
		this.saldo = saldo;
		this.estado = estado;
	}

	public Integer getCompraId() {
		return compraId;
	}

	public String getFecha() {
		return fecha;
	}

	public String getProveedor() {
		return proveedor;
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
