package app.restcontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.entity.UsuarioEntity;
import app.service.UsuarioServiceImpl;
import app.util.Constantes;

@RestController
@RequestMapping("/RestUsuarios") 
public class RestUsuario extends RestControllerGenericNormalImpl<UsuarioEntity,UsuarioServiceImpl> {
	@Autowired 
	private BCryptPasswordEncoder passwordEncoder;
//	@GetMapping("/lista")
//    public ResponseEntity<?> search(@RequestParam String search,@RequestParam int status) {
//    	System.out.println("lista");
//        try {
////        	System.out.println("list:"+paisService.findAll(valor_busqueda));
//            return ResponseEntity.status(HttpStatus.OK).body(servicio.findAll(search,status));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
//        }
//    }
	 
	@GetMapping("/listar")
	public ResponseEntity<?> getAll(HttpServletRequest request,@Param("draw")int draw,@Param("length")int length,@Param("start")int start,@Param("estado")int estado)throws IOException{
		String total="";
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			
			
			String search = request.getParameter("search[value]");
			int tot=Constantes.NUM_MAX_DATATABLE;
			System.out.println("tot:"+tot+"estado:"+estado+"search:"+search+"length:"+length+"start:"+start);
			List<?> lista= servicio.findAll(estado, search, length, start);
			System.out.println("listar_usuarios:"+lista.toString()); 
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
			
			return ResponseEntity.status(HttpStatus.OK).body(Data);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Data);
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"error\": \""+e.getMessage() + "\"}"));
		}
	}
	
    @PostMapping("updateStatus")
    public ResponseEntity<?> updateStatus(@RequestBody UsuarioEntity entity) {
        try {
        	System.out.println("Entidad:"+entity.toString());
        	servicio.updateStatus(entity.getEstado(), entity.getId());
        	UsuarioEntity entity2=servicio.findById(entity.getId());
            return ResponseEntity.status(HttpStatus.OK).body(entity2);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
    
//	@PostMapping("")
//	public ResponseEntity<?> save(@RequestBody UsuarioEntity entidad){
//		
//		try {
//			System.out.println("Entidad SAVE"+entidad.toString());
//			entidad.setPassword(passwordEncoder.encode(entidad.getPassword()));
//			
//			return ResponseEntity.status(HttpStatus.OK).body(servicio.save(entidad));
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
//		}
//	} 
//	@PutMapping("modificar/{id}")
//	public ResponseEntity<?> update(@PathVariable int id,@RequestBody  UsuarioEntity entidad){
//		try {
//			System.out.println("RestController Modificacion Usuario");
//			UsuarioEntity usuariodb=servicio.findById(id);
//			System.out.println("usuarioDb:"+usuariodb);
//			if (!entidad.getPassword().equals(usuariodb.getPassword())) {
//				entidad.setPassword(passwordEncoder.encode(entidad.getPassword()));
//			}
//			return ResponseEntity.status(HttpStatus.OK).body(servicio.update(id,entidad));
//		} catch (Exception e) {//BAD_REQUEST= es error 400
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
//		}
//	}
}
