package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.NewUserDTO;
import es.upm.tfm.adapters.mysqldb.dto.UpdateUserDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UsersNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.UserResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPersistence {

    UserResponse registerNewUser (NewUserDTO newUserDTO) throws UserAlreadyExistingException, RoleNotFoundException;

    UserResponse createUser (NewUserDTO newUserDTO, String role) throws UserAlreadyExistingException, RoleNotFoundException;

    List<UserResponse> getUsers() throws UsersNotFoundException;

    UserResponse getUser(String userName) throws UserNotFoundException, UserNameNotValid;

    UserResponse deleteUser(String userName) throws UserNotFoundException, UserNameNotValid;

    UserResponse updateUser(UpdateUserDTO updateUserDTO, String userName) throws UserNotFoundException, UserNameNotValid;

    UserResponse updateUserRole(String username, String role) throws UserNotFoundException, RoleNotFoundException, UserNameNotValid;
}
