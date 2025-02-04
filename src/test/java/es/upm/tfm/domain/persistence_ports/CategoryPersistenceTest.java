package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.CategoryDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoriesNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNameAlreadyExisting;
import es.upm.tfm.adapters.mysqldb.response.CategoryResponse;
import es.upm.tfm.domain.services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryPersistenceTest {

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
    void testGetCategories() throws CategoriesNotFoundException {
        CategoryResponse categoryResponse1 = new CategoryResponse(1L,"Category1");
        CategoryResponse categoryResponse2 = new CategoryResponse(2L,"Category2");

        when(categoryService.getCategories()).thenReturn(List.of(categoryResponse1, categoryResponse2));

        List<CategoryResponse> response = categoryPersistence.getCategories();

        assertEquals(response, List.of(categoryResponse1, categoryResponse2));
    }

    @Test
    void testGetCategoriesNoCategoriesFound() throws CategoriesNotFoundException {

        when(categoryService.getCategories()).thenThrow(CategoriesNotFoundException.class);

        Assertions.assertThrows(CategoriesNotFoundException.class, () -> {
            categoryPersistence.getCategories();
        });
    }
}
