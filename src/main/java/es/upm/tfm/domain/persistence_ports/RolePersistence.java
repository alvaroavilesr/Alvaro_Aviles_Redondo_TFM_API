package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.RoleDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotValidException;
import es.upm.tfm.adapters.mysqldb.exception.role.RolesNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePersistence {

    RoleResponse createRole (RoleDTO roleDTO) throws RoleNotValidException, RoleAlreadyExistingException;

    List<RoleResponse> getRoles () throws RolesNotFoundException;

    RoleResponse getRole(String roleName) throws RoleNotFoundException;
}
