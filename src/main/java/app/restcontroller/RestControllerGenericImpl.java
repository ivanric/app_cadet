package app.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import app.entity.GenericEntity;
import app.service.GenericServiceImpl;

public abstract class RestControllerGenericImpl <E extends GenericEntity, S extends GenericServiceImpl<E,Long>> implements RestControllerGeneric<E,Long> {
	@Autowired 
	protected S servicio;
	
	@GetMapping(value = "")
	public ResponseEntity<?> getAll(){
		System.out.println("GET LLAMADA");
		try {
			return ResponseEntity.status(HttpStatus.OK).body(servicio.findAll());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
	@GetMapping(value = "/paged")
	public ResponseEntity<?> getAll(Pageable pageable){
		try {
			System.out.println("page:"+pageable.toString());
			return ResponseEntity.status(HttpStatus.OK).body(servicio.findAll(pageable));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
	
//	@GetMapping(value = "/listar")
//	public ResponseEntity<?> getAll(int tot, int estado, String search, int length, int start){
//		try {
//			return ResponseEntity.status(HttpStatus.OK).body(servicio.findAll());
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
//		}
//	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getOne(@PathVariable Long id){
		try {
			return ResponseEntity.status(HttpStatus.OK).body(servicio.findById(id));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
	@PostMapping("")
	public ResponseEntity<?> save(@RequestBody E entidad){
		System.out.println("EntidadSave:"+entidad.toString());
		
		
		try {
			System.out.println("Entidad"+entidad.toString());
			return ResponseEntity.status(HttpStatus.OK).body(servicio.save(entidad));
		} catch (Exception e) {//BAD_REQUEST= es error 400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id,@RequestBody  E entidad){
		try {
			return ResponseEntity.status(HttpStatus.OK).body(servicio.update(id,entidad));
		} catch (Exception e) {//BAD_REQUEST= es error 400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		try {
			//NO_CONTENT no tiene contenido
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(servicio.delete(id));
		} catch (Exception e) {//BAD_REQUEST= es error 400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error, Por favor intente mas tarde. \"}");
		}
	}
}
