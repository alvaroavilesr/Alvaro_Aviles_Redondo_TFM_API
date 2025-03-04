package es.upm.tfm.adapters.mysqldb.respository;

import es.upm.tfm.adapters.mysqldb.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
