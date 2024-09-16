
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
@Table(name = "imagencatalogo")
public class ImagenesCatalogoEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private Integer id; 
	@Column(name = "codigo")
	private Integer codigo; 
	@Column(name = "imagen")
	private String imagen;
	
	
	@Column(name = "imagenDriveId")
	private String imagenDriveId;
	@Column(name = "estado")
	private Integer estado; 
	
	@Transient //importante para que no cargue una compra previa
    @ManyToOne(cascade = CascadeType.REFRESH)//para refrescar
    @JoinColumn(name = "fk_catalogo")  
    private CatalogoEntity catalogo;

	public ImagenesCatalogoEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ImagenesCatalogoEntity(Integer id, Integer codigo, String imagen, String imagenDriveId, Integer estado,
			CatalogoEntity catalogo) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.imagen = imagen;
		this.imagenDriveId = imagenDriveId;
		this.estado = estado;
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

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public CatalogoEntity getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(CatalogoEntity catalogo) {
		this.catalogo = catalogo;
	} 
	
	
}
