package app.service;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import app.entity.PersonaEntity;
import app.entity.CatalogoEntity;
import app.entity.ImagenesCatalogoEntity;
import app.repository.GenericRepositoryNormal;
import app.repository.ImagenCatalogoRepository;
import app.repository.InstitucionRepository;
import app.repository.ProfesionRepository;
import app.repository.CatalogoRepository;
import app.util.Constantes;
import app.util.QRCodeGeneratorService;

@Service
public class CatalogoServiceImpl extends GenericServiceImplNormal<CatalogoEntity, Integer> implements CatalogoService {

	@Autowired private CatalogoRepository catalogoRepository;
	@Autowired private ImagenCatalogoRepository ImagenCatalogoRepository;
	@Autowired private ArchivoService archivoService;
	

	@Autowired
	QRCodeGeneratorService qrCodeGeneratorService;
//	private final String imagePath = "./src/main/resources/static/qrcodes/QRCode.png";
//	private  String imagePath = "./src/main/resources/static/qrcodes/";
	
	@Value("${server.port}")
    private static String puertoservidor;
	
	CatalogoServiceImpl(GenericRepositoryNormal<CatalogoEntity, Integer> genericRepository) {
		super(genericRepository);
		// TODO Auto-generated constructor stub
	}

	
	//sin el @Override porque hereda de
	public int getIdPrimaryKey() throws Exception {
		
        try{
        	int id = catalogoRepository.getIdPrimaryKey();
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
        	int total = catalogoRepository.getCodigo();
          return total;
      } catch (Exception e){
      		System.out.println(e.getMessage());
//          throw new Exception(e.getMessage());
          return 0;
      }
	}
	

	@Override
	@Transactional
	public List<CatalogoEntity> findAll( int estado,String search,int length,int start ) throws Exception {
        try{
//            List<PaisEntity> entities = paisRepository.findAll(Sort.by("idpais").ascending());
            List<CatalogoEntity> entities = catalogoRepository.findAll(estado, search, length, start);
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
        	catalogoRepository.updateStatus(status,id);

        } catch (Exception e){
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            throw new Exception(e.getMessage());
        }
	}
	
	@Override
	public Integer getTotAll(String search,int estado) throws Exception {
		
        try{
        	int total = catalogoRepository.getTotAll(search, estado);
          return total;
      } catch (Exception e){
      		System.out.println(e.getMessage());
//          throw new Exception(e.getMessage());
          return 0;
      }
	}


	
	
