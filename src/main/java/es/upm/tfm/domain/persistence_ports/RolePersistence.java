package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.RoleDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotValidException;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePersistence {

    RoleResponse createRole (RoleDTO roleDTO) throws RoleNotValidException, RoleAlreadyExistingException;
}
