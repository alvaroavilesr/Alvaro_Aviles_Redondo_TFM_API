package es.upm.tfm.domain.services;

import es.upm.tfm.adapters.mysqldb.dto.CategoryDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryAlreadyAttachedToAnItem;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNameAlreadyExisting;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.CategoryResponse;
import es.upm.tfm.domain.persistence_ports.CategoryPersistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryPersistence categoryPersistence;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testSaveCategory() throws CategoryNameAlreadyExisting {
        CategoryDTO categoryDTO = new CategoryDTO("Category1");
        CategoryResponse categoryResponse = new CategoryResponse(1L,"Category1");

        when(categoryService.saveCategory(categoryDTO)).thenReturn(categoryResponse);

        CategoryResponse response = categoryPersistence.saveCategory(categoryDTO);

        assertEquals(response, categoryResponse);
    }

    @Test
    void testSaveCategoryNameAlreadyExisting() throws CategoryNameAlreadyExisting {
        CategoryDTO categoryDTO = new CategoryDTO("Category1");

        when(categoryService.saveCategory(categoryDTO)).thenThrow(CategoryNameAlreadyExisting.class);

        Assertions.assertThrows(CategoryNameAlreadyExisting.class, () -> {
            categoryPersistence.saveCategory(categoryDTO);
        });
    }

    @Test
    void testGetCategory() throws CategoryNotFoundException {
        CategoryResponse categoryResponse = new CategoryResponse(1L,"Category1");

        when(categoryService.findById(1L)).thenReturn(categoryResponse);

        CategoryResponse response = categoryPersistence.findById(1L);

        assertEquals(categoryResponse, response);
    }

    @Test
    void testGetCategoryExceptionNoCategoryFound() throws CategoryNotFoundException {

        when(categoryService.findById(1L)).thenThrow(CategoryNotFoundException.class);

        Assertions.assertThrows(CategoryNotFoundException.class, () -> {
            categoryPersistence.findById(1L);
        });
    }

    @Test
    void testDeleteCategory() throws CategoryAlreadyAttachedToAnItem, CategoryNotFoundException {
        CategoryResponse categoryResponse = new CategoryResponse(1L,"Category1");

        when(categoryService.deleteById(1L)).thenReturn(categoryResponse);

        CategoryResponse response = categoryPersistence.deleteById(1L);

        assertEquals(categoryResponse, response);
    }

    @Test
    void testGetCategoryExceptionCategoryAlreadyAttachedToAnItem() throws CategoryAlreadyAttachedToAnItem, CategoryNotFoundException {

        when(categoryService.deleteById(1L)).thenThrow(CategoryAlreadyAttachedToAnItem.class);

        Assertions.assertThrows(CategoryAlreadyAttachedToAnItem.class, () -> {
            categoryPersistence.deleteById(1L);
        });
    }

    @Test
    void testGetCategoryExceptionCategoryNotFound() throws CategoryAlreadyAttachedToAnItem, CategoryNotFoundException {

        when(categoryService.deleteById(1L)).thenThrow(CategoryNotFoundException.class);

        Assertions.assertThrows(CategoryNotFoundException.class, () -> {
            categoryPersistence.deleteById(1L);
        });
    }
}
