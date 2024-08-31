package app.repository;
import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean //este repositorio no se va a instanciar, como los demas
public interface GenericRepositoryNormal <E extends Object, ID extends Serializable> extends JpaRepository<E, ID>{
	

}
