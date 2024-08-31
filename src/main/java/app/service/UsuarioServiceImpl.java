package app.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.entity.PersonaEntity;
import app.entity.UsuarioEntity;
import app.repository.GenericRepositoryNormal;
import app.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl extends GenericServiceImplNormal<UsuarioEntity, Integer> implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired private PersonaService personaService;
	@Autowired 
	private BCryptPasswordEncoder passwordEncoder;
	
	UsuarioServiceImpl(GenericRepositoryNormal<UsuarioEntity, Integer> genericRepository) {
		super(genericRepository);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	@Transactional
	public List<UsuarioEntity> findAll( int estado,String search,int length,int start ) throws Exception {
        try{
//            List<PaisEntity> entities = paisRepository.findAll(Sort.by("idpais").ascending());
            List<UsuarioEntity> entities = usuarioRepository.findAll(estado, search, length, start);
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
        	usuarioRepository.updateStatus(status,id);

        } catch (Exception e){
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            throw new Exception(e.getMessage());
        }
	}

	//sin el @Override porque hereda de
	public int getIdPrimaryKey() throws Exception {
		
        try{
        	int id = usuarioRepository.getIdPrimaryKey();
          return id;
      } catch (Exception e){
      		System.out.println(e.getMessage());
//          throw new Exception(e.getMessage());
          return 0;
      }
	}
    @Override
    @Transactional
    public UsuarioEntity save(UsuarioEntity entity) throws Exception {
        try{
        	System.out.println("EntitySAVE_Servicio:"+entity.toString());
        	
        	//ADD PERSONA
        	
        	PersonaEntity persona=new PersonaEntity();
        	persona.setId(personaService.getIdPrimaryKey());
        	persona.setCi(entity.getPersona().getCi());
        	persona.setNombrecompleto(entity.getPersona().getNombrecompleto());
        	persona.setEmail(entity.getPersona().getEmail());
        	persona.setCelular(entity.getPersona().getCelular());
        	persona.setEstado(1);
        	PersonaEntity persona2=personaService.save(persona);
        	
        	entity.setPersona(persona2);
        	entity.setId(usuarioRepository.getIdPrimaryKey());
        	entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        	System.out.println("EntityUFVPost:"+entity.toString());
            entity = usuarioRepository.save(entity);
            return entity;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    
	@Override
	@Transactional
	public UsuarioEntity update(Integer id, UsuarioEntity entidad) throws Exception {
		try {
			System.out.println("UsuarioModificar:"+entidad.toString());
			//observado
			Optional<UsuarioEntity> entitiOptional=genericRepository.findById(id);//entitiOptional retorna un objeto si encuentra
			UsuarioEntity usuario=entitiOptional.get();

			if (!entidad.getPassword().equals(usuario.getPassword())) {
				entidad.setPassword(passwordEncoder.encode(entidad.getPassword()));
			}

			usuario=genericRepository.save(entidad);
			return usuario;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
    
	@Override
	public Integer getTotAll(String search,int estado) throws Exception {
		
        try{
        	int total = usuarioRepository.getTotAll(search, estado);
          return total;
      } catch (Exception e){
      		System.out.println(e.getMessage());
//          throw new Exception(e.getMessage());
          return 0;
      }
	}
	@Override
	public UsuarioEntity getUserByLogin(String  valor) throws Exception {
		UsuarioEntity entity= new UsuarioEntity();
        try{
        	entity= usuarioRepository.getUserByLogin( valor);
          return entity;
      } catch (Exception e){
      		System.out.println(e.getMessage());
//          throw new Exception(e.getMessage());
          return null;  
      }
	}
	
	
	@Override
	public UsuarioEntity findByEmail(String  username) throws Exception {
		UsuarioEntity entity= new UsuarioEntity();
        try{
        	entity= usuarioRepository.findByUsername( username);
          return entity;
      } catch (Exception e){
      		System.out.println(e.getMessage());
          return null;  
      }
	}
	
}
