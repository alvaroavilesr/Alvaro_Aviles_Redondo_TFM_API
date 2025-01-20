package es.upm.tfm.adapters.mysqldb.persistence;

import es.upm.tfm.adapters.mysqldb.dto.RoleDTO;
import es.upm.tfm.adapters.mysqldb.dto.RoleDescriptionDTO;
import es.upm.tfm.adapters.mysqldb.entity.RoleEntity;
import es.upm.tfm.adapters.mysqldb.exception.role.*;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import es.upm.tfm.adapters.mysqldb.respository.RoleRepository;
import es.upm.tfm.adapters.mysqldb.respository.UserRepository;
import es.upm.tfm.domain.persistence_ports.RolePersistence;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public List<RoleResponse> getRoles () throws RolesNotFoundException {
        List<RoleEntity> roles = roleRepository.findAll();

        if (roles.isEmpty()){
            throw new RolesNotFoundException();
        }else{
            return roles.stream()
                    .map(rol -> modelMapper.map(rol, RoleResponse.class))
                    .toList();
        }
    }

    @Transactional
    public RoleResponse getRole(String roleName) throws RoleNotFoundException {
        return roleRepository.findById(roleName)
                .map(role -> modelMapper.map(role, RoleResponse.class))
                .orElseThrow(() -> new RoleNotFoundException(roleName));
    }

    public RoleResponse deleteRole(String roleName) throws RoleNotFoundException, RoleAlreadyAssignedException {
        boolean roleNotAssignedToAnyUser = userRepository.findAll().stream()
                .noneMatch(user -> Objects.equals(user.getRole().iterator().next().getRoleName(), roleName));
        if(!roleNotAssignedToAnyUser){
            throw new RoleAlreadyAssignedException(roleName);
        }
        RoleResponse response = roleRepository.findById(roleName)
                .map(role -> modelMapper.map(role, RoleResponse.class))
                .orElseThrow(() -> new RoleNotFoundException(roleName));
        roleRepository.deleteById(roleName);
        return response;
    }

    public RoleResponse updateRoleDescription(RoleDescriptionDTO roleDescriptionDTO, String roleName) throws RoleNotFoundException {
        RoleEntity role = roleRepository.findById(roleName).orElseThrow(() -> new RoleNotFoundException(roleName));
        role.setRoleDescription(roleDescriptionDTO.getRoleDescription());
        return modelMapper.map(roleRepository.save(role), RoleResponse.class);
    }
}
