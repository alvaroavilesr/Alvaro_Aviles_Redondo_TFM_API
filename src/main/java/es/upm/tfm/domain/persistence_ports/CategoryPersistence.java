package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.CategoryDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoriesNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryAlreadyAttachedToAnItem;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNameAlreadyExisting;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.CategoryResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryPersistence {

    CategoryResponse saveCategory(CategoryDTO categoryDTO) throws CategoryNameAlreadyExisting;

    List<CategoryResponse> getCategories()  throws CategoriesNotFoundException;

    CategoryResponse findById(Long id) throws CategoryNotFoundException;

    CategoryResponse deleteById(Long id) throws CategoryAlreadyAttachedToAnItem, CategoryNotFoundException;

    CategoryResponse updateCategory(Long id, CategoryDTO categoryDTO) throws CategoryNotFoundException, CategoryNameAlreadyExisting;
}
