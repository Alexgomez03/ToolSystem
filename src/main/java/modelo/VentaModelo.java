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

@Entity(name = "tb_ventas")
public class VentaModelo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private LocalDate fecha;
	
	@Column(nullable = false)
	private Double total;
	
	@Column
	private LocalDate fechaRegistro;
	
	@Column
	private Boolean anulada;
	
	@Column(columnDefinition = "TEXT")
	private String observacion;

	// Si esta venta se cobró toda de una vez (CONTADO) o quedó como
	// una deuda del cliente para pagar más adelante (CREDITO).
	@Column
	@Enumerated(EnumType.STRING)
	private FormaPago formaPago;

	// Cuánto pagó el cliente hasta ahora. Cuando la venta es al
	// CONTADO, esto se carga automáticamente igual al total (se
	// considera pagada por completo). Cuando es a CREDITO, arranca en
	// 0 y se va actualizando a medida que el cliente va pagando.
	@Column
	private Double montoPagado;
	
	// === Muchos a Uno
	
	@ManyToOne
	private ClienteModelo cliente;
	
	@ManyToOne
	private FuncionarioModelo funcionario;
	
	// Uno a Muchos
	
	@OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<DetalleVentaModelo> detalles;

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

	public ClienteModelo getCliente() {
		return cliente;
	}

	public void setCliente(ClienteModelo cliente) {
		this.cliente = cliente;
	}

	public FuncionarioModelo getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioModelo funcionario) {
		this.funcionario = funcionario;
	}

	public List<DetalleVentaModelo> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleVentaModelo> detalles) {
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

	// Esto no es un dato que se guarda en la base: se calcula cada vez
	// que se pide, comparando cuánto se pagó contra el total de la
	// venta. Una venta al contado siempre da "Pagado" (se considera
	// cobrada en el momento). Los registros viejos, de antes de agregar
	// esto, no tienen forma de pago cargada y también se muestran como
	// "Pagado" (contado), para no salir con datos raros de la nada.
	public EstadoPago getEstadoPago() {
		if (formaPago == null || formaPago == FormaPago.CONTADO)
			return EstadoPago.PAGADO;

		double pagado = montoPagado != null ? montoPagado : 0.0;
		double totalVenta = total != null ? total : 0.0;

		if (pagado <= 0)
			return EstadoPago.PENDIENTE;
		if (pagado >= totalVenta)
			return EstadoPago.PAGADO;
		return EstadoPago.PAGO_PARCIAL;
	}

}
