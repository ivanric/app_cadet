package app.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import app.entity.GenericEntity;



public interface GenericService<E extends GenericEntity,ID extends Serializable>  {
	public List<E> findAll() throws Exception;
	public Page<E> findAll(Pageable pageable) throws Exception;
	public E findById(ID id)throws Exception;
	public E save(E entidad) throws Exception;
	public E update(ID id,E entidad) throws Exception;
	public boolean delete(ID id) throws Exception;
}
