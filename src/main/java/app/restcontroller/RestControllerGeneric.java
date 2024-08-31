package app.restcontroller;

import java.io.Serializable;


import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import app.entity.GenericEntity;

public interface RestControllerGeneric <E extends GenericEntity,ID extends Serializable>{
	public ResponseEntity<? extends Object> getAll();
	public ResponseEntity<?> getAll(Pageable pageable);
	public ResponseEntity<?> getOne(@PathVariable ID id);
	public ResponseEntity<?> save(@RequestBody E entity);
	public ResponseEntity<?> update(@PathVariable ID id,@RequestBody E entity);
	public ResponseEntity<?> delete(@PathVariable ID id);

}
