package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.NewUserDTO;
import es.upm.tfm.adapters.mysqldb.dto.RoleDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotValidException;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPersistenceTest {

    @Mock
    private UserPersistence userPersistence;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterNewUser() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");
        RoleResponse roleResponse = new RoleResponse("User", "User role");
        Set<RoleResponse> roleResponses = Set.of(roleResponse);
        UserResponse userResponse = new UserResponse("User1",  "User", "1", "user@example.com", roleResponses);

        when(userService.registerNewUser(newUserDTO)).thenReturn(userResponse);

        UserResponse response = userPersistence.registerNewUser(newUserDTO);

        assertEquals(response, userResponse);
    }

    @Test
    public void testRegisterNewUserExceptionNotValid() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");

        when(userService.registerNewUser(newUserDTO)).thenThrow(RoleNotFoundException.class);

        Assertions.assertThrows(RoleNotFoundException.class, () -> {
            userPersistence.registerNewUser(newUserDTO);
        });
    }

    @Test
    public void testRegisterNewUserExceptionAlreadyExisting() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");

        when(userService.registerNewUser(newUserDTO)).thenThrow(UserAlreadyExistingException.class);

        Assertions.assertThrows(UserAlreadyExistingException.class, () -> {
            userPersistence.registerNewUser(newUserDTO);
        });
    }

    @Test
    public void testCreateNewUser() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("Admin1",  "Admin", "1", "admin@example.com", "123");
        RoleResponse roleResponse = new RoleResponse("Admin", "Admin role");
        Set<RoleResponse> roleResponses = Set.of(roleResponse);
        UserResponse userResponse = new UserResponse("Admin1",  "Admin", "1", "admin@example.com", roleResponses);

        when(userService.createUser(newUserDTO, "Admin")).thenReturn(userResponse);

        UserResponse response = userPersistence.createUser(newUserDTO, "Admin");

        assertEquals(response, userResponse);
    }

    @Test
    public void testCreateNewUserExceptionNotValid() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");

        when(userService.createUser(newUserDTO, "User")).thenThrow(RoleNotFoundException.class);

        Assertions.assertThrows(RoleNotFoundException.class, () -> {
            userPersistence.createUser(newUserDTO, "User");
        });
    }

    @Test
    public void testCreateNewUserExceptionAlreadyExisting() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");

        when(userService.createUser(newUserDTO, "User")).thenThrow(UserAlreadyExistingException.class);

        Assertions.assertThrows(UserAlreadyExistingException.class, () -> {
            userPersistence.createUser(newUserDTO, "User");
        });
    }

}
