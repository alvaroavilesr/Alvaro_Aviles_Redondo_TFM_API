package es.upm.tfm.adapters.mysqldb.persistence;

import es.upm.tfm.adapters.mysqldb.dto.ItemDTO;
import es.upm.tfm.adapters.mysqldb.entity.CategoryEntity;
import es.upm.tfm.adapters.mysqldb.entity.ItemEntity;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemAlreadyInAnOrderException;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.NoItemsFoundException;
import es.upm.tfm.adapters.mysqldb.response.ItemResponse;
import es.upm.tfm.adapters.mysqldb.respository.CategoryRepository;
import es.upm.tfm.adapters.mysqldb.respository.ItemOrderRepository;
import es.upm.tfm.adapters.mysqldb.respository.ItemRepository;
import es.upm.tfm.domain.persistence_ports.ItemPersistence;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Repository
public class ItemPersistenceMysql implements ItemPersistence {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemOrderRepository itemOrderRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public ItemPersistenceMysql(ItemRepository itemRepository, CategoryRepository categoryRepository, ItemOrderRepository itemOrderRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.itemOrderRepository = itemOrderRepository;
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

    public ItemResponse findById(Long id) throws ItemNotFoundException {
        return itemRepository.findById(id)
                .map(item -> modelMapper.map(item, ItemResponse.class))
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    public ItemResponse deleteById(Long id) throws ItemNotFoundException, ItemAlreadyInAnOrderException {
        boolean itemAlreadyInAnOrder = itemOrderRepository.findAll().stream()
                .noneMatch(itemOrder -> Objects.equals(itemOrder.getItem().getItemId(), id));
        if(!itemAlreadyInAnOrder){
            throw new ItemAlreadyInAnOrderException(id);
        }
        ItemResponse response = itemRepository.findById(id)
                .map(item -> modelMapper.map(item, ItemResponse.class))
                .orElseThrow(() -> new ItemNotFoundException(id));
        itemRepository.deleteById(id);
        return response;
    }

    public ItemResponse updateItem(Long id, ItemDTO itemDTO) throws ItemNotFoundException {
        ItemEntity item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        item.setName(itemDTO.getName());
        item.setDescription(itemDTO.getDescription());
        item.setPrice(itemDTO.getPrice());
        item.setLongDescription(itemDTO.getLongDescription());
        item.setSize(itemDTO.getSize());
        item.setImage(itemDTO.getImage());
        return modelMapper.map(itemRepository.save(item), ItemResponse.class);
    }
}
