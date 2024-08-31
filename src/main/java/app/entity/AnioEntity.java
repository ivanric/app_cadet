
package app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "anio")
public class AnioEntity extends GenericEntity {

	private static final long serialVersionUID = 1L;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "estado")
	private Integer estado;
	
    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_institucion")
    private InstitucionEntity institucion;
	
	public AnioEntity() {
//		super();
		// TODO Auto-generated constructor stub
	}

	public AnioEntity(String nombre, Integer estado, InstitucionEntity institucion) {
		super();
		this.nombre = nombre;
		this.estado = estado;
		this.institucion = institucion;
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

	public InstitucionEntity getInstitucion() {
		return institucion;
	}

	public void setInstitucion(InstitucionEntity institucion) {
		this.institucion = institucion;
	}

}
