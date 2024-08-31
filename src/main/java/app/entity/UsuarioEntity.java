package app.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "usuario", uniqueConstraints = {@UniqueConstraint(columnNames = "username")}) // el email sera unico
public class UsuarioEntity  implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	private Integer id; 
	private String username;
	private String password; 
	private Integer estado;
	
	
	@ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE) //FetchType.EAGER =lo usaremos cuando lo necesitamos

	@JoinTable(
			name="usuarios_roles",//crearemos una tabla intermedia+
			joinColumns = @JoinColumn(name="usuario_id",referencedColumnName = "id"),
					inverseJoinColumns = {@JoinColumn(name="rol_id",referencedColumnName = "id")}
			)
	
	private Collection<RolEntity> roles;
//	private Set<Rol> roles;
	
	
    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_persona")
    private PersonaEntity persona;
	
	public UsuarioEntity() {
//		super();
	}

	public UsuarioEntity(Integer id, String username, String password, Integer estado, Collection<RolEntity> roles,
			PersonaEntity persona) {
		super(); 
		this.id = id;
		this.username = username;
		this.password = password;
		this.estado = estado;
		this.roles = roles;
		this.persona = persona;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public Collection<RolEntity> getRoles() {
		return roles;
	}

	public void setRoles(Collection<RolEntity> roles) {
		this.roles = roles;
	}

	public PersonaEntity getPersona() {
		return persona;
	}

	public void setPersona(PersonaEntity persona) {
		this.persona = persona;
	}

	
	 

}
