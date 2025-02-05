package es.upm.tfm.adapters.mysqldb.persistence;

import es.upm.tfm.adapters.mysqldb.dto.ItemDTO;
import es.upm.tfm.adapters.mysqldb.entity.CategoryEntity;
import es.upm.tfm.adapters.mysqldb.entity.ItemEntity;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.NoItemsFoundException;
import es.upm.tfm.adapters.mysqldb.response.ItemResponse;
import es.upm.tfm.adapters.mysqldb.respository.CategoryRepository;
import es.upm.tfm.adapters.mysqldb.respository.ItemRepository;
import es.upm.tfm.domain.persistence_ports.ItemPersistence;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ItemPersistenceMysql implements ItemPersistence {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public ItemPersistenceMysql(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ItemResponse saveItem(String category, ItemDTO itemDTO) throws CategoryNotFoundException {
        List<CategoryEntity> categories = categoryRepository.findAll().stream()
                .filter(cat -> cat.getName().equals(category)).toList();
        if (categories.isEmpty()){
            throw new CategoryNotFoundException(null);
        }
        ItemEntity stockItemEntity = modelMapper.map(itemDTO, ItemEntity.class);
        stockItemEntity.setCategory(categories.get(0));
        ItemEntity savedStockItem = itemRepository.save(stockItemEntity);
        return modelMapper.map(savedStockItem, ItemResponse.class);
    }

    public List<ItemResponse> getStock() throws NoItemsFoundException {
        List<ItemEntity> stock = itemRepository.findAll();

        if (stock.isEmpty()){
            throw new NoItemsFoundException();
        }else{
            return stock.stream()
                    .map(item -> modelMapper.map(item, ItemResponse.class))
                    .toList();
        }
    }
}
