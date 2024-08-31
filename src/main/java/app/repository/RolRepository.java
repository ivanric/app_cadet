package app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import app.entity.RolEntity;


@Repository
public interface RolRepository extends GenericRepository<RolEntity, Long>{ 
	
	RolEntity findByNombre(String name);
	
	@Query(value="SELECT max(id)+1  from rol",nativeQuery = true)
	public Integer getIdPrimaryKey();
	@Query(value = "SELECT  p.* FROM rol p WHERE upper(concat(p.nombre)) LIKE  concat('%',upper(?1),'%') and (p.estado=?2 or ?2=-1) ORDER BY p.id DESC LIMIT 10",nativeQuery = true)
	public List<RolEntity> findAll(String clave,int estado);
	
//	@Query(value = "select t.*,row_number() OVER(ORDER BY t.id) as RN,10 as tot from marca t where (t.estado=?2 or ?2=-1) and  (upper(t.nombre) like concat('%',upper(?3),'%')) ORDER BY t.id ASC ",nativeQuery = true)
	@Query(value = "select t.* from rol t where (t.estado=:estado or :estado=-1) and  (upper(t.nombre) like concat('%',upper(:search),'%')) ORDER BY t.id DESC ",nativeQuery = true)
	public Page<RolEntity> findAll(@Param("estado") int estado,@Param("search") String search,@Param("pageable") Pageable pageable);
	
	@Modifying
	@Query(value="UPDATE rol SET estado= CASE ?1 WHEN 1 THEN 0 ELSE 1 END WHERE id=?2",nativeQuery = true)
	public void updateStatus(int status,Long id);
	


} 