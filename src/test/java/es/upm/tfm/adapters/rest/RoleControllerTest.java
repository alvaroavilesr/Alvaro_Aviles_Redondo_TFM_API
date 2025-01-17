package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.RoleDTO;
import es.upm.tfm.adapters.mysqldb.dto.RoleDescriptionDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.*;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import es.upm.tfm.domain.services.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @Test
    public void testCreateRole() throws RoleNotValidException, RoleAlreadyExistingException {
        RoleDTO roleDTO = new RoleDTO("Admin", "Admin role");
        RoleResponse roleResponse = new RoleResponse("Admin", "Admin role");

        when(roleService.createRole(roleDTO)).thenReturn(roleResponse);

        ResponseEntity<RoleResponse> response = roleController.createRole(roleDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roleResponse, response.getBody());

        verify(roleService, times(1)).createRole(roleDTO);
    }

    @Test
    public void testCreateRoleExceptionNotValid() throws RoleNotValidException, RoleAlreadyExistingException {
        RoleDTO roleDTO = new RoleDTO("SuperAdmin", "Super admin user role");

        when(roleService.createRole(roleDTO)).thenThrow(RoleNotValidException.class);

        Assertions.assertThrows(RoleNotValidException.class, () -> {
            roleController.createRole(roleDTO);
        });

        verify(roleService, times(1)).createRole(roleDTO);
    }

    @Test
    public void testCreateRoleExceptionAlreadyExisting() throws RoleNotValidException, RoleAlreadyExistingException {
        RoleDTO roleDTO = new RoleDTO("SuperAdmin", "Super admin user role");

        when(roleService.createRole(roleDTO)).thenThrow(RoleAlreadyExistingException.class);

        Assertions.assertThrows(RoleAlreadyExistingException.class, () -> {
            roleController.createRole(roleDTO);
        });

        verify(roleService, times(1)).createRole(roleDTO);
    }

    @Test
    public void testGetRoles() throws RolesNotFoundException {
        RoleResponse roleResponse = new RoleResponse("Admin", "Admin role");
        List<RoleResponse> roleResponses = Collections.singletonList(roleResponse);

        when(roleService.getRoles()).thenReturn(roleResponses);

        ResponseEntity<List<RoleResponse>> response = roleController.getRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roleResponses, response.getBody());

        verify(roleService, times(1)).getRoles();
    }

    @Test
    public void testGetRolesExceptionNotFound() throws RolesNotFoundException {
        when(roleService.getRoles()).thenThrow(RolesNotFoundException.class);

        Assertions.assertThrows(RolesNotFoundException.class, () -> {
            roleController.getRoles();
        });

        verify(roleService, times(1)).getRoles();
    }

    @Test
    public void testGetRole() throws RoleNotFoundException {
        RoleResponse roleResponse = new RoleResponse("Admin", "Admin role");

        when(roleService.getRole("Admin")).thenReturn(roleResponse);

        ResponseEntity<RoleResponse> response = roleController.getRole("Admin");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roleResponse, response.getBody());

        verify(roleService, times(1)).getRole("Admin");
    }

    @Test
    public void testGetRoleNotFound() throws RoleNotFoundException {
        when(roleService.getRole("Super Admin")).thenThrow(RoleNotFoundException.class);

        Assertions.assertThrows(RoleNotFoundException.class, () -> {
            roleController.getRole("Super Admin");
        });

        verify(roleService, times(1)).getRole("Super Admin");
    }

    @Test
    public void testDeleteRole() throws RoleNotFoundException, RoleAlreadyAssignedException {
        RoleResponse roleResponse = new RoleResponse("Admin", "Admin role");

        when(roleService.deleteRole("Admin")).thenReturn(roleResponse);

        ResponseEntity<RoleResponse> response = roleController.deleteRole("Admin");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roleResponse, response.getBody());

        verify(roleService, times(1)).deleteRole("Admin");
    }

    @Test
    public void testDeleteRoleExceptionNotFound() throws RoleNotFoundException, RoleAlreadyAssignedException {
        when(roleService.deleteRole("Super Admin")).thenThrow(RoleNotFoundException.class);

        Assertions.assertThrows(RoleNotFoundException.class, () -> {
            roleController.deleteRole("Super Admin");
        });

        verify(roleService, times(1)).deleteRole("Super Admin");
    }

    @Test
    public void testDeleteRoleExceptionAlreadyAssigned() throws RoleNotFoundException, RoleAlreadyAssignedException {
        when(roleService.deleteRole("Admin")).thenThrow(RoleAlreadyAssignedException.class);

        Assertions.assertThrows(RoleAlreadyAssignedException.class, () -> {
            roleController.deleteRole("Admin");
        });

        verify(roleService, times(1)).deleteRole("Admin");
    }

    @Test
    public void testUpdateRoleDescription() throws RoleNotFoundException {
        RoleDescriptionDTO roleDescriptionDTO = new RoleDescriptionDTO("New Description");
        RoleResponse roleResponse = new RoleResponse("Admin", "New Description");

        when(roleService.updateRoleDescription(roleDescriptionDTO, "Admin")).thenReturn(roleResponse);

        ResponseEntity<RoleResponse> response = roleController.updateRoleDescription(roleDescriptionDTO, "Admin");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roleResponse, response.getBody());

        verify(roleService, times(1)).updateRoleDescription(roleDescriptionDTO, "Admin");
    }

    @Test
    public void testUpdateRoleDescriptionExceptionNotFound() throws RoleNotFoundException {
        RoleDescriptionDTO roleDescriptionDTO = new RoleDescriptionDTO("New Description");
        when(roleService.updateRoleDescription(roleDescriptionDTO, "Super Admin")).thenThrow(RoleNotFoundException.class);

        Assertions.assertThrows(RoleNotFoundException.class, () -> {
            roleController.updateRoleDescription(roleDescriptionDTO, "Super Admin");
        });

        verify(roleService, times(1)).updateRoleDescription(roleDescriptionDTO, "Super Admin");
    }
}
