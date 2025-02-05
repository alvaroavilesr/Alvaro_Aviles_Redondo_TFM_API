package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.ItemDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.NoItemsFoundException;
import es.upm.tfm.adapters.mysqldb.response.ItemResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPersistence {

    ItemResponse saveItem(String category, ItemDTO itemDTO) throws CategoryNotFoundException;

    List<ItemResponse> getStock() throws NoItemsFoundException;

    ItemResponse findById(Long id) throws ItemNotFoundException;
}
