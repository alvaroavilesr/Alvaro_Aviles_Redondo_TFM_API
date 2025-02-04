package es.upm.tfm.adapters.mysqldb.respository;

import es.upm.tfm.adapters.mysqldb.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}
