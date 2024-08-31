package app.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.entity.RolEntity;
import app.repository.GenericRepository;
import app.repository.RolRepository;

@Service
public class RolServiceImpl extends GenericServiceImpl<RolEntity, Long> implements RolService {

	@Autowired
	private RolRepository rolRepository;
	
	public RolServiceImpl(GenericRepository<RolEntity, Long> genericRepository) {
		super(genericRepository);
		// TODO Auto-generated constructor stub
	}

	@Override
	@Transactional
	public List<RolEntity> findAll(String clave, int estado) throws Exception {
        try{
//            List<PaisEntity> entities = paisRepository.findAll(Sort.by("idpais").ascending());
            List<RolEntity> entities = rolRepository.findAll(clave,estado);
            return entities;
        } catch (Exception e){
        	System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
	}
	@Override
	@Transactional(noRollbackFor = Exception.class)
	public Page<RolEntity> findAll( int estado, String search,Pageable pageable) throws Exception {
		
        try{
        	Page<RolEntity> entities = rolRepository.findAll( estado, search,pageable);
          return entities;
      } catch (Exception e){
      	System.out.println(e.getMessage());
          throw new Exception(e.getMessage());
      }
	}
	@Override 
	@Transactional
	public void updateStatus(int status, long id) throws Exception {
		// TODO Auto-generated method stub
        try{
        	System.out.println("estado:"+status+" id:"+id);
        	rolRepository.updateStatus(status,id);

        } catch (Exception e){
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            throw new Exception(e.getMessage());
        }
	}
	@Override
	@Transactional
	public RolEntity save(RolEntity entidad) throws Exception {
		String nombre=entidad.getNombre();
		nombre="ROLE_"+nombre;
		entidad.setNombre(nombre);
		try {
			entidad=genericRepository.save(entidad);
			return entidad;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	

}
