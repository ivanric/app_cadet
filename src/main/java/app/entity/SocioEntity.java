package app.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

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

@Entity
@Table(name = "socio") // el email sera unico
public class SocioEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id 
	private Integer id; 
	
	@Column(name = "codigo")
	private Integer codigo; 
	
	@Column(name = "nrodocumento")
	private String nrodocumento; 
	
	@Column(name = "imagen") 
	private String imagen;
	
	@Column(name = "imagenDriveId") 
	private String imagenDriveId;
	
	
	@Column(name = "qr")
	private String qr;
	@Column(name = "linkqr")
	private String linkqr;
	
	@Column(name = "matricula")
	private String matricula;
	@Column(name = "nombresocio")
	private String nombresocio;
	
//	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "fechaemision")
	@DateTimeFormat(iso =ISO.DATE)
	private LocalDate fechaemision;
	
//	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "fechaexpiracion")
	@DateTimeFormat(iso =ISO.DATE)
	private LocalDate fechaexpiracion;
	 
	
	@Column(name = "lejendario")
	private Integer lejendario;
	
	@Column(name = "estado")
	private Integer estado;

	
    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_profesion")
    private ProfesionEntity profesion;
    
    @OneToOne
    @JoinColumn(name = "fk_persona")
    private PersonaEntity persona;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_institucion")
    private InstitucionEntity institucion;

    
    
	@ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE) //FetchType.EAGER =lo usaremos cuando lo necesitamos
	@JoinTable(
			name="socio_catalogos",//crearemos una tabla intermedia+
			joinColumns = @JoinColumn(name="socio_id",referencedColumnName = "id"),
					inverseJoinColumns = {@JoinColumn(name="catalogo_id",referencedColumnName = "id")}
			)
	private Collection<CatalogoEntity> catalogos;
	
	
	
	@Transient//para que no se guarde en la bd, para que sea tempora
//	@NotNull(message = "Por favor, seleccione un archivo")
	private MultipartFile logo;
	
	public SocioEntity() {
		super();
		// TODO Auto-generated constructor stub 
	}

	public SocioEntity(Integer id, Integer codigo, String nrodocumento, String imagen, String imagenDriveId, String qr,
			String linkqr, String matricula, String nombresocio, LocalDate fechaemision, LocalDate fechaexpiracion,
			Integer lejendario, Integer estado, ProfesionEntity profesion, PersonaEntity persona,
			InstitucionEntity institucion, Collection<CatalogoEntity> catalogos, MultipartFile logo) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nrodocumento = nrodocumento;
		this.imagen = imagen;
		this.imagenDriveId = imagenDriveId;
		this.qr = qr;
		this.linkqr = linkqr;
		this.matricula = matricula;
		this.nombresocio = nombresocio;
		this.fechaemision = fechaemision;
		this.fechaexpiracion = fechaexpiracion;
		this.lejendario = lejendario;
		this.estado = estado;
		this.profesion = profesion;
		this.persona = persona;
		this.institucion = institucion;
		this.catalogos = catalogos;
		this.logo = logo;
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

	public String getNrodocumento() {
		return nrodocumento;
	}

	public void setNrodocumento(String nrodocumento) {
		this.nrodocumento = nrodocumento;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getImagenDriveId() {
		return imagenDriveId;
	}

	public void setImagenDriveId(String imagenDriveId) {
		this.imagenDriveId = imagenDriveId;
	}

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	public String getLinkqr() {
		return linkqr;
	}

	public void setLinkqr(String linkqr) {
		this.linkqr = linkqr;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNombresocio() {
		return nombresocio;
	}

	public void setNombresocio(String nombresocio) {
		this.nombresocio = nombresocio;
	}

	public LocalDate getFechaemision() {
		return fechaemision;
	}

	public void setFechaemision(LocalDate fechaemision) {
		this.fechaemision = fechaemision;
	}

	public LocalDate getFechaexpiracion() {
		return fechaexpiracion;
	}

	public void setFechaexpiracion(LocalDate fechaexpiracion) {
		this.fechaexpiracion = fechaexpiracion;
	}

	public Integer getLejendario() {
		return lejendario;
	}

	public void setLejendario(Integer lejendario) {
		this.lejendario = lejendario;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public ProfesionEntity getProfesion() {
		return profesion;
	}

	public void setProfesion(ProfesionEntity profesion) {
		this.profesion = profesion;
	}

	public PersonaEntity getPersona() {
		return persona;
	}

	public void setPersona(PersonaEntity persona) {
		this.persona = persona;
	}

	public InstitucionEntity getInstitucion() {
		return institucion;
	}

	public void setInstitucion(InstitucionEntity institucion) {
		this.institucion = institucion;
	}

	public Collection<CatalogoEntity> getCatalogos() {
		return catalogos;
	}

	public void setCatalogos(Collection<CatalogoEntity> catalogos) {
		this.catalogos = catalogos;
	}

	public MultipartFile getLogo() {
		return logo;
	}

	public void setLogo(MultipartFile logo) {
		this.logo = logo;
	}

	@Override
	public String toString() {
		return "SocioEntity [id=" + id + ", codigo=" + codigo + ", nrodocumento=" + nrodocumento + ", imagen=" + imagen
				+ ", imagenDriveId=" + imagenDriveId + ", qr=" + qr + ", linkqr=" + linkqr + ", matricula=" + matricula
				+ ", nombresocio=" + nombresocio + ", fechaemision=" + fechaemision + ", fechaexpiracion="
				+ fechaexpiracion + ", lejendario=" + lejendario + ", estado=" + estado + ", profesion=" + profesion
				+ ", persona=" + persona + ", institucion=" + institucion + ", catalogos=" + catalogos + ", logo="
				+ logo + "]";
	}

	

}
