package app.repository;

import java.util.List;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.entity.PersonaEntity;


@Repository 
public interface PersonaRepository extends GenericRepositoryNormal<PersonaEntity, Integer>{
	 
	
	@Query(value="select COALESCE(max(id),0)+1 as id from persona",nativeQuery = true)
	public int getIdPrimaryKey();

	@Query(value = "select t.* from persona t where (t.estado=:estado or :estado=-1) and  (upper(concat(t.id,t.nombrecompleto,'')) like concat('%',upper(:search),'%')) ORDER BY t.id ASC LIMIT :length OFFSET :start ",
			countQuery ="SELECT count(t.*) FROM persona t where (t.estado=:estado or :estado=-1) and  (upper(concat(t.id,t.combrecompleto,'')) like concat('%',upper(:search),'%')) LIMIT :length OFFSET :start ",		
			nativeQuery = true)
	public List<PersonaEntity> findAll(@Param("estado") int estado,@Param("search") String search,@Param("length") int length,@Param("start") int start);
	
	
	@Modifying 
	@Query(value="UPDATE persona SET estado= CASE ?1 WHEN 1 THEN 0 ELSE 1 END WHERE id=?2",nativeQuery = true)
	public void updateStatus(int status,int id); 

	
	@Query(value="select count(t.*) from persona t where (upper(concat(t.id,t.nombrecompleto,'')) like concat('%',upper(:search),'%')) and (t.estado=:estado or :estado=-1) ",nativeQuery = true)
	public Integer getTotAll(@Param("search") String search,@Param("estado") int estado);
	

	
} 