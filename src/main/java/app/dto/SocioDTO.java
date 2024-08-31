package app.dto;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SocioDTO {
	
	private Integer id; 
	private Integer codigo; 
	private String nrodocumento; 
	private String imagen;
	private String qr;
	private String linkqr;
	private String matricula;
	private String nombresocio;
	@DateTimeFormat(iso =ISO.DATE)
	private LocalDate fechaemision;
	@DateTimeFormat(iso =ISO.DATE)
	private LocalDate fechaexpiracion;
	private Integer lejendario;
	
	

	private String ci;
	private String nombrecompleto;
	private String email;
	private Integer celular;
	@Transient//para que no se guarde en la bd, para que sea tempora
//	@NotNull(message = "Por favor, seleccione un archivo")
	private MultipartFile logo;
	public SocioDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SocioDTO(Integer id, Integer codigo, String nrodocumento, String imagen, String qr, String linkqr,
			String matricula, String nombresocio, LocalDate fechaemision, LocalDate fechaexpiracion, Integer lejendario,
			String ci, String nombrecompleto, String email, Integer celular, MultipartFile logo) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nrodocumento = nrodocumento;
		this.imagen = imagen;
		this.qr = qr;
		this.linkqr = linkqr;
		this.matricula = matricula;
		this.nombresocio = nombresocio;
		this.fechaemision = fechaemision;
		this.fechaexpiracion = fechaexpiracion;
		this.lejendario = lejendario;
		this.ci = ci;
		this.nombrecompleto = nombrecompleto;
		this.email = email;
		this.celular = celular;
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
	public MultipartFile getLogo() {
		return logo;
	}
	public void setLogo(MultipartFile logo) {
		this.logo = logo;
	}
	@Override
	public String toString() {
		return "SocioDTO [id=" + id + ", codigo=" + codigo + ", nrodocumento=" + nrodocumento + ", imagen=" + imagen
				+ ", qr=" + qr + ", linkqr=" + linkqr + ", matricula=" + matricula + ", nombresocio=" + nombresocio
				+ ", fechaemision=" + fechaemision + ", fechaexpiracion=" + fechaexpiracion + ", lejendario="
				+ lejendario + ", ci=" + ci + ", nombrecompleto=" + nombrecompleto + ", email=" + email + ", celular="
				+ celular + ", logo=" + logo + "]";
	}
	
	
}
