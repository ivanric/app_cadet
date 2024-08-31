package app.entity;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="departamento")
public class DepartamentoEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 
	@Id
	private Integer id; 
	
	@Column(name = "abreviacion")
	private String abreviacion;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "estado")
	private Integer estado;
	
    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_pais")
    private PaisEntity pais;

	public DepartamentoEntity() {
//		super();
		// TODO Auto-generated constructor stub
	}

	public DepartamentoEntity(Integer id, String abreviacion, String nombre, Integer estado, PaisEntity pais) {
		super();
		this.id = id;
		this.abreviacion = abreviacion;
		this.nombre = nombre;
		this.estado = estado;
		this.pais = pais;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAbreviacion() {
		return abreviacion;
	}

	public void setAbreviacion(String abreviacion) {
		this.abreviacion = abreviacion;
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

	public PaisEntity getPais() {
		return pais;
	}

	public void setPais(PaisEntity pais) {
		this.pais = pais;
	}

	@Override
	public String toString() {
		return "DepartamentoEntity [id=" + id + ", abreviacion=" + abreviacion + ", nombre=" + nombre + ", estado="
				+ estado + ", pais=" + pais + "]";
	}

	
}
