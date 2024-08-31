package app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import app.entity.InstitucionEntity;


@Repository
public interface InstitucionRepository extends GenericRepositoryNormal<InstitucionEntity, Integer>{ 
	
	
	@Query(value="SELECT max(id)+1  from institucion",nativeQuery = true)
	public Integer getIdPrimaryKey();
	
	@Query(value = "SELECT  p.* FROM institucion p WHERE upper(concat(p.institucion)) LIKE  concat('%',upper(?1),'%') and (p.estado=?2 or ?2=-1) ORDER BY p.id DESC LIMIT 10",nativeQuery = true)
	public List<InstitucionEntity> findAll(String clave,int estado);
	
	@Query(value = "select t.* from institucion t where (t.estado=:estado or :estado=-1) and  (upper(t.institucion) like concat('%',upper(:search),'%')) ORDER BY t.id DESC ",nativeQuery = true)
	public Page<InstitucionEntity> findAll(@Param("estado") int estado,@Param("search") String search,@Param("pageable") Pageable pageable);
	
	@Modifying
	@Query(value="UPDATE institucion SET estado= CASE ?1 WHEN 1 THEN 0 ELSE 1 END WHERE id=?2",nativeQuery = true)
	public void updateStatus(int status,Integer id);
	


} 