	@Override
	@Transactional
	public CatalogoEntity save(CatalogoEntity entity) throws Exception {
	    try {
	        System.out.println("EntitySAVE_Servicio:" + entity.toString());

	        List<ImagenesCatalogoEntity> array_imagenes_catalogo = new ArrayList<>();

	        // Guardar imágenes del catálogo
	        if (entity.getCatalogo() != null && !entity.getCatalogo().isEmpty()) {
	            for (int i = 0; i < entity.getCatalogo().size(); i++) {
	                ImagenesCatalogoEntity imagenesCatalogoEntity = new ImagenesCatalogoEntity();
	                imagenesCatalogoEntity.setId(ImagenCatalogoRepository.getIdPrimaryKey());
	                imagenesCatalogoEntity.setCodigo(ImagenCatalogoRepository.getCodigo());

	                MultipartFile catalogoitem = entity.getCatalogo().get(i);
	                System.out.println("CATALOGO:" + catalogoitem.getOriginalFilename());

	                // Guardar en el sistema de archivos local
	                String nombreLocal = entity.getNit()+" - " + this.ImagenCatalogoRepository.getCodigo() +
	                                     catalogoitem.getOriginalFilename().substring(catalogoitem.getOriginalFilename().lastIndexOf('.'));
	                
	                
//	                String nombreLogoCatalogoLocal = archivoService.guargarArchivo(Constantes.nameFolderImgCatalogo, catalogoitem, nombreLocal);
//	                imagenesCatalogoEntity.setImagen(nombreLogoCatalogoLocal);
	                imagenesCatalogoEntity.setImagen(nombreLocal);

	                // Guardar en Google Drive
	                String idArchivoCatalogoDrive = archivoService.guargarArchivoDrive(Constantes.nameFolderImgCatalogo, catalogoitem, nombreLocal);
	                imagenesCatalogoEntity.setImagenDriveId(idArchivoCatalogoDrive);

	                imagenesCatalogoEntity.setEstado(1);

	                ImagenesCatalogoEntity imagenesCatalogoEntity2 = ImagenCatalogoRepository.save(imagenesCatalogoEntity);
	                array_imagenes_catalogo.add(imagenesCatalogoEntity2);
	            }
	            
	        }

	        entity.setImagenesCatalogos(array_imagenes_catalogo);
	        System.out.println("IMAGENES DE CATALOGO ASIGNADOS");
	        entity.setId(catalogoRepository.getIdPrimaryKey());
	        entity.setCodigo(catalogoRepository.getCodigo());

	        // Guardar logo
	        if (!entity.getLogo().isEmpty()) {
	            String nombreLocal =  entity.getNit() +
	                                 entity.getLogo().getOriginalFilename().substring(entity.getLogo().getOriginalFilename().lastIndexOf('.'));

	            // Guardar en el sistema de archivos local
//	            String nombreLogoLocal = archivoService.guargarArchivo(Constantes.nameFolderLogoCatalogo, entity.getLogo(), nombreLocal);
//	            entity.setNombrelogo(nombreLogoLocal);
	            entity.setNombrelogo(nombreLocal);

	            // Guardar en Google Drive
	            String idArchivoLogoDrive = archivoService.guargarArchivoDrive(Constantes.nameFolderLogoCatalogo, entity.getLogo(), nombreLocal);
	            entity.setNombrelogoDriveId(idArchivoLogoDrive);
	        }

	        System.out.println("EntityPost:" + entity.toString());
	        entity = catalogoRepository.save(entity);
	        return entity;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	System.out.println(" catalogo service save err:"+e.getMessage());
	        // Considera agregar más detalles en el mensaje de error
	        throw new Exception("Error al guardar el catálogo: " + e.getMessage(), e);
	    }
	}


