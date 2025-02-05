package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.ItemDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
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
class ItemPersistenceTest {

    @Mock
    private ItemPersistence itemPersistence;

    @InjectMocks
    private ItemService itemService;

    @Test
    void testSaveItem() throws CategoryNotFoundException {

        ItemDTO itemDTO = new ItemDTO("Item", "Item1", "Item1", "S", 1L, "Image");
        CategoryResponse categoryResponse = new CategoryResponse(1L, "Category1");
        ItemResponse itemResponse= new ItemResponse(1L,"Item", "Item1", "Item1", "S", 1L, "Image", categoryResponse);

        when(itemPersistence.saveItem("Category1", itemDTO)).thenReturn(itemResponse);

        ItemResponse response = itemService.saveItem("Category1", itemDTO);

        assertEquals(itemResponse, response);
    }

    @Test
    void testSaveItemCategoryNotFound() throws CategoryNotFoundException {
        ItemDTO itemDTO = new ItemDTO("Item", "Item1", "Item1", "S", 1L, "Image");

        when(itemPersistence.saveItem("Category1", itemDTO)).thenThrow(CategoryNotFoundException.class);

        Assertions.assertThrows(CategoryNotFoundException.class, () -> {
            itemService.saveItem("Category1", itemDTO);
        });
    }

    @Test
    void testGetItemsNoItemsFound() throws NoItemsFoundException {

        when(itemPersistence.getStock()).thenThrow(NoItemsFoundException.class);

        Assertions.assertThrows(NoItemsFoundException.class, () -> {
            itemService.getStock();
        });
    }

    @Test
    void testGetItems() throws NoItemsFoundException {

        CategoryResponse categoryResponse = new CategoryResponse(1L, "Category1");
        ItemResponse itemResponse = new ItemResponse(1L,"Item", "Item1", "Item1", "S", 1L, "Image", categoryResponse);

        when(itemPersistence.getStock()).thenReturn(List.of(itemResponse));

        List<ItemResponse> response = itemService.getStock();

        assertEquals(List.of(itemResponse), response);
    }

    @Test
    void testGetItemNo() throws ItemNotFoundException {

        when(itemPersistence.findById(1L)).thenThrow(ItemNotFoundException.class);

        Assertions.assertThrows(ItemNotFoundException.class, () -> {
            itemService.findById(1L);
        });
    }

    @Test
    void testGet() throws ItemNotFoundException {

        CategoryResponse categoryResponse = new CategoryResponse(1L, "Category1");
        ItemResponse itemResponse = new ItemResponse(1L,"Item", "Item1", "Item1", "S", 1L, "Image", categoryResponse);

        when(itemPersistence.findById(1L)).thenReturn(itemResponse);

        ItemResponse response = itemService.findById(1L);

        assertEquals(itemResponse, response);

    }
}
