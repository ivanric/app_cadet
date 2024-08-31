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
@Table(name="persona")
public class PersonaEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id 
	private Integer id; 
	@Column(name = "ci")
	private String ci;
	@Column(name = "nombrecompleto")
	private String nombrecompleto;
	@Column(name = "email")
	private String email;
	
	@Column(name = "celular")
	private Integer celular;
	
	@Column(name = "estado")
	private Integer estado;
	public PersonaEntity() {
//		super();
		// TODO Auto-generated constructor stub
	}
	public PersonaEntity(Integer id, String ci, String nombrecompleto, String email, Integer celular, Integer estado) {
		super();
		this.id = id;
		this.ci = ci;
		this.nombrecompleto = nombrecompleto;
		this.email = email;
		this.celular = celular;
		this.estado = estado;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCi() {
		return ci;
	}
	public void setCi(String ci) {
		this.ci = ci;
	}
	public String getNombrecompleto() {
		return nombrecompleto;
	}
	public void setNombrecompleto(String nombrecompleto) {
		this.nombrecompleto = nombrecompleto;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getCelular() {
		return celular;
	}
	public void setCelular(Integer celular) {
		this.celular = celular;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	@Override
	public String toString() {
		return "PersonaEntity [id=" + id + ", ci=" + ci + ", nombrecompleto=" + nombrecompleto + ", email=" + email
				+ ", celular=" + celular + ", estado=" + estado + "]";
	}
	
	
}
