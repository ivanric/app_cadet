package app.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "catalogo") // el email sera unico
public class CatalogoEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id 
	private Integer id; 
	
	@Column(name = "codigo")
	private Integer codigo; 
	
	@Column(name = "nit")
	private String nit;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "descripcion",length = 800)
	private String descripcion;
	
	@Column(name = "direccion")
	private String direccion;
	
	@Column(name = "descuento")
	private String descuento;
	
	@Column(name = "tipo")
	private String tipo;
	
	@Column(name = "nombrelogo")    
	private String nombrelogo;
	
	@Column(name = "nombrelogoDriveId")    
	private String nombrelogoDriveId;
	
	@Column(name = "longitud")    
	private String longitud;
	
	@Column(name = "latitud")    
	private String latitud;
	
	@Column(name = "estado")
	private Integer estado;

	@OneToMany(cascade = CascadeType.REFRESH)//CascadeType.REFRESH,cuando hay un cambio en un autor se debe actualizar en un libro
	@JoinColumn(name = "fk_catalogo")
	private List<ImagenesCatalogoEntity> imagenesCatalogos=new ArrayList<>();;

	

	
	@Transient//para que no se guarde en la bd, para que sea tempora
//	@NotNull(message = "Por favor, seleccione un archivo")
	private MultipartFile logo;

	@Transient//para que no se guarde en la bd, para que sea tempora
	private List<MultipartFile> catalogo;
	
	public CatalogoEntity() {
		super();
		// TODO Auto-generated constructor stub 
	}

	public CatalogoEntity(Integer id, Integer codigo, String nit, String nombre, String descripcion, String direccion,
			String descuento, String tipo, String nombrelogo, String nombrelogoDriveId, String longitud, String latitud,
			Integer estado, List<ImagenesCatalogoEntity> imagenesCatalogos, MultipartFile logo,
			List<MultipartFile> catalogo) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nit = nit;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.direccion = direccion;
		this.descuento = descuento;
		this.tipo = tipo;
		this.nombrelogo = nombrelogo;
		this.nombrelogoDriveId = nombrelogoDriveId;
		this.longitud = longitud;
		this.latitud = latitud;
		this.estado = estado;
		this.imagenesCatalogos = imagenesCatalogos;
		this.logo = logo;
		this.catalogo = catalogo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombrelogo() {
		return nombrelogo;
	}

	public void setNombrelogo(String nombrelogo) {
		this.nombrelogo = nombrelogo;
	}

	public String getNombrelogoDriveId() {
		return nombrelogoDriveId;
	}

	public void setNombrelogoDriveId(String nombrelogoDriveId) {
		this.nombrelogoDriveId = nombrelogoDriveId;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public List<ImagenesCatalogoEntity> getImagenesCatalogos() {
		return imagenesCatalogos;
	}

	public void setImagenesCatalogos(List<ImagenesCatalogoEntity> imagenesCatalogos) {
		this.imagenesCatalogos = imagenesCatalogos;
	}

	public MultipartFile getLogo() {
		return logo;
	}

	public void setLogo(MultipartFile logo) {
		this.logo = logo;
	}

	public List<MultipartFile> getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(List<MultipartFile> catalogo) {
		this.catalogo = catalogo;
	}
	
	
}
