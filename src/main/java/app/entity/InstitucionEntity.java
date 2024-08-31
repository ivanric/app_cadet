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
import app.entity.ProvinciaEntity;

@Entity
@Table(name="institucion")
public class InstitucionEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Integer id; 

	@Column(name = "nit")
	private String nit;
	
	@Column(name = "compania")
	private String compania;
	
	@Column(name = "institucion")
	private String institucion;
	
	@Column(name = "representante")
	private String representante;
	
	@Column(name = "correo")
	private String correo;
	
	@Column(name = "direccion")
	private String direccion;
	
	@Column(name = "telefono")
	private String telefono;
	
	@Column(name = "fax")
	private String fax;
	
	@Column(name = "host")
	private String host;
	
	@Column(name = "port")
	private String port;
	
	
	@Column(name = "estado")
	private Integer estado;
	
    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_provincia")
    private ProvinciaEntity provincia;

    
    
	public InstitucionEntity() {
		super();
		// TODO Auto-generated constructor stub
	}



	public InstitucionEntity(Integer id, String nit, String compania, String institucion, String representante,
			String correo, String direccion, String telefono, String fax, String host, String port, Integer estado,
			ProvinciaEntity provincia) {
		super();
		this.id = id;
		this.nit = nit;
		this.compania = compania;
		this.institucion = institucion;
		this.representante = representante;
		this.correo = correo;
		this.direccion = direccion;
		this.telefono = telefono;
		this.fax = fax;
		this.host = host;
		this.port = port;
		this.estado = estado;
		this.provincia = provincia;
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getNit() {
		return nit;
	}



	public void setNit(String nit) {
		this.nit = nit;
	}



	public String getCompania() {
		return compania;
	}



	public void setCompania(String compania) {
		this.compania = compania;
	}



	public String getInstitucion() {
		return institucion;
	}



	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}



	public String getRepresentante() {
		return representante;
	}



	public void setRepresentante(String representante) {
		this.representante = representante;
	}



	public String getCorreo() {
		return correo;
	}



	public void setCorreo(String correo) {
		this.correo = correo;
	}



	public String getDireccion() {
		return direccion;
	}



	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}



	public String getTelefono() {
		return telefono;
	}



	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}



	public String getFax() {
		return fax;
	}



	public void setFax(String fax) {
		this.fax = fax;
	}



	public String getHost() {
		return host;
	}



	public void setHost(String host) {
		this.host = host;
	}



	public String getPort() {
		return port;
	}



	public void setPort(String port) {
		this.port = port;
	}



	public Integer getEstado() {
		return estado;
	}



	public void setEstado(Integer estado) {
		this.estado = estado;
	}



	public ProvinciaEntity getProvincia() {
		return provincia;
	}



	public void setProvincia(ProvinciaEntity provincia) {
		this.provincia = provincia;
	}



	@Override
	public String toString() {
		return "InstitucionEntity [id=" + id + ", nit=" + nit + ", compania=" + compania + ", institucion="
				+ institucion + ", representante=" + representante + ", correo=" + correo + ", direccion=" + direccion
				+ ", telefono=" + telefono + ", fax=" + fax + ", host=" + host + ", port=" + port + ", estado=" + estado
				+ ", provincia=" + provincia + "]";
	}

	
    
}
