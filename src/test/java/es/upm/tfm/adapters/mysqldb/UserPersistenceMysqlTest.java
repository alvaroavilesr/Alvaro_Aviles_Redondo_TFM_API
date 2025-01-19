package es.upm.tfm.adapters.mysqldb;

import es.upm.tfm.adapters.mysqldb.dto.NewUserDTO;
import es.upm.tfm.adapters.mysqldb.dto.RoleDTO;
import es.upm.tfm.adapters.mysqldb.entity.RoleEntity;
import es.upm.tfm.adapters.mysqldb.entity.UserEntity;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotValidException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.persistence.RolePersistenceMysql;
import es.upm.tfm.adapters.mysqldb.persistence.UserPersistenceMysql;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
}
