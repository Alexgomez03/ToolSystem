package modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity(name = "tb_compra_detalle")
public class DetalleCompraModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private Double cantidad;

	// Precio de costo unitario al momento de la compra (puede diferir del
	// precio de venta cargado en ProductoModelo)
	@Column(nullable = false)
	private Double precioCosto;

	// Muchos a UNO

	@ManyToOne
	private ProductoModelo producto;

	@ManyToOne
	private CompraModelo compra;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getPrecioCosto() {
		return precioCosto;
	}

	public void setPrecioCosto(Double precioCosto) {
		this.precioCosto = precioCosto;
	}

	public ProductoModelo getProducto() {
		return producto;
	}

	public void setProducto(ProductoModelo producto) {
		this.producto = producto;
	}

	public CompraModelo getCompra() {
		return compra;
	}

	public void setCompra(CompraModelo compra) {
		this.compra = compra;
	}

}
