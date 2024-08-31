package app.service;

import java.util.List;


import app.entity.UsuarioEntity;



public interface UsuarioService extends GenericServiceNormal<UsuarioEntity,Integer>{
	
	
	UsuarioEntity findByEmail(String username)throws Exception;
	
	public int getIdPrimaryKey() throws Exception;

	public List<UsuarioEntity> findAll(int estado,String search,int length,int start )throws Exception;
	public void updateStatus(int status,int id) throws Exception;
	public UsuarioEntity getUserByLogin(String  valor) throws Exception;
	public Integer getTotAll(String search,int estado) throws Exception;
}
