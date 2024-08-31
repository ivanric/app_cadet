package app.service;

import java.util.List;

import app.entity.SocioEntity;



public interface SocioService extends GenericServiceNormal<SocioEntity,Integer>{

	public int getIdPrimaryKey() throws Exception;
	public Integer getCodigo()throws Exception;
	public List<SocioEntity> findAll(int estado,String search,int length,int start )throws Exception;
	public void updateStatus(int status,int id) throws Exception;
	public Integer getTotAll(String search,int estado) throws Exception;
	SocioEntity findByNrodocumento(String codigo)throws Exception;
	public SocioEntity renovarQR(Integer id,SocioEntity entidad) throws Exception;
	public SocioEntity updatecatalogos(Integer id, SocioEntity entidad) throws Exception;
}
