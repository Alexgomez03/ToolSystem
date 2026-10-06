package modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


 // Registro de Kardex: cada entrada o salida de stock de un producto,
 // generada automáticamente por Compras (ENTRADA) o Ventas (SALIDA).
 // Es un historial de solo lectura, no se edita ni elimina desde la UI.
 
@Entity(name = "tb_stock_movimientos")
public class StockMovimientoModelo {

	public enum TipoMovimiento {
		ENTRADA, SALIDA
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private LocalDateTime fecha;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoMovimiento tipo;

	@Column(nullable = false)
	private Double cantidad;

	// Stock del producto luego de aplicado este movimiento
	@Column(nullable = false)
	private Double stockResultante;

	// Referencia legible del origen del movimiento (p. ej. "Compra N° 12")
	@Column
	private String motivo;

	// Muchos a UNO

	@ManyToOne
	private ProductoModelo producto;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public TipoMovimiento getTipo() {
		return tipo;
	}

	public void setTipo(TipoMovimiento tipo) {
		this.tipo = tipo;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getStockResultante() {
		return stockResultante;
	}

	public void setStockResultante(Double stockResultante) {
		this.stockResultante = stockResultante;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public ProductoModelo getProducto() {
		return producto;
	}

	public void setProducto(ProductoModelo producto) {
		this.producto = producto;
	}

}
