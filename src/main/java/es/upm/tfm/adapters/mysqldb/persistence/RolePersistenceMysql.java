package es.upm.tfm.adapters.mysqldb.persistence;

import es.upm.tfm.adapters.mysqldb.dto.RoleDTO;
import es.upm.tfm.adapters.mysqldb.entity.RoleEntity;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotValidException;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import es.upm.tfm.adapters.mysqldb.respository.RoleRepository;
import es.upm.tfm.adapters.mysqldb.respository.UserRepository;
import es.upm.tfm.domain.persistence_ports.RolePersistence;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RolePersistenceMysql implements RolePersistence {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public RolePersistenceMysql(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public RoleResponse createRole(RoleDTO roleDTO) throws RoleNotValidException, RoleAlreadyExistingException {
        List<String> permittedRoleList = new ArrayList<>();
        permittedRoleList.add("User");
        permittedRoleList.add("Vendor");
        permittedRoleList.add("Admin");
        if(permittedRoleList.stream().noneMatch(str -> str.trim().equals(roleDTO.getRoleName()))){
            throw new RoleNotValidException (roleDTO.getRoleName());
        }
        if(roleRepository.findAll().stream().anyMatch(str -> str.getRoleName().trim().equals(roleDTO.getRoleName()))){
            throw new RoleAlreadyExistingException(roleDTO.getRoleName());
        }
        RoleEntity role = modelMapper.map(roleDTO, RoleEntity.class);
        return modelMapper.map(roleRepository.save(role),RoleResponse.class);
    }
}
