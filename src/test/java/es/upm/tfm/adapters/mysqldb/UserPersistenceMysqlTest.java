package es.upm.tfm.adapters.mysqldb;

import es.upm.tfm.adapters.mysqldb.dto.NewUserDTO;
import es.upm.tfm.adapters.mysqldb.dto.UpdateUserDTO;
import es.upm.tfm.adapters.mysqldb.entity.RoleEntity;
import es.upm.tfm.adapters.mysqldb.entity.UserEntity;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UsersNotFoundException;
import es.upm.tfm.adapters.mysqldb.persistence.UserPersistenceMysql;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import es.upm.tfm.adapters.mysqldb.response.UserResponse;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserPersistenceMysqlTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserPersistenceMysql userPersistenceMysql;

    @Test
    void RegisterNewUserAlreadyExisting(){
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");
        UserEntity user = new UserEntity("User1",  "User", "1");

        Mockito.when(userRepository.findById(newUserDTO.getUserName())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistingException.class, () -> {
            userPersistenceMysql.registerNewUser(newUserDTO);
        });
    }

    @Test
    void RegisterNewUserRoleNotFound(){
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");

        Mockito.when(userRepository.findById(newUserDTO.getUserName())).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findById("User")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> {
            userPersistenceMysql.registerNewUser(newUserDTO);
        });
    }

    @Test
    void RegisterNewUser() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");
        RoleEntity role = new RoleEntity("User", "User role");

        Set<RoleEntity> userRoles = new HashSet<>();
        userRoles.add(role);

        UserEntity user = new UserEntity("User1",  "User", "1",  userRoles);

        Mockito.when(userRepository.findById(newUserDTO.getUserName())).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findById("User")).thenReturn(Optional.of(role));
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserResponse userResponse = userPersistenceMysql.registerNewUser(newUserDTO);

        Assertions.assertEquals("User1", userResponse.getUserName());
        Assertions.assertEquals("User", userResponse.getUserFirstName());
        Assertions.assertEquals("1", userResponse.getUserLastName());

        verify(userRepository, times(1)).findById(newUserDTO.getUserName());
        verify(roleRepository, times(1)).findById("User");
        verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));

    }

    @Test
    void CreateNewUserAlreadyExisting(){
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");
        UserEntity user = new UserEntity("User1",  "User", "1");

        Mockito.when(userRepository.findById(newUserDTO.getUserName())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistingException.class, () -> {
            userPersistenceMysql.createUser(newUserDTO, "User");
        });
    }

    @Test
    void CreateNewUserRoleNotFound(){
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");

        Mockito.when(userRepository.findById(newUserDTO.getUserName())).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findById("User")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> {
            userPersistenceMysql.createUser(newUserDTO, "User");
        });
    }

    @Test
    void CreateNewUser() throws UserAlreadyExistingException, RoleNotFoundException {
        NewUserDTO newUserDTO = new NewUserDTO("Admin1",  "Admin", "1", "admin@example.com", "123");
        RoleEntity role = new RoleEntity("Admin", "Admin role");

        Set<RoleEntity> userRoles = new HashSet<>();
        userRoles.add(role);

        UserEntity user = new UserEntity("Admin1",  "Admin", "1",  userRoles);

        Mockito.when(userRepository.findById(newUserDTO.getUserName())).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findById("Admin")).thenReturn(Optional.of(role));
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserResponse userResponse = userPersistenceMysql.createUser(newUserDTO, "Admin");

        Assertions.assertEquals("Admin1", userResponse.getUserName());
        Assertions.assertEquals("Admin", userResponse.getUserFirstName());
        Assertions.assertEquals("1", userResponse.getUserLastName());

        verify(userRepository, times(1)).findById(newUserDTO.getUserName());
        verify(roleRepository, times(1)).findById("Admin");
        verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));

    }

    @Test
    void GetUsersUsersNotFound(){
        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(UsersNotFoundException.class, () -> {
            userPersistenceMysql.getUsers();
        });
    }

    @Test
    void GetUsers() throws UsersNotFoundException {
        RoleEntity role = new RoleEntity("User", "User role");
        UserEntity user = new UserEntity("User1",  "Alvaro", "Aviles" ,Set.of(role));

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponse> responses = userPersistenceMysql.getUsers();

        Assertions.assertEquals("User1" , responses.get(0).getUserName());
        Assertions.assertEquals("Alvaro" , responses.get(0).getUserFirstName());
        Assertions.assertEquals("Aviles" , responses.get(0).getUserLastName());
    }

    @Test
    void GetUserUserNotFound(){
        Mockito.when(userRepository.findById("User1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userPersistenceMysql.getUser("User1");
        });
    }

    @Test
    void GetUserUserNameNotValid(){
        UserEntity user = new UserEntity("User1", "Alvaro", "Aviles", "alvaro@gmail.com", "pass", Collections.emptySet(), null);

        Mockito.when(userRepository.findById("User1")).thenReturn(Optional.of(user));

        assertThrows(UserNameNotValid.class, () -> {
            userPersistenceMysql.getUser("User1");
        });
    }

    @Test
    void GetUser() throws UserNotFoundException, UserNameNotValid {
        RoleEntity role = new RoleEntity("User", "Role for users");
        UserEntity user = new UserEntity("User1", "Alvaro", "Aviles", "alvaro@gmail.com", "pass", Set.of(role), null);
        RoleResponse roleResponse = new RoleResponse("User", "Role for users");
        UserResponse userResponse = new UserResponse("User1", "Alvaro", "Aviles", "alvaro@gmail.com",Set.of(roleResponse));

        Mockito.when(userRepository.findById("User1")).thenReturn(Optional.of(user));

        UserResponse response = userPersistenceMysql.getUser("User1");

        Assertions.assertEquals(response, userResponse);
    }

    @Test
    void DeleteUserUserNotFound(){
        Mockito.when(userRepository.findById("User1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userPersistenceMysql.deleteUser("User1");
        });
    }

    @Test
    void DeleteUserUserNameNotValid(){
        UserEntity user = new UserEntity("User1", "Alvaro", "Aviles", "alvaro@gmail.com", "pass", Collections.emptySet(), null);

        Mockito.when(userRepository.findById("User1")).thenReturn(Optional.of(user));

        assertThrows(UserNameNotValid.class, () -> {
            userPersistenceMysql.deleteUser("User1");
        });
    }

    @Test
    void DeleteUser() throws UserNotFoundException, UserNameNotValid {
        RoleEntity role = new RoleEntity("User", "Role for users");
        UserEntity user = new UserEntity("User1", "Alvaro", "Aviles", "alvaro@gmail.com", "pass", Set.of(role), null);
        RoleResponse roleResponse = new RoleResponse("User", "Role for users");
        UserResponse userResponse = new UserResponse("User1", "Alvaro", "Aviles", "alvaro@gmail.com",Set.of(roleResponse));

        Mockito.when(userRepository.findById("User1")).thenReturn(Optional.of(user));

        UserResponse response = userPersistenceMysql.deleteUser("User1");

        Assertions.assertEquals(response, userResponse);
    }

    @Test
    void UpdateUserUserNotFound(){
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("NewName", "NewSurname", "NewEmail", "NewPass");

        Mockito.when(userRepository.findById("User1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userPersistenceMysql.updateUser(updateUserDTO,  "User1");
        });
    }

    @Test
    void UpdateUserUserNameNotValid(){
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("NewName", "NewSurname", "NewEmail", "NewPass");

        UserEntity user = new UserEntity("User1", "Alvaro", "Aviles", "alvaro@gmail.com", "pass", Collections.emptySet(), null);

        Mockito.when(userRepository.findById("User1")).thenReturn(Optional.of(user));

        assertThrows(UserNameNotValid.class, () -> {
            userPersistenceMysql.updateUser(updateUserDTO, "User1");
        });
    }

    @Test
    void UpdateUser() throws UserNotFoundException, UserNameNotValid {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("NewName", "NewSurname", "NewEmail@gmail.com", "NewPass");
        RoleEntity role = new RoleEntity("User", "Role for users");
        UserEntity user = new UserEntity("User1", "Alvaro", "Aviles", "alvaro@gmail.com", "pass", Set.of(role), null);
        RoleResponse roleResponse = new RoleResponse("User", "Role for users");
        UserResponse userResponse = new UserResponse("User1", "NewName", "NewSurname", "NewEmail@gmail.com",Set.of(roleResponse));

        Mockito.when(userRepository.findById("User1")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        UserResponse response = userPersistenceMysql.updateUser(updateUserDTO, "User1");

        Assertions.assertEquals(response, userResponse);
    }

    @Test
    void UpdateUserRoleUserNotFound(){
        Mockito.when(userRepository.findById("User1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userPersistenceMysql.updateUserRole("User1",  "User");
        });
    }

    @Test
    void UpdateUserRoleUserNameNotValid(){
        UserEntity user = new UserEntity("User1", "Alvaro", "Aviles", "alvaro@gmail.com", "pass", Collections.emptySet(), null);

        Mockito.when(userRepository.findById("User1")).thenReturn(Optional.of(user));

        assertThrows(UserNameNotValid.class, () -> {
            userPersistenceMysql.updateUserRole("User1",  "User");
        });
    }

    @Test
    void UpdateUserRoleRoleNotFoundException(){
        RoleEntity role = new RoleEntity("User", "Role for users");
        UserEntity user = new UserEntity("User1", "Alvaro", "Aviles", "alvaro@gmail.com", "pass", Set.of(role), null);

        Mockito.when(userRepository.findById("User1")).thenReturn(Optional.of(user));
        Mockito.when(roleRepository.findById("User")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> {
            userPersistenceMysql.updateUserRole("User1",  "User");
        });
    }

    @Test
    void UpdateUserRole() throws UserNotFoundException, RoleNotFoundException, UserNameNotValid {
        RoleEntity role = new RoleEntity("User", "Role for users");
        UserEntity user = new UserEntity("User1", "Alvaro", "Aviles", "alvaro@gmail.com", "pass", Set.of(role), null);
        RoleResponse roleResponse = new RoleResponse("User", "Role for users");
        UserResponse userResponse = new UserResponse("User1", "Alvaro", "Aviles", "alvaro@gmail.com",Set.of(roleResponse));

        Mockito.when(userRepository.findById("User1")).thenReturn(Optional.of(user));
        Mockito.when(roleRepository.findById("User")).thenReturn(Optional.of(role));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        UserResponse response = userPersistenceMysql.updateUserRole("User1",  "User");

        Assertions.assertEquals(response, userResponse);
    }
}
