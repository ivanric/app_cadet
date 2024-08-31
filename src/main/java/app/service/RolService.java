package app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import app.entity.RolEntity;

public interface RolService extends GenericService<RolEntity,Long>{
	public List<RolEntity> findAll(String clave,int estado) throws Exception ;

	public Page<RolEntity> findAll(int estado,String search,Pageable pageable) throws Exception;
	public void updateStatus(int status,long id) throws Exception;
	public RolEntity save(RolEntity entidad) throws Exception;
}
