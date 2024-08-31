package app.service;

import java.util.List;

import app.entity.CatalogoEntity;



public interface CatalogoService extends GenericServiceNormal<CatalogoEntity,Integer>{

	public int getIdPrimaryKey() throws Exception;
	public Integer getCodigo()throws Exception;
	public List<CatalogoEntity> findAll(int estado,String search,int length,int start )throws Exception;
	public void updateStatus(int status,int id) throws Exception;
	public Integer getTotAll(String search,int estado) throws Exception;
}
