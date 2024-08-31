package app.restcontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.entity.CatalogoEntity;
import app.entity.SocioEntity;
import app.service.CatalogoServiceImpl;
import app.util.Constantes;

@RestController
@RequestMapping("/RestCatalogos") 
public class RestCatalogo extends RestControllerGenericNormalImpl<CatalogoEntity,CatalogoServiceImpl> {

	@GetMapping("/listar")
	public ResponseEntity<?> getAll(HttpServletRequest request,@Param("draw")int draw,@Param("length")int length,@Param("start")int start,@Param("estado")int estado)throws IOException{
		String total="";
		Map<String, Object> Data = new HashMap<String, Object>();
		try {

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
			
			return ResponseEntity.status(HttpStatus.OK).body(Data);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Data);
		}
	}
	
    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(@RequestBody CatalogoEntity entity) {
        try {
        	System.out.println("Entidad:"+entity.toString());
        	servicio.updateStatus(entity.getEstado(), entity.getId());
        	CatalogoEntity entity2=servicio.findById(entity.getId());
            return ResponseEntity.status(HttpStatus.OK).body(entity2);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }
    
//    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/guardar")
	public ResponseEntity<?> save(CatalogoEntity entidad,@RequestParam("logo") MultipartFile file,@RequestParam("catalogo") MultipartFile catalogo){
//	public ResponseEntity<?> save(CatalogoEntity entidad){
		System.out.println("EntidadSave LLEGO:"+entidad.toString());

		try {
			
			
			return ResponseEntity.status(HttpStatus.OK).body(servicio.save(entidad));
		} catch (Exception e) {//BAD_REQUEST= es error 400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
    @PostMapping("/modificar/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id,CatalogoEntity entidad ,@RequestParam("logo") MultipartFile file,@RequestParam("catalogo") MultipartFile catalogo){
		try {
			System.out.println("EntidadModificar LLEGO:"+entidad.toString());
			return ResponseEntity.status(HttpStatus.OK).body(servicio.update(id,entidad));
		} catch (Exception e) {//BAD_REQUEST= es error 400
			e.printStackTrace();
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable String id){ 
        try { 
        	System.out.println("ID A BUSCAR");
        	CatalogoEntity entity=new CatalogoEntity();
        	entity=servicio.findById(Integer.parseInt(id));
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
}
