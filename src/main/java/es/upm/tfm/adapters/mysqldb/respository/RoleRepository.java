package es.upm.tfm.adapters.mysqldb.respository;

import es.upm.tfm.adapters.mysqldb.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
}
