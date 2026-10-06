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


 // Usuario del sistema (login). Se separa de {@link FuncionarioModelo} porque
 // no todo funcionario necesita una cuenta para entrar al sistema, y porque
 // mezclar credenciales con datos de personal (cargo, fecha de ingreso, etc.)
 // en una sola entidad complica tanto el ABM de Funcionarios como la lógica
 // de login. La relación con Funcionario es opcional: permite, por ejemplo,
 // saber qué persona real está detrás de un usuario a efectos de reportes,
 // pero un usuario "de sistema" puede existir sin funcionario asociado.
 
@Entity(name = "tb_usuarios")
public class UsuarioModelo {

	public enum Rol {
		ADMINISTRADOR, VENDEDOR
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, unique = true, length = 50)
	private String usuario;

	@Column(nullable = false)
	private String passwordHash;

	@Column(nullable = false)
	private String salt;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Rol rol;

	@Column
	private Boolean estado;

	@ManyToOne
	private FuncionarioModelo funcionario;

	@Column
	private LocalDateTime ultimoAcceso;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public FuncionarioModelo getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioModelo funcionario) {
		this.funcionario = funcionario;
	}

	public LocalDateTime getUltimoAcceso() {
		return ultimoAcceso;
	}

	public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
		this.ultimoAcceso = ultimoAcceso;
	}

	// Nombre para mostrar en pantalla: el del funcionario si tiene uno vinculado, si no el nombre de usuario. 
	public String getNombreParaMostrar() {
		if (funcionario != null)
			return funcionario.getNombre() + " " + funcionario.getApellido();
		return usuario;
	}

}
