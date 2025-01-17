package es.upm.tfm.domain.services;

import es.upm.tfm.adapters.mysqldb.dto.RoleDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotValidException;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import es.upm.tfm.domain.persistence_ports.RolePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RolePersistence rolePersistence;

    @Autowired
    @Lazy
    public RoleService(RolePersistence rolePersistence) {
        this.rolePersistence = rolePersistence;
    }

    public RoleResponse createRole(RoleDTO roleDTO) throws RoleNotValidException, RoleAlreadyExistingException {
        return this.rolePersistence.createRole(roleDTO);
    }
}
