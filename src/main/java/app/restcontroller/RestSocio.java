package app.restcontroller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.dto.SocioDTO;
import app.entity.CatalogoEntity;
import app.entity.PersonaEntity;
import app.entity.SocioEntity;
import app.entity.SocioEntity;
import app.service.SocioServiceImpl;
import app.service.UsuarioServiceImpl;
import app.util.Constantes;
import app.util.URIS;

@RestController
@RequestMapping("/RestSocios") 
public class RestSocio extends RestControllerGenericNormalImpl<SocioEntity,SocioServiceImpl> {

	@GetMapping("/listar")
	public ResponseEntity<?> getAll(HttpServletRequest request,@Param("draw")int draw,@Param("length")int length,@Param("start")int start,@Param("estado")int estado)throws IOException{
		String total="";
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			System.out.println("***************MODIFICANDO CODIGO V2 LISTANDO *****************************************");
			String search = request.getParameter("search[value]");
			int tot=Constantes.NUM_MAX_DATATABLE;
			System.out.println("tot:"+tot+"estado:"+estado+"search:"+search+"length:"+length+"start:"+start);
			List<?> lista= servicio.findAll(estado, search, length, start);
			System.out.println("listar:"+lista.toString()); 
			try {

					total=String.valueOf(servicio.getTotAll(search,estado));	
						
			} catch (Exception e) {
				total="0";
			}
			Data.put("draw", draw);
			Data.put("recordsTotal", total);
			Data.put("data", lista);
			if(!search.equals(""))
				Data.put("recordsFiltered", lista.size());
			else
				Data.put("recordsFiltered", total);
			System.out.println("***************MODIFICANDO CODIGO V2 LISTANDO *****************************************");
			return ResponseEntity.status(HttpStatus.OK).body(Data);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Data);
		}
	}
    @PostMapping("/guardar")
	public ResponseEntity<?> save( SocioDTO socioDTO,@RequestParam("logo") MultipartFile file){
//	public ResponseEntity<?> save(CatalogoEntity entidad){
		System.out.println("EntidadSave LLEGO:"+socioDTO.toString());

		try {
			PersonaEntity personaEntity=new PersonaEntity();
			personaEntity.setCi(socioDTO.getCi());
			personaEntity.setNombrecompleto(socioDTO.getNombrecompleto());
			personaEntity.setEmail(socioDTO.getEmail());
			personaEntity.setCelular(socioDTO.getCelular());
			personaEntity.setEstado(1);
			
			SocioEntity socioEntity=new SocioEntity();
			socioEntity.setNrodocumento(socioDTO.getNrodocumento());
			socioEntity.setMatricula(socioDTO.getMatricula());
			socioEntity.setNombresocio(socioDTO.getNombresocio());
			socioEntity.setFechaemision(socioDTO.getFechaemision());
			socioEntity.setFechaexpiracion(socioDTO.getFechaexpiracion());
			socioEntity.setEstado(1);
			socioEntity.setLejendario(socioDTO.getLejendario());
			
			socioEntity.setPersona(personaEntity);
			socioEntity.setLogo(socioDTO.getLogo());
			
			
			System.out.println("***************************LOGOO:"+socioEntity.getLogo().getOriginalFilename());
			System.out.println("***************************LEGENDARIO:"+socioDTO.getLejendario());
			
			return ResponseEntity.status(HttpStatus.OK).body(servicio.save(socioEntity));
		} catch (Exception e) {//BAD_REQUEST= es error 400
			e.printStackTrace();
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
    @PutMapping("/catalogos/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id,SocioEntity entidad){
		
		
		System.out.println("EntidadModificar LLEGO:"+entidad.toString());
    	try {
			
	
			return ResponseEntity.status(HttpStatus.OK).body(servicio.updatecatalogos(id,entidad));
		} catch (Exception e) {//BAD_REQUEST= es error 400
			e.printStackTrace();
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
    @PostMapping("/modificar/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id,SocioDTO socioDTO ,@RequestParam("logo") MultipartFile file){
		
		
		System.out.println("EntidadModificar LLEGO:"+socioDTO.toString());
    	try {
			
			PersonaEntity personaEntity=new PersonaEntity();
			personaEntity.setCi(socioDTO.getCi());
			personaEntity.setCelular(socioDTO.getCelular());
			personaEntity.setEstado(1);
			
			SocioEntity socioEntity=new SocioEntity();
			socioEntity.setId(socioDTO.getId());
			socioEntity.setCodigo(socioDTO.getCodigo());
			socioEntity.setMatricula(socioDTO.getMatricula());
			socioEntity.setNombresocio(socioDTO.getNombresocio());
			socioEntity.setFechaemision(socioDTO.getFechaemision());
			socioEntity.setFechaexpiracion(socioDTO.getFechaexpiracion());
			socioEntity.setEstado(1);
			socioEntity.setLejendario(socioDTO.getLejendario());
			
			socioEntity.setPersona(personaEntity);
			socioEntity.setLogo(socioDTO.getLogo());
			
			
			return ResponseEntity.status(HttpStatus.OK).body(servicio.update(id,socioEntity));
		} catch (Exception e) {//BAD_REQUEST= es error 400
			e.printStackTrace();
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(@RequestBody SocioEntity entity) {
        try {
        	System.out.println("Entidad:"+entity.toString());
        	servicio.updateStatus(entity.getEstado(), entity.getId());
        	SocioEntity entity2=servicio.findById(entity.getId());
            return ResponseEntity.status(HttpStatus.OK).body(entity2);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
    @GetMapping("/findByNrodocumento"+"/{id}")
    public ResponseEntity<?> buscar(@PathVariable String id){ 
        try { 
        	System.out.println("ID A BUSCAR");
        	SocioEntity entity=new SocioEntity();
        	entity=servicio.findByNrodocumento(id);
        	if (entity!=null) {
        		System.out.println("Socio encontrado:"+entity.toString());	
			}
        	
            return ResponseEntity.status(HttpStatus.OK).body(entity);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
    
	@PostMapping("modificarqr/{id}")
	public ResponseEntity<?> modificarqr(@PathVariable Integer id,@RequestBody  SocioEntity entidad){
		try {
			return ResponseEntity.status(HttpStatus.OK).body(servicio.renovarQR(id,entidad));
		} catch (Exception e) {//BAD_REQUEST= es error 400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
//    @GetMapping("/catalogos"+"/{id}")
//    public ResponseEntity<?> catalogos(@PathVariable String id){
//        try { 
//        	System.out.println("ID A BUSCAR");
//        	SocioEntity entity=new SocioEntity();
//        	entity=servicio.findById(Integer.parseInt(id));
//        	if (entity!=null) {
//        		System.out.println("Socio encontrado:"+entity.toString());	
//			}
//            return ResponseEntity.status(HttpStatus.OK).body(entity);
//        } catch (Exception e) {
//        	System.out.println(e.getMessage());
//        	e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
//        }
//    }
	
	 @GetMapping("/logo_socio/{filename}")
	    public ResponseEntity<Resource> getFile_logo_socio(@PathVariable String filename) {
	        try {
	            System.out.println("ENTRO LOGO SOCIO:" + filename);
	            URIS uris = new URIS();
	            String sistemaOperativo = uris.checkOS();
	            System.out.println("INICIANDO APP");
	            System.out.println("SISTEMA OPERATIVO: " + sistemaOperativo);

	            Resource resource = null;

	            try {
	                Path filePath;
	                if (sistemaOperativo.contains("Linux")) {
	                    filePath = Paths.get("/home", Constantes.nameFolderLogoSocio).resolve(filename).normalize();
	                    resource = new UrlResource(filePath.toUri());
	                } else if (sistemaOperativo.contains("Windows")) {
	                    filePath = Paths.get("C:\\", Constantes.nameFolderLogoSocio).resolve(filename).normalize();
	                    resource = new UrlResource(filePath.toUri());
	                }

	                // Verifica si el recurso fue encontrado y es legible
	                if (resource != null && resource.exists() && resource.isReadable()) {
	                    String contentType = "application/octet-stream"; // Tipo de contenido por defecto
	                    try {
	                        contentType = Files.probeContentType(resource.getFile().toPath());
	                    } catch (IOException ex) {
	                        System.out.println("No se pudo determinar el tipo de archivo.");
	                    }

	                    return ResponseEntity.ok()
	                            .contentType(MediaType.parseMediaType(contentType))
	                            .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
	                            .body(resource);
	                } else {
	                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	                System.out.println("Error al obtener la ruta de archivos: " + e.getMessage());
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
	 
	 
		
	 @GetMapping("/qr_socio/{filename}")
	    public ResponseEntity<Resource> qr_socio(@PathVariable String filename) {
	        try {
	            System.out.println("ENTRO qr SOCIO:" + filename);
	            URIS uris = new URIS();
	            String sistemaOperativo = uris.checkOS();
	            System.out.println("INICIANDO APP");
	            System.out.println("SISTEMA OPERATIVO: " + sistemaOperativo);

	            Resource resource = null;

	            try {
	                Path filePath;
	                if (sistemaOperativo.contains("Linux")) {
	                    filePath = Paths.get("/home", Constantes.nameFolderQrSocio).resolve(filename).normalize();
	                    resource = new UrlResource(filePath.toUri());
	                } else if (sistemaOperativo.contains("Windows")) {
	                    filePath = Paths.get("C:\\", Constantes.nameFolderQrSocio).resolve(filename).normalize();
	                    resource = new UrlResource(filePath.toUri());
	                }

	                // Verifica si el recurso fue encontrado y es legible
	                if (resource != null && resource.exists() && resource.isReadable()) {
	                    String contentType = "application/octet-stream"; // Tipo de contenido por defecto
	                    try {
	                        contentType = Files.probeContentType(resource.getFile().toPath());
	                    } catch (IOException ex) {
	                        System.out.println("No se pudo determinar el tipo de archivo.");
	                    }
	                    System.out.println("*************OBTENIENDO QR:"+resource.getFilename() );
	                    return ResponseEntity.ok()
	                            .contentType(MediaType.parseMediaType(contentType))
	                            .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
	                            .body(resource);
	                } else {
	                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	                System.out.println("***********************Error al obtener la ruta de archivos: " + e.getMessage());
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }


}
