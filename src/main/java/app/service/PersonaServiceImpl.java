package app.service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.entity.PersonaEntity;
import app.repository.GenericRepositoryNormal;
import app.repository.PersonaRepository;

@Service
public class PersonaServiceImpl extends GenericServiceImplNormal<PersonaEntity, Integer> implements PersonaService {

	@Autowired
	private PersonaRepository personaRepository;
	
	
	PersonaServiceImpl(GenericRepositoryNormal<PersonaEntity, Integer> genericRepository) {
		super(genericRepository);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	@Transactional
	public List<PersonaEntity> findAll( int estado,String search,int length,int start ) throws Exception {
        try{
//            List<PaisEntity> entities = paisRepository.findAll(Sort.by("idpais").ascending());
            List<PersonaEntity> entities = personaRepository.findAll(estado, search, length, start);
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
        	personaRepository.updateStatus(status,id);

        } catch (Exception e){
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            throw new Exception(e.getMessage());
        }
	}

	//sin el @Override porque hereda de
	public int getIdPrimaryKey() throws Exception {
		
        try{
        	int id = personaRepository.getIdPrimaryKey();
          return id;
      } catch (Exception e){
      		System.out.println(e.getMessage());
//          throw new Exception(e.getMessage());
          return 0;
      }
	}
    @Override
    @Transactional
    public PersonaEntity save(PersonaEntity entity) throws Exception {
        try{
        	System.out.println("EntitySave:"+entity.toString());
        	int id=personaRepository.getIdPrimaryKey();
        	System.out.println("id:"+id);
        	entity.setId(id);
        	
        	System.out.println("EntityPost:"+entity.toString());
            entity = personaRepository.save(entity);
            return entity;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    
	@Override
	public Integer getTotAll(String search,int estado) throws Exception {
		
        try{
        	int total = personaRepository.getTotAll(search, estado);
          return total;
      } catch (Exception e){
      		System.out.println(e.getMessage());
//          throw new Exception(e.getMessage());
          return 0;
      }
	}

	

	
}
