package es.upm.tfm.adapters.mysqldb.respository;

import es.upm.tfm.adapters.mysqldb.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
}

