package es.upm.tfm.domain.services;

import es.upm.tfm.adapters.mysqldb.dto.NewUserDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.user.UsersNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import es.upm.tfm.domain.persistence_ports.UserPersistence;

import java.util.List;

@Service
public class UserService {

    private final UserPersistence userPersistence;

    @Autowired
    @Lazy
    public UserService(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    public UserResponse registerNewUser (NewUserDTO newUserDTO) throws UserAlreadyExistingException, RoleNotFoundException {
        return this.userPersistence.registerNewUser(newUserDTO);
    }

    public UserResponse createUser (NewUserDTO newUserDTO, String role) throws UserAlreadyExistingException, RoleNotFoundException {
        return this.userPersistence.createUser(newUserDTO, role);
    }

    public List<UserResponse> getUsers() throws UsersNotFoundException {
        return this.userPersistence.getUsers();
    }

}
