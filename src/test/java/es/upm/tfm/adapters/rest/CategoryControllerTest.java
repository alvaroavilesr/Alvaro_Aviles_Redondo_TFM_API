package es.upm.tfm.adapters.rest;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void testSaveCategory() throws CategoryNameAlreadyExisting {
        CategoryDTO categoryDTO = new CategoryDTO("Category1");
        CategoryResponse categoryResponse = new CategoryResponse(1L,"Category1");

        when(categoryService.saveCategory(categoryDTO)).thenReturn(categoryResponse);

        ResponseEntity<CategoryResponse> response = categoryController.saveCategory(categoryDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryResponse, response.getBody());

        verify(categoryService, times(1)).saveCategory(categoryDTO);
    }

    @Test
    void testCreateRoleExceptionNameAlreadyExisting() throws CategoryNameAlreadyExisting {
        CategoryDTO categoryDTO = new CategoryDTO("Category1");

        when(categoryService.saveCategory(categoryDTO)).thenThrow(CategoryNameAlreadyExisting.class);

        Assertions.assertThrows(CategoryNameAlreadyExisting.class, () -> {
            categoryController.saveCategory(categoryDTO);
        });

        verify(categoryService, times(1)).saveCategory(categoryDTO);
    }

    @Test
    void testGetCategories() throws CategoriesNotFoundException {
        CategoryResponse categoryResponse1 = new CategoryResponse(1L,"Category1");
        CategoryResponse categoryResponse2 = new CategoryResponse(2L,"Category2");

        when(categoryService.getCategories()).thenReturn(List.of(categoryResponse1,categoryResponse2));

        ResponseEntity<List<CategoryResponse>> response = categoryController.getCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(categoryResponse1,categoryResponse2), response.getBody());

        verify(categoryService, times(1)).getCategories();
    }

    @Test
    void testGetCategoriesExceptionNoCategoriesFound() throws CategoriesNotFoundException {

        when(categoryService.getCategories()).thenThrow(CategoriesNotFoundException.class);

        Assertions.assertThrows(CategoriesNotFoundException.class, () -> {
            categoryController.getCategories();
        });

        verify(categoryService, times(1)).getCategories();
    }
}
