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
import app.util.QRCodeGeneratorServiceDrive;

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
	private QRCodeGeneratorServiceDrive qrCodeGeneratorService;
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

/*
    @Override
    @Transactional
    public SocioEntity save(SocioEntity entity) throws Exception {
        try{
        	System.out.println("EntitySAVE_Servicio:"+entity.toString());
        	
        	if (entity.getId()==null) {
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
            	entity.setLinkqr(codigoDocumento+".png");

                InstitucionEntity institucionEntity=institucionRepository.findById(1).get();
//                String bodyQR="http://"+direccionIP.getHostAddress()+":"+puertoservidor+""+"/socios/"+codigoDocumento;
                String bodyQR=institucionEntity.getHost()+"/socios/estadosocio/"+codigoDocumento;
            	
            	//GENERAR QR
//                imagePath=imagePath+codigoDocumento+".png";
                
//            	MethodUtils.generateImageQRCode(bodyQR, 250, 250, imagePath);
                qrCodeGeneratorService.generateQRCode(bodyQR, codigoDocumento);
                
//            	entity.setMatricula(entity.getMatricula());
//            	entity.setNombresocio(entity.getNombresocio());
//            	entity.setFechaemision(entity.getFechaemision());
//            	entity.setFechaexpiracion(entity.getFechaexpiracion());
//            	System.out.println("AGREGANDO PERSONA:"+persona2.toString());
            	entity.setPersona(persona2);
//            	System.out.println("AGREGANDO PROFESION");
            	entity.setProfesion(profesionRepository.findById(1).get());
//            	System.out.println("AGREGANDO INSTITUCION");
            	entity.setInstitucion(institucionRepository.findById(1).get());
//            	System.out.println("AGREGANDO EMPRESAS");
            	entity.setCatalogos(catalogoRepository.findByEstado(1));
            	
//            	System.out.println("AGREGANDO LOGO DE SOCIO");
            	if (!entity.getLogo().isEmpty()) {
            		String nombre="logo-"+this.socioRepository.getCodigo()+entity.getLogo().getOriginalFilename().substring(entity.getLogo().getOriginalFilename().lastIndexOf('.'));
        			System.out.println("NOMBRE SOCIO LOGO:"+nombre);
    				String nombreLogo=archivoService.guargarArchivo(Constantes.nameFolderLogoSocio,entity.getLogo(),nombre);
    				entity.setImagen(nombreLogo);
    			}
            	
            	System.out.println("EntityPost:"+entity.toString());
                
            	entity = socioRepository.save(entity);
			}else {
				entity = socioRepository.save(entity);
			}
        	

            return entity;
        } catch (Exception e){
        	e.printStackTrace();
            System.out.println(e.getMessage());
        	throw new Exception(e.getMessage());
            
        }
    }*/
	
	
	@Override
	@Transactional
	public SocioEntity save(SocioEntity entity) throws Exception {
	    try {
	        System.out.println("EntitySAVE_Servicio:" + entity.toString());

	        if (entity.getId() == null) {
	            System.out.println("IMAGEN:" + entity.getLogo().getOriginalFilename());

	            // ADD PERSONA
	            PersonaEntity persona = new PersonaEntity();
	            persona.setId(personaService.getIdPrimaryKey());
	            persona.setCi(entity.getPersona().getCi());
	            persona.setNombrecompleto(entity.getPersona().getNombrecompleto());
	            persona.setEmail(entity.getPersona().getEmail());
	            persona.setCelular(entity.getPersona().getCelular());
	            persona.setEstado(1);
	            PersonaEntity persona2 = personaService.save(persona);

	            entity.setId(socioRepository.getIdPrimaryKey());
	            entity.setCodigo(socioRepository.getCodigo());

	            String codigoDocumento = passwordEncoder.encode(socioRepository.getCodigo() + "");
	            codigoDocumento = codigoDocumento.replace("/", "c"); // Eliminar las barras '/' del resultado
	            codigoDocumento = codigoDocumento.replace(".", "a"); // Eliminar los puntos '.' del resultado
	            codigoDocumento = codigoDocumento.replace("$", "d"); // Eliminar los signos '$' del resultado
	            entity.setNrodocumento(codigoDocumento);
	            entity.setLinkqr(codigoDocumento + ".png");

	            InstitucionEntity institucionEntity = institucionRepository.findById(1).get();
	            String bodyQR = institucionEntity.getHost() + "/socios/estadosocio/" + codigoDocumento;

	            // GENERAR QR localmete y en drive guarda
//	            qrCodeGeneratorService.generateQRCode(bodyQR, codigoDocumento);
	            qrCodeGeneratorService.generateQRCode(bodyQR,"QR - "+entity.getPersona().getCi());

	            entity.setPersona(persona2);
	            entity.setProfesion(profesionRepository.findById(1).get());
	            entity.setInstitucion(institucionRepository.findById(1).get());
	            entity.setCatalogos(catalogoRepository.findByEstado(1));

	            // Guardar logo del socio en el sistema de archivos local y en Google Drive
	            if (!entity.getLogo().isEmpty()) {
//	                String nombre = "logo-" + this.socioRepository.getCodigo() +
//	                        entity.getLogo().getOriginalFilename().substring(entity.getLogo().getOriginalFilename().lastIndexOf('.'));
//	                System.out.println("NOMBRE SOCIO LOGO:" + nombre);
	            	
	                String nombre = "SOCIO - " + entity.getPersona().getCi() +
	                        entity.getLogo().getOriginalFilename().substring(entity.getLogo().getOriginalFilename().lastIndexOf('.'));
	                System.out.println("NOMBRE SOCIO LOGO:" + nombre);
	                
	                // Guardar localmente
//	                String nombreLogoLocal = archivoService.guargarArchivo(Constantes.nameFolderLogoSocio, entity.getLogo(), nombre);
//	                entity.setImagen(nombreLogoLocal);
	                entity.setImagen(nombre);

	                // Guardar en Google Drive
	                String idArchivoLogoDrive = archivoService.guargarArchivoDrive(Constantes.nameFolderLogoSocio, entity.getLogo(), nombre);
	                entity.setImagenDriveId(idArchivoLogoDrive);
	            }

	            System.out.println("EntityPost:" + entity.toString());

	            // Guardar la entidad de socio en la base de datos
	            entity = socioRepository.save(entity);
	        } else {
	            // Actualizar la entidad de socio existente
	            entity = socioRepository.save(entity);
	        }

	        return entity;
	    } catch (Exception e) {
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
    /*
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
	}*/
	
	@Override
	@Transactional
	public SocioEntity update(Integer id, SocioEntity entidad) throws Exception {
	    try {
	        System.out.println("Modificar Entity:" + entidad.toString());
	        
	        // Obtener el socio existente
	        SocioEntity entitymod = socioRepository.findById(id).orElseThrow(() -> new Exception("Socio no encontrado"));

	        // Actualizar los datos del socio
	        entitymod.getPersona().setCi(entidad.getPersona().getCi());
	        entitymod.setMatricula(entidad.getMatricula());
	        entitymod.setNombresocio(entidad.getNombresocio());
	        entitymod.getPersona().setCelular(entidad.getPersona().getCelular());
	        entitymod.setFechaemision(entidad.getFechaemision());
	        entitymod.setFechaexpiracion(entidad.getFechaexpiracion());
	        entitymod.setLejendario(entidad.getLejendario());

	        // Actualizar el logo del socio
	        if (!entidad.getLogo().isEmpty()) {
	            // Eliminar el logo existente localmente
//	            this.archivoService.eliminarArchivo(Constantes.nameFolderLogoSocio, entitymod.getImagen());

	            // Eliminar el logo existente en Google Drive
	            this.archivoService.eliminarArchivoDrive(Constantes.nameFolderLogoSocio, entitymod.getImagen());

	            // Guardar el nuevo logo localmente

	                       
	            String nombre = "SOCIO - " + entitymod.getPersona().getCi() + 
                        entidad.getLogo().getOriginalFilename().substring(entidad.getLogo().getOriginalFilename().lastIndexOf('.'));
            	
//	            String nombreLogo = archivoService.guargarArchivo(Constantes.nameFolderLogoSocio, entidad.getLogo(), nombre);
//	            entitymod.setImagen(nombreLogo);
	            
	            entitymod.setImagen(nombre);

	            // Guardar el nuevo logo en Google Drive
	            String idArchivoLogoDrive = archivoService.guargarArchivoDrive(Constantes.nameFolderLogoSocio, entidad.getLogo(), nombre);
	            entitymod.setImagenDriveId(idArchivoLogoDrive); // Asumimos que hay un campo `logoDriveId` para almacenar el ID de Drive
	        }

	        // Guardar cambios en la base de datos
	        entitymod = genericRepository.save(entitymod);
	        return entitymod;
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println(e.getMessage());
	        throw new Exception(e.getMessage());
	    }
	}
	
	/*
	@Override
	public SocioEntity renovarQR(Integer id, SocioEntity entidad) throws Exception{
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
        	System.out.println("*************ELIMNANDO QR SERVICE:"+entitymod.getLinkqr());
            this.archivoService.eliminarArchivo(Constantes.nameFolderQrSocio,entitymod.getLinkqr());
            
       	 
	       	String codigoDocumento_md=passwordEncoder.encode(entitymod.getCodigo()+"");
	       	codigoDocumento_md = codigoDocumento.replace("/", "c");// Eliminar las barras '/' del resultado
	       	codigoDocumento_md = codigoDocumento.replace(".", "a");// Eliminar las barras '/' del resultado
	       	codigoDocumento_md = codigoDocumento.replace("$", "d");// Eliminar las barras '/' del resultado
	       	entitymod.setNrodocumento(codigoDocumento);        	
//	       	entitymod.setNrodocumento(codigoDocumento_md);  
            
            String qr_nuevo="qr-"+codigoDocumento_md;
            qrCodeGeneratorService.generateQRCode(bodyQR, qr_nuevo);
            System.out.println("****************************QR NUEVO MODIFICADO:"+qr_nuevo+".png"); 
            entitymod.setLinkqr(qr_nuevo+".png");
			entitymod=genericRepository.save(entitymod);
			
			return entitymod;
		} catch (Exception e) { 
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	*/
	
	@Override
	public SocioEntity renovarQR(Integer id, SocioEntity entidad) throws Exception {
	    try {
	        System.out.println("Modificar Entity QR*****************:" + entidad.toString());

	        // Buscar y actualizar los datos del socio
	        SocioEntity entitymod = socioRepository.findById(id)
	            .orElseThrow(() -> new Exception("Socio no encontrado"));
	        entitymod.getPersona().setCi(entidad.getPersona().getCi());
	        entitymod.setMatricula(entidad.getMatricula());
	        entitymod.setNombresocio(entidad.getNombresocio());
	        entitymod.getPersona().setCelular(entidad.getPersona().getCelular());
	        entitymod.setFechaemision(entidad.getFechaemision());
	        entitymod.setFechaexpiracion(entidad.getFechaexpiracion());

	        // Generar el contenido del QR
	        String codigoDocumento = entitymod.getNrodocumento();
	        System.out.println("************************** NUMERO DE DOCUMENTO UPDATE QR:" + codigoDocumento);

	        InstitucionEntity institucionEntity = institucionRepository.findById(1)
	            .orElseThrow(() -> new Exception("Institución no encontrada"));
	        System.out.println("*************INSTITUCION:" + institucionEntity.toString());
	        String bodyQR = institucionEntity.getHost() + "/socios/estadosocio/" + codigoDocumento;
	        System.out.println("****************CONTENIDO QR:" + bodyQR);

	        // Intentar eliminar el QR anterior en Google Drive
	        try {
	            System.out.println("************* ELIMINANDO QR EN GOOGLE DRIVE: " + entitymod.getLinkqr());
	            this.archivoService.eliminarArchivoDrive(Constantes.nameFolderQrSocio, entitymod.getLinkqr());
	        } catch (Exception e) {
	            System.out.println("No se pudo eliminar el QR anterior en Google Drive: " + e.getMessage());
	        }

	        // Codificar el número de documento para generar un nombre seguro para el archivo
	        String codigoDocumento_md = codigoDocumento.replace("/", "c")
	                                                   .replace(".", "a")
	                                                   .replace("$", "d");
//	        entitymod.setNrodocumento(codigoDocumento);

	        // Generar el nuevo nombre del archivo QR
//	        String qr_nuevo = "qr-" + codigoDocumento_md;
	        String qr_nuevo = "qr-" + entitymod.getPersona().getCi();

	        // Generar el QR localmente y en Google Drive
	        qrCodeGeneratorService.generateQRCode(bodyQR, qr_nuevo);  // Asegúrate de que se guarda localmente y en Google Drive
	        
	        // Actualizar el enlace del QR en la entidad
	        entitymod.setLinkqr(qr_nuevo + ".png");
	        entitymod = genericRepository.save(entitymod);

	        System.out.println("**************************** QR NUEVO MODIFICADO: " + qr_nuevo + ".png");

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
