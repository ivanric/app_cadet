package app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.entity.UsuarioEntity;


@Repository 
public interface UsuarioRepository extends GenericRepositoryNormal<UsuarioEntity, Integer>{
	 
	
	@Query(value="select COALESCE(max(id),0)+1 as id from usuario",nativeQuery = true)
	public int getIdPrimaryKey();
//	@Query(value = "SELECT  p.* FROM usuario p,rol r WHERE  upper(concat(p.id,p.username)) LIKE  concat('%',upper(?1),'%') and (p.estado=?2 or ?2=-1) ORDER BY p.id DESC LIMIT 10",nativeQuery = true)
//	public List<UsuarioEntity> findAll(String clave,int estado);
	
	@Query(value = "select t.* from usuario t where (t.estado=:estado or :estado=-1) and  (upper(concat(t.id,t.username,'')) like concat('%',upper(:search),'%')) ORDER BY t.id ASC LIMIT :length OFFSET :start ",
			countQuery ="SELECT count(t.*) FROM usuario t where (t.estado=:estado or :estado=-1) and  (upper(concat(t.id,t.num,'')) like concat('%',upper(:search),'%')) LIMIT :length OFFSET :start ",		
			nativeQuery = true)
	public List<UsuarioEntity> findAll(@Param("estado") int estado,@Param("search") String search,@Param("length") int length,@Param("start") int start);
	
	
	@Modifying 
	@Query(value="UPDATE usuario SET estado= CASE ?1 WHEN 1 THEN 0 ELSE 1 END WHERE id=?2",nativeQuery = true)
	public void updateStatus(int status,int id); 
	
	@Query(value="SELECT * FROM usuario WHERE username=?1",nativeQuery = true)
	public UsuarioEntity getUserByLogin(String valor); 
	
	@Query(value="select count(t.*) from usuario t where (upper(concat(t.id,t.username,'')) like concat('%',upper(:search),'%')) and (t.estado=:estado or :estado=-1) ",nativeQuery = true)
	public Integer getTotAll(@Param("search") String search,@Param("estado") int estado);
	
	@Query(value="SELECT * FROM usuario WHERE username=?1 AND estado=1",nativeQuery = true)
	UsuarioEntity findByUsername(String username);
	
} 