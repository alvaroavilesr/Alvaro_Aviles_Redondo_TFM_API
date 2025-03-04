package es.upm.tfm.domain.services;

import es.upm.tfm.adapters.mysqldb.dto.CategoryDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoriesNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryAlreadyAttachedToAnItem;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNameAlreadyExisting;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.CategoryResponse;
import es.upm.tfm.domain.persistence_ports.CategoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CategoryResponse> getCategories()  throws CategoriesNotFoundException {
        return this.categoryPersistence.getCategories();
    }

    public CategoryResponse findById(Long id) throws CategoryNotFoundException {
        return this.categoryPersistence.findById(id);
    }

    public CategoryResponse deleteById(Long id) throws CategoryAlreadyAttachedToAnItem, CategoryNotFoundException {
        return this.categoryPersistence.deleteById(id);
    }

    public CategoryResponse updateCategory(Long id, CategoryDTO categoryDTO) throws CategoryNotFoundException, CategoryNameAlreadyExisting {
        return this.categoryPersistence.updateCategory(id, categoryDTO);
    }
}
