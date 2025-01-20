package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.NewUserDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import es.upm.tfm.adapters.mysqldb.response.UserResponse;
import es.upm.tfm.domain.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testRegisterNewUser() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");
        RoleResponse roleResponse = new RoleResponse("User", "User role");
        Set<RoleResponse> roleResponses = Set.of(roleResponse);
        UserResponse userResponse = new UserResponse("User1",  "User", "1", "user@example.com", roleResponses);

        when(userService.registerNewUser(newUserDTO)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.registerNewUser(newUserDTO);

        assertEquals(userResponse, response.getBody());

        verify(userService, times(1)).registerNewUser(newUserDTO);
    }

    @Test
    public void testRegisterNewUserExceptionNotFound() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");

        when(userService.registerNewUser(newUserDTO)).thenThrow(RoleNotFoundException.class);

        Assertions.assertThrows(RoleNotFoundException.class, () -> {
            userController.registerNewUser(newUserDTO);
        });

        verify(userService, times(1)).registerNewUser(newUserDTO);
    }

    @Test
    public void testRegisterNewUserExceptionAlreadyExisting() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");

        when(userService.registerNewUser(newUserDTO)).thenThrow(UserAlreadyExistingException.class);

        Assertions.assertThrows(UserAlreadyExistingException.class, () -> {
            userController.registerNewUser(newUserDTO);
        });

        verify(userService, times(1)).registerNewUser(newUserDTO);
    }

    @Test
    public void testCreateUser() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");
        RoleResponse roleResponse = new RoleResponse("User", "User role");
        Set<RoleResponse> roleResponses = Set.of(roleResponse);
        UserResponse userResponse = new UserResponse("User1",  "User", "1", "user@example.com", roleResponses);

        when(userService.createUser(newUserDTO, "User")).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.createUser(newUserDTO, "User");

        assertEquals(userResponse, response.getBody());

        verify(userService, times(1)).createUser(newUserDTO, "User");
    }

    @Test
    public void testCreateUserExceptionNotFound() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");

        when(userService.createUser(newUserDTO, "Super admin")).thenThrow(RoleNotFoundException.class);

        Assertions.assertThrows(RoleNotFoundException.class, () -> {
            userController.createUser(newUserDTO, "Super admin");
        });

        verify(userService, times(1)).createUser(newUserDTO, "Super admin");
    }

    @Test
    public void testCreateUserExceptionAlreadyExisting() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");

        when(userService.createUser(newUserDTO, "User")).thenThrow(UserAlreadyExistingException.class);

        Assertions.assertThrows(UserAlreadyExistingException.class, () -> {
            userController.createUser(newUserDTO, "User");
        });

        verify(userService, times(1)).createUser(newUserDTO, "User");
    }
}
