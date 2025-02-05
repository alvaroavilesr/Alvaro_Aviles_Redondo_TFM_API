package es.upm.tfm.domain.services;

import es.upm.tfm.adapters.mysqldb.dto.ItemDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.NoItemsFoundException;
import es.upm.tfm.adapters.mysqldb.response.CategoryResponse;
import es.upm.tfm.adapters.mysqldb.response.ItemResponse;
import es.upm.tfm.domain.persistence_ports.ItemPersistence;
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
class ItemServiceTest {

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

        when(itemService.getStock()).thenThrow(NoItemsFoundException.class);

        Assertions.assertThrows(NoItemsFoundException.class, () -> {
            itemService.getStock();
        });
    }

    @Test
    void testGetItems() throws NoItemsFoundException {

        CategoryResponse categoryResponse = new CategoryResponse(1L, "Category1");
        ItemResponse itemResponse = new ItemResponse(1L,"Item", "Item1", "Item1", "S", 1L, "Image", categoryResponse);

        when(itemService.getStock()).thenReturn(List.of(itemResponse));

        List<ItemResponse> response = itemService.getStock();

        assertEquals(List.of(itemResponse), response);
    }
}
