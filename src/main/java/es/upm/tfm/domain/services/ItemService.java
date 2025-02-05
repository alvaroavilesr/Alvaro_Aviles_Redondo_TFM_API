package es.upm.tfm.domain.services;

import es.upm.tfm.adapters.mysqldb.dto.ItemDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.ItemResponse;
import es.upm.tfm.domain.persistence_ports.ItemPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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

}