	/*
	@Override
	@Transactional
	public CatalogoEntity save(CatalogoEntity entity) throws Exception {
	    try {
	        System.out.println("EntitySAVE_Servicio:" + entity.toString());

	        List<ImagenesCatalogoEntity> array_imagenes_catalogo = new ArrayList<>();

	        // Guardar las imágenes de catálogo en Google Drive
	        if (entity.getCatalogo() != null) {
	            if (!entity.getCatalogo().isEmpty()) {
	                for (int i = 0; i < entity.getCatalogo().size(); i++) {
	                    ImagenesCatalogoEntity imagenesCatalogoEntity = new ImagenesCatalogoEntity();
	                    imagenesCatalogoEntity.setId(ImagenCatalogoRepository.getIdPrimaryKey());
	                    imagenesCatalogoEntity.setCodigo(ImagenCatalogoRepository.getCodigo());

	                    MultipartFile catalogoitem = entity.getCatalogo().get(i);
	                    System.out.println("CATALOGO:" + catalogoitem.getOriginalFilename());

	                    String nombre = "cat-isert-" + this.ImagenCatalogoRepository.getCodigo() +
	                                    catalogoitem.getOriginalFilename().substring(catalogoitem.getOriginalFilename().lastIndexOf('.'));
	                    String idArchivoCatalogo = archivoService.guargarArchivoDrive(Constantes.nameFolderImgCatalogo, catalogoitem, nombre);
	                    imagenesCatalogoEntity.setImagen(idArchivoCatalogo);  // Guardar el ID del archivo en Google Drive
	                    imagenesCatalogoEntity.setEstado(1);

	                    ImagenesCatalogoEntity imagenesCatalogoEntity2 = ImagenCatalogoRepository.save(imagenesCatalogoEntity);
	                    array_imagenes_catalogo.add(imagenesCatalogoEntity2);
	                }
	            }
	        }

	        entity.setImagenesCatalogos(array_imagenes_catalogo);

	        entity.setId(catalogoRepository.getIdPrimaryKey());
	        entity.setCodigo(catalogoRepository.getCodigo());

	        // Guardar el logo en Google Drive
	        if (!entity.getLogo().isEmpty()) {
	            String nombre = "cod-" + this.catalogoRepository.getCodigo() +
	                            entity.getLogo().getOriginalFilename().substring(entity.getLogo().getOriginalFilename().lastIndexOf('.'));

	            String idArchivoLogo = archivoService.guargarArchivoDrive(Constantes.nameFolderLogoCatalogo, entity.getLogo(), nombre);
	            entity.setNombrelogo(idArchivoLogo);  // Guardar el ID del archivo en Google Drive
	        }

	        System.out.println("EntityPost:" + entity.toString());
	        entity = catalogoRepository.save(entity); 
	        return entity;
	    } catch (Exception e) {
	    	e.printStackTrace();
	        throw new Exception(e.getMessage());
	    }
	}
*/
    /*
	@Override
	@Transactional
	public CatalogoEntity update(Integer id, CatalogoEntity entity) throws Exception {
		try {
			System.out.println("Modificar Entity Service:"+entity.toString());
			//observado
			CatalogoEntity catalogoEntity2=catalogoRepository.findById(id).get();
			System.out.println("CATALOGO BD:"+catalogoEntity2.toString());
			List<ImagenesCatalogoEntity> array_imagenes_catalogo=new ArrayList<>();
			
        	if(entity.getCatalogo()!=null) {
        	  	if (!entity.getCatalogo().isEmpty()) {
        	  		for (int i = 0; i < catalogoEntity2.getImagenesCatalogos().size(); i++) {
						this.archivoService.eliminarArchivo(Constantes.nameFolderImgCatalogo,catalogoEntity2.getImagenesCatalogos().get(i).getImagen());
						this.ImagenCatalogoRepository.delete(catalogoEntity2.getImagenesCatalogos().get(i));
					}
        	  		
        	  		System.out.println("LISTA CATALOGOS TAM:"+entity.getCatalogo().size());
               		for (int i = 0; i < entity.getCatalogo().size(); i++) {
                		ImagenesCatalogoEntity imagenesCatalogoEntity=new ImagenesCatalogoEntity();
                		imagenesCatalogoEntity.setId(ImagenCatalogoRepository.getIdPrimaryKey());
                		imagenesCatalogoEntity.setCodigo(ImagenCatalogoRepository.getCodigo());
                		
                		MultipartFile catalogoitem=entity.getCatalogo().get(i);
                		System.out.println("IMAGEN CAT:"+(i+1)+" :"+catalogoitem.getOriginalFilename());
                		
                		String nombre="cat-modif-"+catalogoEntity2.getCodigo()+"-"+this.ImagenCatalogoRepository.getCodigo()+catalogoitem.getOriginalFilename().substring(catalogoitem.getOriginalFilename().lastIndexOf('.'));
        				System.out.println("NOMBRECATALOGOLOG:"+nombre);
                		String nombreLogoCatalogo=archivoService.guargarArchivo(Constantes.nameFolderImgCatalogo,catalogoitem,nombre);
        				imagenesCatalogoEntity.setImagen(nombreLogoCatalogo);
        				imagenesCatalogoEntity.setEstado(1);
        				
        				ImagenesCatalogoEntity imagenesCatalogoEntity2=ImagenCatalogoRepository.save(imagenesCatalogoEntity);
        				array_imagenes_catalogo.add(imagenesCatalogoEntity2);
            		}
        	  	}
        	}
       	
        	entity.setImagenesCatalogos(array_imagenes_catalogo);
        	
        	if (!entity.getLogo().isEmpty()) {
        		this.archivoService.eliminarArchivo(Constantes.nameFolderLogoCatalogo,catalogoEntity2.getNombrelogo());
        		String nombre="mod-"+catalogoEntity2.getCodigo()+entity.getLogo().getOriginalFilename().substring(entity.getLogo().getOriginalFilename().lastIndexOf('.'));
    			
				String nombreLogo=archivoService.guargarArchivo(Constantes.nameFolderLogoCatalogo,entity.getLogo(),nombre);
				entity.setNombrelogo(nombreLogo);
			}
        	
			 entity=genericRepository.save(entity);
			return entity;
		} catch (Exception e) { 
			e.printStackTrace(); 
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
    */	
	
