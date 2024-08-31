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
        try{
        	System.out.println("EntitySAVE_Servicio:"+entity.toString());
        	
        	//ADD PERSONA
//        	MultipartFile logo=entity.get;
//        	System.out.println("LOGONOMBRE:"+logo.getOriginalFilename());
        	System.out.println("LOGO: "+entity.getLogo().getOriginalFilename());
//        	for (MultipartFile file: entity.getCatalogo()) {
//				System.out.println("CATALOGO:"+file.getOriginalFilename());
//			}
        	
        	List<ImagenesCatalogoEntity> array_imagenes_catalogo=new ArrayList<>();
        	
        	if(entity.getCatalogo()!=null) {
        	  	if (!entity.getCatalogo().isEmpty()) {

            		for (int i = 0; i < entity.getCatalogo().size(); i++) {
                		ImagenesCatalogoEntity imagenesCatalogoEntity=new ImagenesCatalogoEntity();
                		imagenesCatalogoEntity.setId(ImagenCatalogoRepository.getIdPrimaryKey());
                		imagenesCatalogoEntity.setCodigo(ImagenCatalogoRepository.getCodigo());
                		
                		MultipartFile catalogoitem=entity.getCatalogo().get(i);
                		System.out.println("CATALOGO:"+catalogoitem.getOriginalFilename());
                		
                		String nombre="cat-isert-"+this.ImagenCatalogoRepository.getCodigo()+catalogoitem.getOriginalFilename().substring(catalogoitem.getOriginalFilename().lastIndexOf('.'));
        				String nombreLogoCatalogo=archivoService.guargarCatalogo(catalogoitem,nombre);
        				imagenesCatalogoEntity.setImagen(nombreLogoCatalogo);
        				imagenesCatalogoEntity.setEstado(1);
        				
        				ImagenesCatalogoEntity imagenesCatalogoEntity2=ImagenCatalogoRepository.save(imagenesCatalogoEntity);
        				array_imagenes_catalogo.add(imagenesCatalogoEntity2);
            		}
    			}


        	}
        	entity.setImagenesCatalogos(array_imagenes_catalogo);
        	
        	entity.setId(catalogoRepository.getIdPrimaryKey());
        	entity.setCodigo(catalogoRepository.getCodigo());
        	
        	if (!entity.getLogo().isEmpty()) {
        		String nombre="cod-"+this.catalogoRepository.getCodigo()+entity.getLogo().getOriginalFilename().substring(entity.getLogo().getOriginalFilename().lastIndexOf('.'));
//    			
				String nombreLogo=archivoService.guargarLogo(entity.getLogo(),nombre);
				entity.setNombrelogo(nombreLogo);
			}

        	System.out.println("EntityPost:"+entity.toString());
            entity = catalogoRepository.save(entity);
            return entity;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    
    
    
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
						this.archivoService.eliminarArchivoCatalogo(catalogoEntity2.getImagenesCatalogos().get(i).getImagen());
						this.ImagenCatalogoRepository.delete(catalogoEntity2.getImagenesCatalogos().get(i));
					}
        	  		
        	  		System.out.println("LISTA CATALOGOS TAM:"+entity.getCatalogo().size());
               		for (int i = 0; i < entity.getCatalogo().size(); i++) {
                		ImagenesCatalogoEntity imagenesCatalogoEntity=new ImagenesCatalogoEntity();
                		imagenesCatalogoEntity.setId(ImagenCatalogoRepository.getIdPrimaryKey());
                		imagenesCatalogoEntity.setCodigo(ImagenCatalogoRepository.getCodigo());
                		
                		MultipartFile catalogoitem=entity.getCatalogo().get(i);
                		System.out.println("IMAGEN CAT:"+(i+1)+" :"+catalogoitem.getOriginalFilename());
                		
                		String nombre="cat-modif-"+this.ImagenCatalogoRepository.getCodigo()+catalogoitem.getOriginalFilename().substring(catalogoitem.getOriginalFilename().lastIndexOf('.'));
        				System.out.println("NOMBRECATALOGOLOG:"+nombre);
                		String nombreLogoCatalogo=archivoService.guargarCatalogo(catalogoitem,nombre);
        				imagenesCatalogoEntity.setImagen(nombreLogoCatalogo);
        				imagenesCatalogoEntity.setEstado(1);
        				
        				ImagenesCatalogoEntity imagenesCatalogoEntity2=ImagenCatalogoRepository.save(imagenesCatalogoEntity);
        				array_imagenes_catalogo.add(imagenesCatalogoEntity2);
            		}
        	  	}
        	}
       	
        	entity.setImagenesCatalogos(array_imagenes_catalogo);
        	
        	if (!entity.getLogo().isEmpty()) {
        		this.archivoService.eliminarArchivoLogo(catalogoEntity2.getNombrelogo());
        		String nombre="mod-"+catalogoEntity2.getCodigo()+entity.getLogo().getOriginalFilename().substring(entity.getLogo().getOriginalFilename().lastIndexOf('.'));
    			
				String nombreLogo=archivoService.guargarLogo(entity.getLogo(),nombre);
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
    


}
