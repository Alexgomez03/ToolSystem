package modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity(name = "tb_productos")
public class ProductoModelo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String descripcion;
	
	@Column(nullable = false, unique = true)
	private String codigo;
	
	@Column(nullable = false)
	private Double precioVenta;
	
	@Column(nullable = false)
	private Double stock;
	
	@Column(nullable = false)
	private String unidadMedida;
	
	@Column
	private Boolean estado;
	
	//==== Muchos a UNO =====
	
	@ManyToOne
	private CategoriaModelo categoria;
	
	@ManyToOne
	private MarcaModelo marca;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(Double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public CategoriaModelo getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaModelo categoria) {
		this.categoria = categoria;
	}

	public MarcaModelo getMarca() {
		return marca;
	}

	public void setMarca(MarcaModelo marca) {
		this.marca = marca;
	}
}
