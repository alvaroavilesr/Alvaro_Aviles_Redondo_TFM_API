package es.upm.tfm.adapters.mysqldb;

import es.upm.tfm.adapters.mysqldb.dto.NewUserDTO;
import es.upm.tfm.adapters.mysqldb.dto.RoleDTO;
import es.upm.tfm.adapters.mysqldb.entity.RoleEntity;
import es.upm.tfm.adapters.mysqldb.entity.UserEntity;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotValidException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.user.UsersNotFoundException;
import es.upm.tfm.adapters.mysqldb.persistence.RolePersistenceMysql;
import es.upm.tfm.adapters.mysqldb.persistence.UserPersistenceMysql;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import es.upm.tfm.adapters.mysqldb.response.UserResponse;
import es.upm.tfm.adapters.mysqldb.respository.RoleRepository;
import es.upm.tfm.adapters.mysqldb.respository.UserRepository;
import es.upm.tfm.domain.models.User;
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
import static org.mockito.ArgumentMatchers.any;
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
    public void RegisterNewUserAlreadyExisting(){
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");
        UserEntity user = new UserEntity("User1",  "User", "1");

        Mockito.when(userRepository.findById(newUserDTO.getUserName())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistingException.class, () -> {
            userPersistenceMysql.registerNewUser(newUserDTO);
        });
    }

    @Test
    public void RegisterNewUserRoleNotFound(){
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");

        Mockito.when(userRepository.findById(newUserDTO.getUserName())).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findById("User")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> {
            userPersistenceMysql.registerNewUser(newUserDTO);
        });
    }

    @Test
    public void RegisterNewUser() throws UserAlreadyExistingException, RoleNotFoundException {
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
    public void CreateNewUserAlreadyExisting(){
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");
        UserEntity user = new UserEntity("User1",  "User", "1");

        Mockito.when(userRepository.findById(newUserDTO.getUserName())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistingException.class, () -> {
            userPersistenceMysql.createUser(newUserDTO, "User");
        });
    }

    @Test
    public void CreateNewUserRoleNotFound(){
        NewUserDTO newUserDTO = new NewUserDTO("User1",  "User", "1", "user@example.com", "123");

        Mockito.when(userRepository.findById(newUserDTO.getUserName())).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findById("User")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> {
            userPersistenceMysql.createUser(newUserDTO, "User");
        });
    }

    @Test
    public void CreateNewUser() throws UserAlreadyExistingException, RoleNotFoundException {
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
    public void GetUsersUsersNotFound(){
        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(UsersNotFoundException.class, () -> {
            userPersistenceMysql.getUsers();
        });
    }

    @Test
    public void GetUsers() throws UsersNotFoundException {
        RoleEntity role = new RoleEntity("User", "User role");
        RoleResponse roleResponse = new RoleResponse("User", "User role");
        UserEntity user = new UserEntity("User1",  "Alvaro", "Aviles" ,Set.of(role));
        UserResponse userResponse = new UserResponse("User1", "Alvaro", "Aviles", "alvaro@gmail.com",Set.of(roleResponse));

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponse> responses = userPersistenceMysql.getUsers();

        Assertions.assertEquals(responses.get(0).getUserName(), "User1");
        Assertions.assertEquals(responses.get(0).getUserFirstName(), "Alvaro");
        Assertions.assertEquals(responses.get(0).getUserLastName(), "Aviles");
    }

}
