package app;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import app.entity.DepartamentoEntity;
import app.entity.InstitucionEntity;
import app.entity.PaisEntity;
import app.entity.PersonaEntity;
import app.entity.ProfesionEntity;
import app.entity.ProvinciaEntity;
import app.entity.RolEntity;
import app.entity.UsuarioEntity;
import app.repository.DepartamentoRepository;
import app.repository.InstitucionRepository;
import app.repository.PaisRepository;
import app.repository.PersonaRepository;
import app.repository.ProfesionRepository;
import app.repository.ProvinciaRepository;
import app.repository.RolRepository;
import app.repository.UsuarioRepository;
import app.service.RolService;
/*

@SpringBootApplication
public class CadetappApplicationbck  extends SpringBootServletInitializer{

	@Autowired
	private ProfesionRepository profesionRepository;
	
	@Autowired
	private PaisRepository paisRepository;
	
	@Autowired
	private DepartamentoRepository departamentoRepository;
	
	@Autowired
	private ProvinciaRepository provinciaRepository;
	
	@Autowired
	private InstitucionRepository institucionRepository;
	
	@Autowired
	private RolRepository rolRepository;
	
	@Autowired
	private PersonaRepository personaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(CadetappApplicationbck.class, args);
	
		
	
		
		
		
	}
	  @Bean
	    public ApplicationRunner initializer() {
	        return args -> {
	        	
	        	ProfesionEntity profesionEntity=new ProfesionEntity(1, "ADMINISTRADOR DE EMPRESAS", 1);
	        	ProfesionEntity profesionsave=profesionRepository.save(profesionEntity);
	        	
	        	
	        	PaisEntity paisEntity=new PaisEntity(1,"BOLIVIA",1);
	        	PaisEntity paissave=paisRepository.save(paisEntity);
	        	
	        	DepartamentoEntity departamentoEntity=new DepartamentoEntity(1, null, "TARIJA",1, paissave);
	        	DepartamentoEntity departamentosave=departamentoRepository.save(departamentoEntity);
	        	
	        	ProvinciaEntity proviniciaEntity=new ProvinciaEntity(1,"CERCADO",1, departamentosave); 
	        	ProvinciaEntity provinciasave=provinciaRepository.save(proviniciaEntity);
	        	
	        	System.out.println("**************INIT PRE INSTI****");
	        	InstitucionEntity institucionEntitybd=null;
	        	try {
	        		institucionEntitybd = institucionRepository.findById(1).get();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
					// TODO: handle exception
					institucionEntitybd=null;
				}
	        	
//	        	InstitucionEntity institucionEntitybd = institucionRepository.findById(1).orElseThrow(() -> new Exception("Institución no encontrada"));
		        
	        	System.out.println("**************INIT INSTITUCION:"+institucionEntitybd);
	        	
	        	if (institucionEntitybd==null) {
	        		System.out.println("****************AGREGANDO INSTITUCION NUEVA");
//		        	InstitucionEntity institucionEntity=new InstitucionEntity(1,"123456789","CADET","COLEGIO DE ADMINISTRADOR DE EMPRESAS DE TARIJA","cadet", "cadet.tarija@gmail.com", "direccion", "75119900", null, "cadetarija.com", null, 1, provinciasave);
		        	InstitucionEntity institucionEntity=new InstitucionEntity(1,"123456789","CADET","COLEGIO DE ADMINISTRADOR DE EMPRESAS DE TARIJA","cadet", "cadet.tarija@gmail.com", "direccion", "75119900", null, "192.168.100.2:8080", null, 1, provinciasave);
		        	institucionRepository.save(institucionEntity);
				}
 
	        	
	        	Collection<RolEntity> rolesarray=new ArrayList<RolEntity>();
			 	
			 	RolEntity rolEntity=new RolEntity("ROLE_ADMIN",1); 
			 	RolEntity rolsave=rolRepository.save(rolEntity); 
			 	rolesarray.add(rolsave);
			 	// Inserta datos en la base de datos al iniciar la aplicación
				PersonaEntity persona=new PersonaEntity(1, "1234567891", "cadet", "cadet.tarija@gmail.com", 75119900, 1);
				personaRepository.save(persona);
				
				BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
				String password= bCryptPasswordEncoder.encode("cadet.2024");//encrypt password
				System.out.println(password);//$2a$10$Pe2oBHzkQVDf5ubi5ubThe0xAIimT00ogdXiAQKmX3g4.4W/LG.pm
				
				UsuarioEntity usuarioEntity=new UsuarioEntity(1, "cadet", password, 1,rolesarray, persona);
				usuarioRepository.save(usuarioEntity);
				
		        System.out.println("Usuarios agregados a la base de datos.");
	    };
	  }
//	 public void run(String args) throws Exception {
//	        
//		 	Collection<RolEntity> rolesarray=new ArrayList<RolEntity>();
//		 	
//		 	RolEntity rolEntity=new RolEntity("ROLE_ADMIN",1);
//		 	RolEntity rolsave=rolRepository.save(rolEntity);
//		 	rolesarray.add(rolsave);
//		 	// Inserta datos en la base de datos al iniciar la aplicación
//			PersonaEntity persona=new PersonaEntity(1, "1234567891", "cadet", "cadet.tarija@gmail.com", 75119900, 1);
//			personaRepository.save(persona);
//			
//			BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
//			String password= bCryptPasswordEncoder.encode("cadet.2024");//encrypt password
//			System.out.println(password);//$2a$10$Pe2oBHzkQVDf5ubi5ubThe0xAIimT00ogdXiAQKmX3g4.4W/LG.pm
//			
//			UsuarioEntity usuarioEntity=new UsuarioEntity(1, "cadet", password, 1,rolesarray, persona);
//			usuarioRepository.save(usuarioEntity);
//			
//	        System.out.println("Usuarios agregados a la base de datos.");
//	    }
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(CadetappApplicationbck.class);
	}

}*/
