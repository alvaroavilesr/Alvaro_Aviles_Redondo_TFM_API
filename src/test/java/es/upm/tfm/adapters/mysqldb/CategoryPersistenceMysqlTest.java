package es.upm.tfm.adapters.mysqldb;

import es.upm.tfm.adapters.mysqldb.dto.CategoryDTO;
import es.upm.tfm.adapters.mysqldb.entity.CategoryEntity;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNameAlreadyExisting;
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

import java.util.List;

import static org.junit.Assert.assertThrows;

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
    }
}
