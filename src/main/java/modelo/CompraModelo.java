package modelo;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "tb_compras")
public class CompraModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private LocalDate fecha;

	@Column
	private String nroFactura;

	@Column(nullable = false)
	private Double total;

	@Column
	private LocalDate fechaRegistro;

	@Column
	private Boolean anulada;

	@Column(columnDefinition = "TEXT")
	private String observacion;

	// Si esta compra se pagó toda de una vez (CONTADO) o quedó como
	// una deuda con el proveedor para pagar más adelante (CREDITO).
	@Column
	@Enumerated(EnumType.STRING)
	private FormaPago formaPago;

	// Cuánto se le pagó al proveedor hasta ahora. Al CONTADO se carga
	// automáticamente igual al total; a CREDITO arranca en 0 y se va
	// actualizando a medida que se le va pagando.
	@Column
	private Double montoPagado;

	// === Muchos a Uno

	@ManyToOne
	private ProveedorModelo proveedor;

	// Uno a Muchos

	@OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<DetalleCompraModelo> detalles;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Boolean getAnulada() {
		return anulada;
	}

	public void setAnulada(Boolean anulada) {
		this.anulada = anulada;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public ProveedorModelo getProveedor() {
		return proveedor;
	}

	public void setProveedor(ProveedorModelo proveedor) {
		this.proveedor = proveedor;
	}

	public List<DetalleCompraModelo> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleCompraModelo> detalles) {
		this.detalles = detalles;
	}

	public FormaPago getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(FormaPago formaPago) {
		this.formaPago = formaPago;
	}

	public Double getMontoPagado() {
		return montoPagado;
	}

	public void setMontoPagado(Double montoPagado) {
		this.montoPagado = montoPagado;
	}

	// Igual que en VentaModelo: se calcula al vuelo, no se guarda en
	// la base. Al contado siempre da "Pagado"; un registro viejo sin
	// forma de pago cargada también se muestra como "Pagado".
	public EstadoPago getEstadoPago() {
		if (formaPago == null || formaPago == FormaPago.CONTADO)
			return EstadoPago.PAGADO;

		double pagado = montoPagado != null ? montoPagado : 0.0;
		double totalCompra = total != null ? total : 0.0;

		if (pagado <= 0)
			return EstadoPago.PENDIENTE;
		if (pagado >= totalCompra)
			return EstadoPago.PAGADO;
		return EstadoPago.PAGO_PARCIAL;
	}

}
