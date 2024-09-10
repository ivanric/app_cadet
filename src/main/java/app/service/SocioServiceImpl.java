package app.service;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.entity.InstitucionEntity;
import app.entity.PersonaEntity;
import app.entity.SocioEntity;
import app.repository.CatalogoRepository;
import app.repository.GenericRepositoryNormal;
import app.repository.InstitucionRepository;
import app.repository.ProfesionRepository;
import app.repository.SocioRepository;
import app.util.Constantes;
import app.util.QRCodeGeneratorService;

@Service
public class SocioServiceImpl extends GenericServiceImplNormal<SocioEntity, Integer> implements SocioService {

	@Autowired private SocioRepository socioRepository;
	@Autowired private CatalogoRepository catalogoRepository;
	@Autowired private InstitucionRepository institucionRepository;
	@Autowired private ProfesionRepository profesionRepository;
	@Autowired private PersonaService personaService;
	@Autowired private BCryptPasswordEncoder passwordEncoder;
	@Autowired private ArchivoService archivoService;
	@Autowired
	QRCodeGeneratorService qrCodeGeneratorService;
//	private final String imagePath = "./src/main/resources/static/qrcodes/QRCode.png";
//	private  String imagePath = "./src/main/resources/static/qrcodes/";
	
	@Value("${server.port}")
    private static String puertoservidor;
	

	

    private String IPPUBLICA = ""; // Configura tu IP pública manualmente
    private String PORT= ""; // ejemplo de puerto
    
    
	SocioServiceImpl(GenericRepositoryNormal<SocioEntity, Integer> genericRepository) {
		super(genericRepository);
		// TODO Auto-generated constructor stub
	}

	
	//sin el @Override porque hereda de
	public int getIdPrimaryKey() throws Exception {
		
        try{
        	int id = socioRepository.getIdPrimaryKey();
          return id;
      } catch (Exception e){
      		System.out.println(e.getMessage());
//          throw new Exception(e.getMessage());
          return 0;
      }
	}
	@Override
	public Integer getCodigo() throws Exception {
		
        try{
        	int total = socioRepository.getCodigo();
          return total;
      } catch (Exception e){
      		System.out.println(e.getMessage());
//          throw new Exception(e.getMessage());
          return 0;
      }
	}
	

