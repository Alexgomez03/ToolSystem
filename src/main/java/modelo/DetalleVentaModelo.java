package modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity(name = "tb_venta_detalle")
public class DetalleVentaModelo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private Double cantidad;
	
	@Column(nullable = false)
	private Double precio;
	
	// Muchos a UNO
	
	@ManyToOne
	private ProductoModelo producto;
	
	@ManyToOne
	private VentaModelo venta;

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

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public ProductoModelo getProducto() {
		return producto;
	}

	public void setProducto(ProductoModelo producto) {
		this.producto = producto;
	}

	public VentaModelo getVenta() {
		return venta;
	}

	public void setVenta(VentaModelo venta) {
		this.venta = venta;
	}

}
