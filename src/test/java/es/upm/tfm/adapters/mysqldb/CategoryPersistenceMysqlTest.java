package es.upm.tfm.adapters.mysqldb;

import es.upm.tfm.adapters.mysqldb.dto.CategoryDTO;
import es.upm.tfm.adapters.mysqldb.entity.CategoryEntity;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoriesNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNameAlreadyExisting;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.persistence.CategoryPersistenceMysql;
import es.upm.tfm.adapters.mysqldb.response.CategoryResponse;
import es.upm.tfm.adapters.mysqldb.respository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryPersistenceMysqlTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryPersistenceMysql categoryPersistenceMysql;

    @Test
    void SaveCategoryNameAlreadyExisting() {

        CategoryDTO categoryDTO = new CategoryDTO("Category1");
        CategoryEntity categoryEntity = new CategoryEntity(1L, "Category1");

        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(categoryEntity));

        assertThrows(CategoryNameAlreadyExisting.class, () -> {
            categoryPersistenceMysql.saveCategory(categoryDTO);
        });
    }

    @Test
    void SaveCategory() throws CategoryNameAlreadyExisting {

        CategoryDTO categoryDTO = new CategoryDTO("Category2");
        CategoryEntity categoryEntity1 = new CategoryEntity(1L, "Category1");
        CategoryEntity categoryEntity2 = new CategoryEntity(2L, "Category2");

        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(categoryEntity1));
        Mockito.when(categoryRepository.save(Mockito.any(CategoryEntity.class))).thenReturn(categoryEntity2);
        CategoryResponse response = categoryPersistenceMysql.saveCategory(categoryDTO);

        Assertions.assertEquals("Category2", response.getName());

        verify(categoryRepository, times(1)).findAll();
        verify(categoryRepository, times(1)).save(Mockito.any(CategoryEntity.class));
    }

    @Test
    void GetCategoriesNoCategoriesFound() {

        Mockito.when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(CategoriesNotFoundException.class, () -> {
            categoryPersistenceMysql.getCategories();
        });

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void GetCategories() throws CategoriesNotFoundException {
        CategoryEntity categoryEntity1 = new CategoryEntity(1L, "Category1");
        CategoryEntity categoryEntity2 = new CategoryEntity(2L, "Category2");
        CategoryResponse categoryResponse1 = new CategoryResponse(1L, "Category1");
        CategoryResponse categoryResponse2 = new CategoryResponse(2L, "Category2");

        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(categoryEntity1, categoryEntity2));

        List<CategoryResponse> response = categoryPersistenceMysql.getCategories();

        Assertions.assertEquals(response, List.of(categoryResponse1, categoryResponse2));

        verify(categoryRepository, times(1)).findAll();

    }

    @Test
    void GetCategoryNoCategoryFound() {

        Mockito.when(categoryRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryPersistenceMysql.findById(1L);
        });

        verify(categoryRepository, times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    void GetCategory() throws CategoryNotFoundException {
        CategoryEntity categoryEntity = new CategoryEntity(1L, "Category1");
        CategoryResponse categoryResponse = new CategoryResponse(1L, "Category1");

        Mockito.when(categoryRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(categoryEntity));

        CategoryResponse response = categoryPersistenceMysql.findById(1L);

        Assertions.assertEquals(response, categoryResponse);

        verify(categoryRepository, times(1)).findById(Mockito.any(Long.class));

    }
}
