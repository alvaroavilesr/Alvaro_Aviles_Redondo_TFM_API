package es.upm.tfm.adapters.mysqldb.respository;

import es.upm.tfm.adapters.mysqldb.entity.ItemOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemOrderRepository extends JpaRepository<ItemOrderEntity, Long> {
}
