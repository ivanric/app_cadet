package app.entity;

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
@Table(name="rol")
public class RolEntity  extends GenericEntity {


	private static final long serialVersionUID = 1L;
	private String nombre;
	private int estado;
	
//	@ManyToOne(cascade = {CascadeType.MERGE},fetch= FetchType.EAGER)
//	@JoinColumn(name = "usuario_id")
//    private UsuarioEntity usuario; 
	 
	

	public RolEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public RolEntity(String nombre, int estado) {
	super();
	this.nombre = nombre;
	this.estado = estado;
}

	public RolEntity(String nombre) {
		super();
		this.nombre = nombre;
	}
	public RolEntity(Long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
 

	
	


	
	
	
}