	@Override
	@Transactional
	public CatalogoEntity update(Integer id, CatalogoEntity entity) throws Exception {
	    try {
	        System.out.println("Modificar Entity Service: " + entity.toString());
	        // Obtener el catálogo existente
	        CatalogoEntity catalogoEntity2 = catalogoRepository.findById(id).get();
	        System.out.println("CATALOGO BD: " + catalogoEntity2.toString());
	        List<ImagenesCatalogoEntity> array_imagenes_catalogo = new ArrayList<>();

	        // Eliminar imágenes del catálogo
	        if (entity.getCatalogo() != null) {
	            if (!entity.getCatalogo().isEmpty()) {
	                // Eliminar imágenes locales y en Google Drive
	                for (ImagenesCatalogoEntity imgEntity : catalogoEntity2.getImagenesCatalogos()) {
	                    String nombreImagen = imgEntity.getImagen();
	                    // Eliminar archivo local
//	                    this.archivoService.eliminarArchivo(Constantes.nameFolderImgCatalogo, nombreImagen);
	                    // Eliminar archivo en Google Drive
	                    this.archivoService.eliminarArchivoDrive(Constantes.nameFolderImgCatalogo, nombreImagen);
	                    this.ImagenCatalogoRepository.delete(imgEntity);
	                }
	                
	                System.out.println("LISTA CATALOGOS TAM: " + entity.getCatalogo().size());
	                // Agregar nuevas imágenes
	                for (MultipartFile catalogoitem : entity.getCatalogo()) {
	                    ImagenesCatalogoEntity imagenesCatalogoEntity = new ImagenesCatalogoEntity();
	                    imagenesCatalogoEntity.setId(ImagenCatalogoRepository.getIdPrimaryKey());
	                    imagenesCatalogoEntity.setCodigo(ImagenCatalogoRepository.getCodigo());

	                    String nombre =catalogoEntity2.getNit()+ "-" + this.ImagenCatalogoRepository.getCodigo() + catalogoitem.getOriginalFilename().substring(catalogoitem.getOriginalFilename().lastIndexOf('.'));
	                    System.out.println("NOMBRECATALOGOLOG: " + nombre);

	                    // Guardar archivo local
//	                    String nombreLogoCatalogo = archivoService.guargarArchivo(Constantes.nameFolderImgCatalogo, catalogoitem, nombre);
//	                    imagenesCatalogoEntity.setImagen(nombreLogoCatalogo);
	                    imagenesCatalogoEntity.setImagen(nombre);
	                    // Guardar archivo en Google Drive
	                    String idArchivoCatalogoDrive=archivoService.guargarArchivoDrive(Constantes.nameFolderImgCatalogo, catalogoitem, nombre);
	                    imagenesCatalogoEntity.setImagenDriveId(idArchivoCatalogoDrive);
	                    
	                    
	                    imagenesCatalogoEntity.setEstado(1);

	                    ImagenesCatalogoEntity imagenesCatalogoEntity2 = ImagenCatalogoRepository.save(imagenesCatalogoEntity);
	                    array_imagenes_catalogo.add(imagenesCatalogoEntity2);
	                }
	            }
	        }

	        entity.setImagenesCatalogos(array_imagenes_catalogo);

	        // Actualizar el logo
	        if (!entity.getLogo().isEmpty()) {
	            // Eliminar logo local
	            this.archivoService.eliminarArchivo(Constantes.nameFolderLogoCatalogo, catalogoEntity2.getNombrelogo());
	            // Eliminar logo en Google Drive
	            this.archivoService.eliminarArchivoDrive(Constantes.nameFolderLogoCatalogo, catalogoEntity2.getNombrelogo());

	            String nombre =catalogoEntity2.getNit() + entity.getLogo().getOriginalFilename().substring(entity.getLogo().getOriginalFilename().lastIndexOf('.'));
	            
//	            String nombreLogo = archivoService.guargarArchivo(Constantes.nameFolderLogoCatalogo, entity.getLogo(), nombre);
//	            entity.setNombrelogo(nombreLogo);
	            entity.setNombrelogo(nombre);
	            // Guardar nuevo logo en Google Drive
	            String idArchivoLogoDrive= archivoService.guargarArchivoDrive(Constantes.nameFolderLogoCatalogo, entity.getLogo(), nombre);
	            entity.setNombrelogoDriveId(idArchivoLogoDrive);
	            
	            
	        }

	        entity = genericRepository.save(entity);
	        return entity;
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println(e.getMessage());
	        throw new Exception(e.getMessage());
	    }
	}



}
