package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.CategoryDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNameAlreadyExisting;
import es.upm.tfm.adapters.mysqldb.response.CategoryResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryPersistence {

    CategoryResponse saveCategory(CategoryDTO categoryDTO) throws CategoryNameAlreadyExisting;

}
