package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.NewUserDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.response.UserResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPersistence {

    UserResponse registerNewUser (NewUserDTO newUserDTO) throws UserAlreadyExistingException, RoleNotFoundException;
}
