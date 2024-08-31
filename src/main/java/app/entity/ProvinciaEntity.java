package app.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="provincia")
public class ProvinciaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 
	@Id
	private Integer id; 
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "estado")
	private Integer estado;
	
    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_departamento")
    private DepartamentoEntity departamento;

	public ProvinciaEntity() {
//		super();
		// TODO Auto-generated constructor stub
	}

	public ProvinciaEntity(Integer id, String nombre, Integer estado, DepartamentoEntity departamento) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.estado = estado;
		this.departamento = departamento;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public DepartamentoEntity getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DepartamentoEntity departamento) {
		this.departamento = departamento;
	}

	@Override
	public String toString() {
		return "ProvinciaEntity [id=" + id + ", nombre=" + nombre + ", estado=" + estado + ", departamento="
				+ departamento + "]";
	}

	
    
}
