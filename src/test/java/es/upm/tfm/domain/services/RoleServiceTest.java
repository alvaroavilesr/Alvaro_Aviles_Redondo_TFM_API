package es.upm.tfm.domain.services;

import es.upm.tfm.adapters.mysqldb.dto.RoleDTO;
import es.upm.tfm.adapters.mysqldb.dto.RoleDescriptionDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.*;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import es.upm.tfm.domain.persistence_ports.RolePersistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RolePersistence rolePersistence;

    @InjectMocks
    private RoleService roleService;

    @Test
    void testCreateRole() throws RoleNotValidException, RoleAlreadyExistingException {
        RoleDTO roleDTO = new RoleDTO("Admin", "Admin role");
        RoleResponse roleResponse = new RoleResponse("Admin", "Admin role");

        when(roleService.createRole(roleDTO)).thenReturn(roleResponse);

        RoleResponse response = rolePersistence.createRole(roleDTO);

        assertEquals(response, roleResponse);
    }

    @Test
    void testCreateRoleExceptionNotValid() throws RoleNotValidException, RoleAlreadyExistingException {
        RoleDTO roleDTO = new RoleDTO("SuperAdmin", "Super admin user role");

        when(roleService.createRole(roleDTO)).thenThrow(RoleNotValidException.class);

        Assertions.assertThrows(RoleNotValidException.class, () -> {
            rolePersistence.createRole(roleDTO);
        });
    }

    @Test
    void testCreateRoleExceptionAlreadyExisting() throws RoleNotValidException, RoleAlreadyExistingException {
        RoleDTO roleDTO = new RoleDTO("SuperAdmin", "Super admin user role");

        when(roleService.createRole(roleDTO)).thenThrow(RoleAlreadyExistingException.class);

        Assertions.assertThrows(RoleAlreadyExistingException.class, () -> {
            rolePersistence.createRole(roleDTO);
        });
    }

    @Test
    void testGetRoles() throws RolesNotFoundException {
        RoleResponse roleResponse = new RoleResponse("Admin", "Admin role");
        List<RoleResponse> roleResponses = Collections.singletonList(roleResponse);

        when(roleService.getRoles()).thenReturn(roleResponses);

        List<RoleResponse> response = rolePersistence.getRoles();

        assertEquals(response, roleResponses);
    }

    @Test
    void testGetRolesExceptionNotFound() throws RolesNotFoundException {
        when(roleService.getRoles()).thenThrow(RolesNotFoundException.class);

        Assertions.assertThrows(RolesNotFoundException.class, () -> {
            rolePersistence.getRoles();
        });
    }

    @Test
    void testGetRole() throws RoleNotFoundException {
        RoleResponse roleResponse = new RoleResponse("Admin", "Admin role");

        when(roleService.getRole("Admin")).thenReturn(roleResponse);

        RoleResponse response = rolePersistence.getRole("Admin");

        assertEquals(response, roleResponse);
    }

    @Test
    void testGetRoleNotFound() throws RoleNotFoundException {
        when(roleService.getRole("Super Admin")).thenThrow(RoleNotFoundException.class);

        Assertions.assertThrows(RoleNotFoundException.class, () -> {
            rolePersistence.getRole("Super Admin");
        });
    }

    @Test
    void testDeleteRole() throws RoleNotFoundException, RoleAlreadyAssignedException {
        RoleResponse roleResponse = new RoleResponse("Admin", "Admin role");

        when(roleService.deleteRole("Admin")).thenReturn(roleResponse);

        RoleResponse response = rolePersistence.deleteRole("Admin");

        assertEquals(response, roleResponse);
    }

    @Test
    void testDeleteRoleExceptionNotFound() throws RoleNotFoundException, RoleAlreadyAssignedException {
        when(roleService.deleteRole("Super Admin")).thenThrow(RoleNotFoundException.class);

        Assertions.assertThrows(RoleNotFoundException.class, () -> {
            rolePersistence.deleteRole("Super Admin");
        });
    }

    @Test
    void testDeleteRoleExceptionAlreadyAssigned() throws RoleNotFoundException, RoleAlreadyAssignedException {
        when(roleService.deleteRole("Admin")).thenThrow(RoleAlreadyAssignedException.class);

        Assertions.assertThrows(RoleAlreadyAssignedException.class, () -> {
            rolePersistence.deleteRole("Admin");
        });
    }

    @Test
    void testUpdateRoleDescription() throws RoleNotFoundException {
        RoleDescriptionDTO roleDescriptionDTO = new RoleDescriptionDTO("New Description");
        RoleResponse roleResponse = new RoleResponse("Admin", "New Description");

        when(roleService.updateRoleDescription(roleDescriptionDTO, "Admin")).thenReturn(roleResponse);

        RoleResponse response = rolePersistence.updateRoleDescription(roleDescriptionDTO, "Admin");

        assertEquals(response, roleResponse);
    }

    @Test
    void testUpdateRoleDescriptionExceptionNotFound() throws RoleNotFoundException {
        RoleDescriptionDTO roleDescriptionDTO = new RoleDescriptionDTO("New Description");
        when(roleService.updateRoleDescription(roleDescriptionDTO, "Super Admin")).thenThrow(RoleNotFoundException.class);

        Assertions.assertThrows(RoleNotFoundException.class, () -> {
            rolePersistence.updateRoleDescription(roleDescriptionDTO, "Super Admin");
        });
    }
}
