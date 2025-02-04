package es.upm.tfm.domain.services;

import es.upm.tfm.adapters.mysqldb.dto.CategoryDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNameAlreadyExisting;
import es.upm.tfm.adapters.mysqldb.response.CategoryResponse;
import es.upm.tfm.domain.persistence_ports.CategoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryPersistence categoryPersistence;

    @Autowired
    @Lazy
    public CategoryService(CategoryPersistence categoryPersistence) {
        this.categoryPersistence = categoryPersistence;
    }

    public CategoryResponse saveCategory(CategoryDTO categoryDTO) throws CategoryNameAlreadyExisting {
        return this.categoryPersistence.saveCategory(categoryDTO);
    }
}
