package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.ItemDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemAlreadyInAnOrderException;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.NoItemsFoundException;
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

import java.util.List;

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

    @Test
    void testGetItemsNoItemsFound() throws NoItemsFoundException {

        when(itemService.getStock()).thenThrow(NoItemsFoundException.class);

        Assertions.assertThrows(NoItemsFoundException.class, () -> {
            itemController.getItems();
        });

        verify(itemService, times(1)).getStock();
    }

    @Test
    void testGetItems() throws NoItemsFoundException {

        CategoryResponse categoryResponse = new CategoryResponse(1L, "Category1");
        ItemResponse itemResponse = new ItemResponse(1L,"Item", "Item1", "Item1", "S", 1L, "Image", categoryResponse);

        when(itemService.getStock()).thenReturn(List.of(itemResponse));

        ResponseEntity<List<ItemResponse>> response = itemController.getItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(itemResponse), response.getBody());

        verify(itemService, times(1)).getStock();
    }

    @Test
    void testGetItemNoItemFound() throws ItemNotFoundException {

        when(itemService.findById(1L)).thenThrow(ItemNotFoundException.class);

        Assertions.assertThrows(ItemNotFoundException.class, () -> {
            itemController.getItem(1L);
        });

        verify(itemService, times(1)).findById(1L);
    }

    @Test
    void testGet() throws ItemNotFoundException {

        CategoryResponse categoryResponse = new CategoryResponse(1L, "Category1");
        ItemResponse itemResponse = new ItemResponse(1L,"Item", "Item1", "Item1", "S", 1L, "Image", categoryResponse);

        when(itemService.findById(1L)).thenReturn(itemResponse);

        ResponseEntity<ItemResponse> response = itemController.getItem(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemResponse, response.getBody());

        verify(itemService, times(1)).findById(1L);
    }

    @Test
    void testDeleteItemItemAlreadyInAnOrder() throws ItemAlreadyInAnOrderException, ItemNotFoundException {

        when(itemService.deleteById(1L)).thenThrow(ItemAlreadyInAnOrderException.class);

        Assertions.assertThrows(ItemAlreadyInAnOrderException.class, () -> {
            itemController.deleteItem(1L);
        });

        verify(itemService, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteItemItemNotFound() throws ItemAlreadyInAnOrderException, ItemNotFoundException {

        when(itemService.deleteById(1L)).thenThrow(ItemNotFoundException.class);

        Assertions.assertThrows(ItemNotFoundException.class, () -> {
            itemController.deleteItem(1L);
        });

        verify(itemService, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteItem() throws ItemAlreadyInAnOrderException, ItemNotFoundException {

        ItemResponse itemResponse = new ItemResponse(1L,"Item", "Item1", "Item1", "S", 1L, "Image", null);

        when(itemService.deleteById(1L)).thenReturn(itemResponse);

        ResponseEntity<ItemResponse> response = itemController.deleteItem(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemResponse, response.getBody());

        verify(itemService, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateItemItemNotFound() throws ItemNotFoundException {

        ItemDTO itemDTO = new ItemDTO("Item", "Item1", "Item1", "S", 1L, "Image");

        when(itemService.updateItem(1L, itemDTO)).thenThrow(ItemNotFoundException.class);

        Assertions.assertThrows(ItemNotFoundException.class, () -> {
            itemController.updateItem(1L, itemDTO);
        });

        verify(itemService, times(1)).updateItem(1L, itemDTO);
    }

    @Test
    void testUpdateItem() throws ItemNotFoundException {

        ItemDTO itemDTO = new ItemDTO("Item", "Item1", "Item1", "S", 1L, "Image");
        CategoryResponse categoryResponse = new CategoryResponse(1L, "Category1");
        ItemResponse itemResponse = new ItemResponse(1L,"Item", "Item1", "Item1", "S", 1L, "Image", categoryResponse);

        when(itemService.updateItem(1L, itemDTO)).thenReturn(itemResponse);


        ResponseEntity<ItemResponse> response = itemController.updateItem(1L, itemDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemResponse, response.getBody());

        verify(itemService, times(1)).updateItem(1L, itemDTO);
    }
}
