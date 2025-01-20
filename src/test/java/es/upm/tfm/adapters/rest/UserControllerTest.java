package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.NewUserDTO;
import es.upm.tfm.adapters.mysqldb.dto.UpdateUserDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UsersNotFoundException;
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

import java.util.List;
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


    @Test
    public void testGetUsers() throws UsersNotFoundException {
        RoleResponse roleResponse = new RoleResponse("User", "User role");
        UserResponse userResponse = new UserResponse("User1", "Alvaro", "Aviles", "alvaro@gmail.com",Set.of(roleResponse));

        when(userService.getUsers()).thenReturn(List.of(userResponse));

        ResponseEntity<List<UserResponse>> response = userController.getUsers();

        assertEquals(List.of(userResponse), response.getBody());

        verify(userService, times(1)).getUsers();
    }

    @Test
    public void testGetUsersExceptionUsersNotFound() throws UsersNotFoundException {
        when(userService.getUsers()).thenThrow(UsersNotFoundException.class);

        Assertions.assertThrows(UsersNotFoundException.class, () -> {
            userController.getUsers();
        });

        verify(userService, times(1)).getUsers();
    }

    @Test
    public void testGetUser() throws UserNotFoundException, UserNameNotValid {
        RoleResponse roleResponse = new RoleResponse("User", "User role");
        UserResponse userResponse = new UserResponse("User1", "Alvaro", "Aviles", "alvaro@gmail.com",Set.of(roleResponse));

        when(userService.getUser("User1")).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.getUser("User1");

        assertEquals(userResponse, response.getBody());

        verify(userService, times(1)).getUser("User1");
    }

    @Test
    public void testGetUserExceptionUserNotFound() throws UserNotFoundException, UserNameNotValid {
        when(userService.getUser("User1")).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userController.getUser("User1");
        });

        verify(userService, times(1)).getUser("User1");
    }

    @Test
    public void testGetUserExceptionUserNameNotValid() throws UserNotFoundException, UserNameNotValid {
        when(userService.getUser("User1")).thenThrow(UserNameNotValid.class);

        Assertions.assertThrows(UserNameNotValid.class, () -> {
            userController.getUser("User1");
        });

        verify(userService, times(1)).getUser("User1");
    }

    @Test
    public void testDeleteUser() throws UserNotFoundException, UserNameNotValid {
        RoleResponse roleResponse = new RoleResponse("User", "User role");
        UserResponse userResponse = new UserResponse("User1", "Alvaro", "Aviles", "alvaro@gmail.com",Set.of(roleResponse));

        when(userService.deleteUser("User1")).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.deleteUser("User1");

        assertEquals(userResponse, response.getBody());

        verify(userService, times(1)).deleteUser("User1");
    }

    @Test
    public void testDeleteUserExceptionUserNotFound() throws UserNotFoundException, UserNameNotValid {
        when(userService.deleteUser("User1")).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userController.deleteUser("User1");
        });

        verify(userService, times(1)).deleteUser("User1");
    }

    @Test
    public void testDeleteUserExceptionUserNameNotValid() throws UserNotFoundException, UserNameNotValid {
        when(userService.deleteUser("User1")).thenThrow(UserNameNotValid.class);

        Assertions.assertThrows(UserNameNotValid.class, () -> {
            userController.deleteUser("User1");
        });

        verify(userService, times(1)).deleteUser("User1");
    }

    @Test
    public void testUpdateUser() throws UserNotFoundException, UserNameNotValid {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("NewName", "NewSurname", "NewEmail", "NewPass");
        RoleResponse roleResponse = new RoleResponse("User", "User role");
        UserResponse userResponse = new UserResponse("User1", "Alvaro", "Aviles", "alvaro@gmail.com",Set.of(roleResponse));

        when(userService.updateUser(updateUserDTO, "User1")).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.updateUser(updateUserDTO, "User1");

        assertEquals(userResponse, response.getBody());

        verify(userService, times(1)).updateUser(updateUserDTO, "User1");
    }

    @Test
    public void testUpdateUserExceptionUserNotFound() throws UserNotFoundException, UserNameNotValid {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("NewName", "NewSurname", "NewEmail", "NewPass");

        when(userService.updateUser(updateUserDTO, "User1")).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userController.updateUser(updateUserDTO, "User1");
        });

        verify(userService, times(1)).updateUser(updateUserDTO, "User1");
    }

    @Test
    public void testUpdateUserExceptionUserNameNotValid() throws UserNotFoundException, UserNameNotValid {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("NewName", "NewSurname", "NewEmail", "NewPass");

        when(userService.updateUser(updateUserDTO, "User1")).thenThrow(UserNameNotValid.class);

        Assertions.assertThrows(UserNameNotValid.class, () -> {
            userController.updateUser(updateUserDTO, "User1");
        });

        verify(userService, times(1)).updateUser(updateUserDTO, "User1");
    }

    @Test
    public void testUpdateUserRole() throws UserNotFoundException, RoleNotFoundException, UserNameNotValid {
        RoleResponse roleResponse = new RoleResponse("User", "User role");
        UserResponse userResponse = new UserResponse("User1", "Alvaro", "Aviles", "alvaro@gmail.com",Set.of(roleResponse));

        when(userService.updateUserRole("User1", "User")).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.updateUserRole("User1", "User");

        assertEquals(userResponse, response.getBody());

        verify(userService, times(1)).updateUserRole("User1", "User");
    }

    @Test
    public void testUpdateUserRoleExceptionUserNotFound() throws UserNotFoundException, RoleNotFoundException, UserNameNotValid {
        when(userService.updateUserRole("User1", "User")).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userController.updateUserRole("User1", "User");
        });

        verify(userService, times(1)).updateUserRole("User1", "User");
    }

    @Test
    public void testUpdateUserRoleExceptionRoleNotFound() throws UserNotFoundException, RoleNotFoundException, UserNameNotValid {
        when(userService.updateUserRole("User1", "User")).thenThrow(RoleNotFoundException.class);

        Assertions.assertThrows(RoleNotFoundException.class, () -> {
            userController.updateUserRole("User1", "User");
        });

        verify(userService, times(1)).updateUserRole("User1", "User");
    }

    @Test
    public void testUpdateUserRoleExceptionUsernameNotValid() throws UserNotFoundException, RoleNotFoundException, UserNameNotValid {
        when(userService.updateUserRole("User1", "User")).thenThrow(UserNameNotValid.class);

        Assertions.assertThrows(UserNameNotValid.class, () -> {
            userController.updateUserRole("User1", "User");
        });

        verify(userService, times(1)).updateUserRole("User1", "User");
    }
}
