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
import app.entity.ImagenesCatalogoEntity;
import app.repository.GenericRepositoryNormal;
import app.repository.InstitucionRepository;
import app.repository.ProfesionRepository;
import app.repository.ImagenCatalogoRepository;
import app.util.QRCodeGeneratorService;

@Service
public class ImagenCatalogoServiceImpl extends GenericServiceImplNormal<ImagenesCatalogoEntity, Integer> implements ImagenCatalogoService {

	@Autowired private ImagenCatalogoRepository ImagenCatalogoRepository;
	@Autowired private ArchivoService archivoService;
	
	
	@Autowired
	QRCodeGeneratorService qrCodeGeneratorService;
//	private final String imagePath = "./src/main/resources/static/qrcodes/QRCode.png";
//	private  String imagePath = "./src/main/resources/static/qrcodes/";
	
	@Value("${server.port}")
    private static String puertoservidor;
	
	ImagenCatalogoServiceImpl(GenericRepositoryNormal<ImagenesCatalogoEntity, Integer> genericRepository) {
		super(genericRepository);
		// TODO Auto-generated constructor stub
	}

	
	//sin el @Override porque hereda de
	public int getIdPrimaryKey() throws Exception {
		
        try{
        	int id = ImagenCatalogoRepository.getIdPrimaryKey();
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
        	int total = ImagenCatalogoRepository.getCodigo();
          return total;
      } catch (Exception e){
      		System.out.println(e.getMessage());
//          throw new Exception(e.getMessage());
          return 0;
      }
	}
	

	@Override
	@Transactional
	public List<ImagenesCatalogoEntity> findAll( int estado,String search,int length,int start ) throws Exception {
        try{
//            List<PaisEntity> entities = paisRepository.findAll(Sort.by("idpais").ascending());
            List<ImagenesCatalogoEntity> entities = ImagenCatalogoRepository.findAll(estado, search, length, start);
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
        	ImagenCatalogoRepository.updateStatus(status,id);

        } catch (Exception e){
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            throw new Exception(e.getMessage());
        }
	}
	
	@Override
	public Integer getTotAll(String search,int estado) throws Exception {
		
        try{
        	int total = ImagenCatalogoRepository.getTotAll(search, estado);
          return total;
      } catch (Exception e){
      		System.out.println(e.getMessage());
//          throw new Exception(e.getMessage());
          return 0;
      }
	}

	

    
    
    
	@Override
	@Transactional
	public ImagenesCatalogoEntity update(Integer id, ImagenesCatalogoEntity entidad) throws Exception {
		try {
			System.out.println("Modificar1:"+entidad.toString());
			//observado

			ImagenesCatalogoEntity entitymod=new ImagenesCatalogoEntity();
			
			entitymod=genericRepository.save(entidad);
			return entitymod;
		} catch (Exception e) { 
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
    


}