	@Override
	@Transactional
	public List<SocioEntity> findAll( int estado,String search,int length,int start ) throws Exception {
        try{
//            List<PaisEntity> entities = paisRepository.findAll(Sort.by("idpais").ascending());
            List<SocioEntity> entities = socioRepository.findAll(estado, search, length, start);
            return entities;
        } catch (Exception e){
        	System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
	}
	

	@Override 
	@Transactional
	public void updateStatus(int status, int id) throws Exception {
		// TODO Auto-generated method stub
        try{
        	System.out.println("estado:"+status+" id:"+id);
        	socioRepository.updateStatus(status,id);

        } catch (Exception e){
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            throw new Exception(e.getMessage());
        }
	}
	
	@Override
	public Integer getTotAll(String search,int estado) throws Exception {
		
        try{
        	int total = socioRepository.getTotAll(search, estado);
          return total;
      } catch (Exception e){
      		System.out.println(e.getMessage());
//          throw new Exception(e.getMessage());
          return 0;
      }
	}


    @Override
    @Transactional
    public SocioEntity save(SocioEntity entity) throws Exception {
        try{
        	System.out.println("EntitySAVE_Servicio:"+entity.toString());
        	
        	System.out.println("IMAGEN:"+entity.getLogo().getOriginalFilename());
        	
        	//ADD PERSONA
        	
        	PersonaEntity persona=new PersonaEntity();
        	persona.setId(personaService.getIdPrimaryKey());
        	persona.setCi(entity.getPersona().getCi());
        	persona.setNombrecompleto(entity.getPersona().getNombrecompleto());
        	persona.setEmail(entity.getPersona().getEmail());
        	persona.setCelular(entity.getPersona().getCelular());
        	persona.setEstado(1);
        	PersonaEntity persona2=personaService.save(persona);
        	

        	entity.setId(socioRepository.getIdPrimaryKey());
        	entity.setCodigo(socioRepository.getCodigo());
        	
        	 
        	String codigoDocumento=passwordEncoder.encode(socioRepository.getCodigo()+"");
        	codigoDocumento = codigoDocumento.replace("/", "c");// Eliminar las barras '/' del resultado
        	codigoDocumento = codigoDocumento.replace(".", "a");// Eliminar las barras '/' del resultado
        	codigoDocumento = codigoDocumento.replace("$", "d");// Eliminar las barras '/' del resultado
        	entity.setNrodocumento(codigoDocumento);        	
        	entity.setLinkqr("/RestSocios/"+codigoDocumento);

            InstitucionEntity institucionEntity=institucionRepository.findById(1).get();
//            String bodyQR="http://"+direccionIP.getHostAddress()+":"+puertoservidor+""+"/socios/"+codigoDocumento;
            String bodyQR=institucionEntity.getHost()+"/socios/estadosocio/"+codigoDocumento;
        	
        	//GENERAR QR
//            imagePath=imagePath+codigoDocumento+".png";
            
//        	MethodUtils.generateImageQRCode(bodyQR, 250, 250, imagePath);
            qrCodeGeneratorService.generateQRCode(bodyQR, codigoDocumento);
            
//        	entity.setMatricula(entity.getMatricula());
//        	entity.setNombresocio(entity.getNombresocio());
//        	entity.setFechaemision(entity.getFechaemision());
//        	entity.setFechaexpiracion(entity.getFechaexpiracion());
//        	System.out.println("AGREGANDO PERSONA:"+persona2.toString());
        	entity.setPersona(persona2);
//        	System.out.println("AGREGANDO PROFESION");
        	entity.setProfesion(profesionRepository.findById(1).get());
//        	System.out.println("AGREGANDO INSTITUCION");
        	entity.setInstitucion(institucionRepository.findById(1).get());
//        	System.out.println("AGREGANDO EMPRESAS");
        	entity.setCatalogos(catalogoRepository.findByEstado(1));
        	
//        	System.out.println("AGREGANDO LOGO DE SOCIO");
        	if (!entity.getLogo().isEmpty()) {
        		String nombre="logo-"+this.socioRepository.getCodigo()+entity.getLogo().getOriginalFilename().substring(entity.getLogo().getOriginalFilename().lastIndexOf('.'));
    			System.out.println("NOMBRE SOCIO LOGO:"+nombre);
				String nombreLogo=archivoService.guargarArchivo(Constantes.nameFolderLogoSocio,entity.getLogo(),nombre);
				entity.setImagen(nombreLogo);
			}
        	
        	System.out.println("EntityPost:"+entity.toString());
            
        	entity = socioRepository.save(entity);
            return entity;
        } catch (Exception e){
        	e.printStackTrace();
            System.out.println(e.getMessage());
        	throw new Exception(e.getMessage());
            
        }
    }
    
	@Override
	@Transactional
	public SocioEntity updatecatalogos(Integer id, SocioEntity entidad) throws Exception {
		try {
			System.out.println("Modificar Entity:"+entidad.toString());
			//observado

			SocioEntity entitymod=socioRepository.findById(id).get();
			
			entitymod.getPersona().setCi(entidad.getPersona().getCi());
			entitymod.setMatricula(entidad.getMatricula());
			entitymod.setNombresocio(entidad.getNombresocio());
			entitymod.getPersona().setCelular(entidad.getPersona().getCelular());
			entitymod.setFechaemision(entidad.getFechaemision());
			entitymod.setFechaexpiracion(entidad.getFechaexpiracion());
			
			if (entidad.getCatalogos()!=null) {
				entitymod.setCatalogos(entidad.getCatalogos());
				
			}
			

			
//			entitymod=genericRepository.save(entidad);
			entitymod=genericRepository.save(entitymod);
			return entitymod;
		} catch (Exception e) { 
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
    
	@Override
	@Transactional
	public SocioEntity update(Integer id, SocioEntity entidad) throws Exception {
		try {
			System.out.println("Modificar Entity:"+entidad.toString());
			//observado

			SocioEntity entitymod=socioRepository.findById(id).get();
			
			entitymod.getPersona().setCi(entidad.getPersona().getCi());
			entitymod.setMatricula(entidad.getMatricula());
			entitymod.setNombresocio(entidad.getNombresocio());
			entitymod.getPersona().setCelular(entidad.getPersona().getCelular());
			entitymod.setFechaemision(entidad.getFechaemision());
			entitymod.setFechaexpiracion(entidad.getFechaexpiracion());
			entitymod.setLejendario(entidad.getLejendario());

			if (!entidad.getLogo().isEmpty()) {
//        		this.archivoService.eliminarArchivoSocioLogo(entitymod.getImagen());
        		this.archivoService.eliminarArchivo(Constantes.nameFolderLogoSocio,entitymod.getImagen());
        		String nombre="mod-"+entitymod.getCodigo()+entidad.getLogo().getOriginalFilename().substring(entidad.getLogo().getOriginalFilename().lastIndexOf('.'));
        		String nombreLogo=archivoService.guargarArchivo(Constantes.nameFolderLogoSocio,entidad.getLogo(),nombre);
				
				entitymod.setImagen(nombreLogo);
			}

			
//			entitymod=genericRepository.save(entidad);
			entitymod=genericRepository.save(entitymod);
			return entitymod;
		} catch (Exception e) { 
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	@Override
	public SocioEntity renovarQR(Integer id, SocioEntity entidad) throws Exception {
		try {
			System.out.println("Modificar Entity:"+entidad.toString());
			//observado

			SocioEntity entitymod=socioRepository.findById(id).get();
			entitymod.getPersona().setCi(entidad.getPersona().getCi());
			entitymod.setMatricula(entidad.getMatricula());
			entitymod.setNombresocio(entidad.getNombresocio());
			entitymod.getPersona().setCelular(entidad.getPersona().getCelular());
			entitymod.setFechaemision(entidad.getFechaemision());
			entitymod.setFechaexpiracion(entidad.getFechaexpiracion());
			
			
			//RENOVANDO QR
			String codigoDocumento=entitymod.getNrodocumento();
			System.out.println("**************************NUMERO DE DOCUMENTOOOO UPDATE QR:"+codigoDocumento);
			
            InstitucionEntity institucionEntity=institucionRepository.findById(1).get();
            System.out.println("**************************UPDATE HOSTTTT:");
            String bodyQR=institucionEntity.getHost()+"/socios/estadosocio/"+codigoDocumento;
            System.out.println("CONTENIDO QR:"+bodyQR);
            
//            String nombredelQR=codigoDocumento;
//            qrCodeGeneratorService.updateQRCodeContent(bodyQR,nombredelQR);

//            InstitucionEntity institucionEntity=institucionRepository.findById(1).get();
//            String bodyQR="http://"+direccionIP.getHostAddress()+":"+puertoservidor+""+"/socios/"+codigoDocumento;
//            String bodyQR=institucionEntity.getHost()+"/socios/estadosocio/"+codigoDocumento;
        	System.out.println("*^**************************ELIMINADO QR:");
        	
            this.archivoService.eliminarArchivo(Constantes.nameFolderQrSocio,codigoDocumento+".png");
            String qr_nuevo="modqr-"+codigoDocumento;
            qrCodeGeneratorService.generateQRCode(bodyQR, qr_nuevo);
            System.out.println("****************************QR MODIFICADO:"+qr_nuevo+".png"); 
			entitymod=genericRepository.save(entitymod);
			return entitymod;
		} catch (Exception e) { 
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	public SocioEntity findByNrodocumento(String  codigo) throws Exception {
		SocioEntity entity= new SocioEntity();
        try{
        	entity= socioRepository.findByNrodocumento( codigo);
          return entity;
      } catch (Exception e){
      		System.out.println(e.getMessage());
          return null;  
      }
	}

}
