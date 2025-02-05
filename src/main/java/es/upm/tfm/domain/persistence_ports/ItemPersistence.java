package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.ItemDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.ItemResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPersistence {

    ItemResponse saveItem(String category, ItemDTO itemDTO) throws CategoryNotFoundException;

}
