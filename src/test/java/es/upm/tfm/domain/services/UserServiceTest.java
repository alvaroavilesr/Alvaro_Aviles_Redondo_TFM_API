package es.upm.tfm.domain.services;

import es.upm.tfm.adapters.mysqldb.dto.NewUserDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UsersNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import es.upm.tfm.adapters.mysqldb.response.UserResponse;
import es.upm.tfm.domain.persistence_ports.UserPersistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

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
    public void testCreateRoleExceptionNotValid() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");

        when(userService.registerNewUser(newUserDTO)).thenThrow(RoleNotFoundException.class);

        Assertions.assertThrows(RoleNotFoundException.class, () -> {
            userPersistence.registerNewUser(newUserDTO);
        });
    }

    @Test
    public void testCreateRoleExceptionAlreadyExisting() throws UserAlreadyExistingException, RoleNotFoundException {
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

    @Test
    public void testGetUsers() throws UsersNotFoundException {
        RoleResponse roleResponse = new RoleResponse("User", "User role");
        UserResponse userResponse = new UserResponse("User1", "Alvaro", "Aviles", "alvaro@gmail.com",Set.of(roleResponse));

        when(userService.getUsers()).thenReturn(List.of(userResponse));

        List<UserResponse> response = userPersistence.getUsers();

        assertEquals(List.of(userResponse), response);
    }

    @Test
    public void testGetUsersExceptionUsersNotFound() throws UsersNotFoundException {
        when(userService.getUsers()).thenThrow(UsersNotFoundException.class);

        Assertions.assertThrows(UsersNotFoundException.class, () -> {
            userPersistence.getUsers();
        });
    }

    @Test
    public void testGetUser() throws UserNotFoundException, UserNameNotValid {
        RoleResponse roleResponse = new RoleResponse("User", "User role");
        UserResponse userResponse = new UserResponse("User1", "Alvaro", "Aviles", "alvaro@gmail.com",Set.of(roleResponse));

        when(userService.getUser("User1")).thenReturn(userResponse);

        UserResponse response = userPersistence.getUser("User1");

        assertEquals(userResponse, response);
    }

    @Test
    public void testGetUserExceptionUserNotFound() throws UserNotFoundException, UserNameNotValid {
        when(userService.getUser("User1")).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userPersistence.getUser("User1");
        });
    }

    @Test
    public void testGetUserExceptionUserNameNotValid() throws UserNotFoundException, UserNameNotValid {
        when(userService.getUser("User1")).thenThrow(UserNameNotValid.class);

        Assertions.assertThrows(UserNameNotValid.class, () -> {
            userPersistence.getUser("User1");
        });
    }
}
