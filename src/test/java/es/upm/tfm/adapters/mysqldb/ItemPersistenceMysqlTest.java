package es.upm.tfm.adapters.mysqldb;

import es.upm.tfm.adapters.mysqldb.dto.ItemDTO;
import es.upm.tfm.adapters.mysqldb.entity.CategoryEntity;
import es.upm.tfm.adapters.mysqldb.entity.ItemEntity;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.NoItemsFoundException;
import es.upm.tfm.adapters.mysqldb.persistence.ItemPersistenceMysql;
import es.upm.tfm.adapters.mysqldb.response.CategoryResponse;
import es.upm.tfm.adapters.mysqldb.response.ItemResponse;
import es.upm.tfm.adapters.mysqldb.respository.CategoryRepository;
import es.upm.tfm.adapters.mysqldb.respository.ItemRepository;
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
class ItemPersistenceMysqlTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ItemPersistenceMysql itemPersistenceMysql;

    @Test
    void SaveItemCategoryNotFound() {

        ItemDTO itemDTO = new ItemDTO("Item", "Item1", "Item1", "S", 1L, "Image");
        CategoryEntity categoryEntity = new CategoryEntity(1L, "Category1");

        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(categoryEntity));

        assertThrows(CategoryNotFoundException.class, () -> {
            itemPersistenceMysql.saveItem("category2", itemDTO);
        });

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void SaveItem() throws CategoryNotFoundException {

        ItemDTO itemDTO = new ItemDTO("Item", "Item1", "Item1", "S", 1L, "Image");
        CategoryEntity categoryEntity = new CategoryEntity(1L, "Category1");
        ItemEntity itemEntity = new ItemEntity(1L,"Item", "Item1", "Item1", "S", 1L, "Image", categoryEntity);

        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(categoryEntity));
        Mockito.when(itemRepository.save(Mockito.any(ItemEntity.class))).thenReturn(itemEntity);

        ItemResponse itemResponse = itemPersistenceMysql.saveItem("Category1", itemDTO);

        Assertions.assertEquals(itemResponse.getName(), itemEntity.getName());
        Assertions.assertEquals(itemResponse.getDescription(), itemEntity.getDescription());
        Assertions.assertEquals(itemResponse.getCategory().getName(), itemEntity.getCategory().getName());

        verify(categoryRepository, times(1)).findAll();
        verify(itemRepository, times(1)).save(Mockito.any(ItemEntity.class));
    }

    @Test
    void GetStockNoItemsFound() {

        Mockito.when(itemRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NoItemsFoundException.class, () -> {
            itemPersistenceMysql.getStock();
        });

        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void GetStock() {

        CategoryEntity categoryEntity = new CategoryEntity(1L, "Category1");
        ItemEntity itemEntity = new ItemEntity(1L,"Item", "Item1", "Item1", "S", 1L, "Image", categoryEntity);
        CategoryResponse categoryResponse = new CategoryResponse(1L, "Category1");
        ItemResponse itemResponse = new ItemResponse(1L,"Item", "Item1", "Item1", "S", 1L, "Image", categoryResponse);

        Mockito.when(itemRepository.findAll()).thenReturn(List.of(itemEntity));

        List<ItemResponse> response = itemPersistenceMysql.getStock();

        Assertions.assertEquals(response, List.of(itemResponse));

        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void FindByIdItemNotFound() {

        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> {
            itemPersistenceMysql.findById(1L);
        });

        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void FindById() throws ItemNotFoundException {

        CategoryEntity categoryEntity = new CategoryEntity(1L, "Category1");
        ItemEntity itemEntity = new ItemEntity(1L,"Item", "Item1", "Item1", "S", 1L, "Image", categoryEntity);
        CategoryResponse categoryResponse = new CategoryResponse(1L, "Category1");
        ItemResponse itemResponse = new ItemResponse(1L,"Item", "Item1", "Item1", "S", 1L, "Image", categoryResponse);

        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(itemEntity));

        ItemResponse response = itemPersistenceMysql.findById(1L);

        Assertions.assertEquals(response, itemResponse);

        verify(itemRepository, times(1)).findById(1L);
    }
}
