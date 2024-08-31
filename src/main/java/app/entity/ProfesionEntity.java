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
@Table(name="profesion")
public class ProfesionEntity implements Serializable{

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

	public ProfesionEntity() {
//		super();
		// TODO Auto-generated constructor stub
	}

	public ProfesionEntity(Integer id, String nombre, Integer estado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.estado = estado;
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

	@Override
	public String toString() {
		return "ProfesionEntity [id=" + id + ", nombre=" + nombre + ", estado=" + estado + "]";
	}

	
}
