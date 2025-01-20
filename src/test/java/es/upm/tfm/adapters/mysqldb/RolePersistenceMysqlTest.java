package es.upm.tfm.adapters.mysqldb;

import es.upm.tfm.adapters.mysqldb.dto.RoleDTO;
import es.upm.tfm.adapters.mysqldb.dto.RoleDescriptionDTO;
import es.upm.tfm.adapters.mysqldb.entity.RoleEntity;
import es.upm.tfm.adapters.mysqldb.entity.UserEntity;
import es.upm.tfm.adapters.mysqldb.exception.role.*;
import es.upm.tfm.adapters.mysqldb.persistence.RolePersistenceMysql;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import es.upm.tfm.adapters.mysqldb.respository.RoleRepository;
import es.upm.tfm.adapters.mysqldb.respository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RolePersistenceMysqlTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RolePersistenceMysql rolePersistenceMysql;

    @Test
    public void CreateRoleRoleNotValid() throws RoleAlreadyExistingException, RoleNotValidException {
        RoleDTO roleDTO = new RoleDTO("SuperAdmin", "Super admin user role");

        assertThrows(RoleNotValidException.class, () -> {
            rolePersistenceMysql.createRole(roleDTO);
        });
    }

    @Test
    void CreateRoleRoleAlreadyExisting() {
        RoleDTO roleDTO = new RoleDTO("Admin", "Admin role");
        RoleEntity role = new RoleEntity("Admin", "Admin role");

        Mockito.when(roleRepository.findAll()).thenReturn(List.of(role));

        assertThrows(RoleAlreadyExistingException.class, () -> {
            rolePersistenceMysql.createRole(roleDTO);
        });

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void CreateRole() throws RoleAlreadyExistingException, RoleNotValidException {
        RoleDTO roleDTO = new RoleDTO("Admin", "Admin role");
        RoleEntity role = new RoleEntity("Admin", "Admin role");
        RoleResponse roleResponse = new RoleResponse("Admin", "Admin role");

        Mockito.when(roleRepository.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(roleRepository.save(Mockito.any(RoleEntity.class))).thenReturn(role);

        RoleResponse response = rolePersistenceMysql.createRole(roleDTO);

        Assertions.assertEquals(response, roleResponse);

        verify(roleRepository, times(1)).findAll();
        verify(roleRepository, times(1)).save(Mockito.any(RoleEntity.class));
    }

    @Test
    void GetRolesNoRolesFound() {
        Mockito.when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(RolesNotFoundException.class, () -> {
            rolePersistenceMysql.getRoles();
        });

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void GetRoles() throws RolesNotFoundException {
        RoleEntity role = new RoleEntity("Admin", "Admin role");
        RoleResponse roleResponse = new RoleResponse("Admin", "Admin role");

        Mockito.when(roleRepository.findAll()).thenReturn(List.of(role));

        List<RoleResponse> response = rolePersistenceMysql.getRoles();

        Assertions.assertEquals(response, List.of(roleResponse));

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void GetRoleNotFound() {
        Mockito.when(roleRepository.findById("Admin")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> {
            rolePersistenceMysql.getRole("Admin");
        });

        verify(roleRepository, times(1)).findById("Admin");
    }

    @Test
    void GetRole() throws RoleNotFoundException {
        RoleEntity role = new RoleEntity("Admin", "Admin role");
        RoleResponse roleResponse = new RoleResponse("Admin", "Admin role");

        Mockito.when(roleRepository.findById("Admin")).thenReturn(Optional.of(role));

        RoleResponse response = rolePersistenceMysql.getRole("Admin");

        Assertions.assertEquals(response, roleResponse);

        verify(roleRepository, times(1)).findById("Admin");
    }

    @Test
    void DeleteRoleRoleAlreadyAssigned(){
        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity role = new RoleEntity("Admin", "Role for admins");
        roles.add(role);

        UserEntity user = new UserEntity("User1", "Alvaro", "Avilés", roles);

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        assertThrows(RoleAlreadyAssignedException.class, () -> {
            rolePersistenceMysql.deleteRole("Admin");
        });
    }

    @Test
    void DeleteRoleRoleNotFound(){

        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(roleRepository.findById("Admin")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> {
            rolePersistenceMysql.deleteRole("Admin");
        });
    }

    @Test
    void DeleteRole() throws RoleNotFoundException, RoleAlreadyAssignedException {
        RoleEntity role = new RoleEntity("Admin", "Admin role");
        RoleResponse roleResponse = new RoleResponse("Admin", "Admin role");

        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(roleRepository.findById("Admin")).thenReturn(Optional.of(role
        ));

        RoleResponse response = rolePersistenceMysql.deleteRole("Admin");

        Assertions.assertEquals(response, roleResponse);
    }

    @Test
    void UpdateRoleDescriptionRoleNotFound(){
        Mockito.when(roleRepository.findById("Admin")).thenReturn(Optional.empty());

        RoleDescriptionDTO roleDescriptionDTO = new RoleDescriptionDTO("new desc");

        assertThrows(RoleNotFoundException.class, () -> {
            rolePersistenceMysql.updateRoleDescription(roleDescriptionDTO, "Admin");
        });

        verify(roleRepository, times(1)).findById("Admin");
    }

    @Test
    void UpdateRoleDescription() throws RoleNotFoundException {
        RoleEntity role = new RoleEntity("Admin", "Admin role");
        RoleResponse roleResponse = new RoleResponse("Admin", "New Description");

        Mockito.when(roleRepository.findById("Admin")).thenReturn(Optional.of(role));
        Mockito.when(roleRepository.save(role)).thenReturn(role);

        RoleDescriptionDTO roleDescriptionDTO = new RoleDescriptionDTO("New Description");

        RoleResponse response = rolePersistenceMysql.updateRoleDescription(roleDescriptionDTO, "Admin");

        Assertions.assertEquals(response, roleResponse);

        verify(roleRepository, times(1)).findById("Admin");
        verify(roleRepository, times(1)).save(role);
    }

}
