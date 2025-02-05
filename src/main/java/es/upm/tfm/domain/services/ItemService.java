package es.upm.tfm.domain.services;

import es.upm.tfm.adapters.mysqldb.dto.ItemDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemAlreadyInAnOrderException;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.NoItemsFoundException;
import es.upm.tfm.adapters.mysqldb.response.ItemResponse;
import es.upm.tfm.domain.persistence_ports.ItemPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemPersistence itemPersistence;

    @Autowired
    @Lazy
    public ItemService(ItemPersistence itemPersistence) {
        this.itemPersistence = itemPersistence;
    }

    public ItemResponse saveItem(String category, ItemDTO itemDTO) throws CategoryNotFoundException {
        return this.itemPersistence.saveItem(category, itemDTO);
    }

    public List<ItemResponse> getStock() throws NoItemsFoundException {
        return this.itemPersistence.getStock();
    }

    public ItemResponse findById(Long id) throws ItemNotFoundException {
        return this.itemPersistence.findById(id);
    }

    public ItemResponse deleteById(Long id) throws ItemNotFoundException, ItemAlreadyInAnOrderException {
        return this.itemPersistence.deleteById(id);
    }

    public ItemResponse updateItem(Long id, ItemDTO itemDTO) throws ItemNotFoundException {
        return this.itemPersistence.updateItem(id, itemDTO);
    }
}
