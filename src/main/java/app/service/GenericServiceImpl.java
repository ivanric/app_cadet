package app.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import app.entity.GenericEntity;
import app.repository.GenericRepository;

public abstract class GenericServiceImpl <E extends GenericEntity,ID extends Serializable> implements GenericService<E, ID>{
	protected GenericRepository<E,ID> genericRepository;
	//instance

	public GenericServiceImpl(GenericRepository<E, ID> genericRepository) {
		super();
		this.genericRepository = genericRepository;
	}
	@Override
	@Transactional
	public List<E> findAll() throws Exception {
		try {
			List<E>  entities=genericRepository.findAll();
			return entities;
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Page<E> findAll(Pageable pageable) throws Exception {
		try {
			Page<E>  entities=genericRepository.findAll(pageable);
			return entities;
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(e.getMessage());
		}
	}
	@Override
	@Transactional
	public E findById(ID id) throws Exception {
		try {
			Optional<E> entitiOptional=genericRepository.findById(id);
			return entitiOptional.get();//una persona si lo encuentra
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	@Transactional
	public E save(E entidad) throws Exception {
		try {
			entidad=genericRepository.save(entidad);
			return entidad;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	@Transactional
	public E update(ID id, E entidad) throws Exception {
		try {
			//observado
			Optional<E> entitiOptional=genericRepository.findById(id);//entitiOptional retorna un objeto si encuentra
			E persona=entitiOptional.get();
			
			persona=genericRepository.save(entidad);
			return persona;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override 
	@Transactional
	public boolean delete(ID id) throws Exception {
		try {
			if (genericRepository.existsById(id)) {
				genericRepository.deleteById(id);
				return true;
			}else {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
}
