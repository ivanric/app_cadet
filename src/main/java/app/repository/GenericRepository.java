package app.repository;
import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import app.entity.GenericEntity;

@NoRepositoryBean //este repositorio no se va a instanciar, como los demas
public interface GenericRepository <E extends GenericEntity, ID extends Serializable> extends JpaRepository<E, ID>{
	

}
