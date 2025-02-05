package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.ItemDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.CategoryResponse;
import es.upm.tfm.adapters.mysqldb.response.ItemResponse;
import es.upm.tfm.domain.services.ItemService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @Test
    void testSaveItem() throws CategoryNotFoundException {

        ItemDTO itemDTO = new ItemDTO("Item", "Item1", "Item1", "S", 1L, "Image");
        CategoryResponse categoryResponse = new CategoryResponse(1L, "Category1");
        ItemResponse itemResponse= new ItemResponse(1L,"Item", "Item1", "Item1", "S", 1L, "Image", categoryResponse);

        when(itemService.saveItem("Category1", itemDTO)).thenReturn(itemResponse);

        ResponseEntity<ItemResponse> response = itemController.saveItem("Category1", itemDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemResponse, response.getBody());

        verify(itemService, times(1)).saveItem("Category1", itemDTO);
    }

    @Test
    void testSaveItemCategoryNotFound() throws CategoryNotFoundException {
        ItemDTO itemDTO = new ItemDTO("Item", "Item1", "Item1", "S", 1L, "Image");

        when(itemService.saveItem("Category1", itemDTO)).thenThrow(CategoryNotFoundException.class);

        Assertions.assertThrows(CategoryNotFoundException.class, () -> {
            itemController.saveItem("Category1", itemDTO);
        });

        verify(itemService, times(1)).saveItem("Category1", itemDTO);
    }
}
