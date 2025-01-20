package es.upm.tfm.adapters.mysqldb.persistence;

import es.upm.tfm.adapters.mysqldb.dto.NewUserDTO;
import es.upm.tfm.adapters.mysqldb.entity.RoleEntity;
import es.upm.tfm.adapters.mysqldb.entity.UserEntity;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UsersNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.UserResponse;
import es.upm.tfm.adapters.mysqldb.respository.RoleRepository;
import es.upm.tfm.adapters.mysqldb.respository.UserRepository;
import es.upm.tfm.domain.persistence_ports.UserPersistence;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserPersistenceMysql implements UserPersistence {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper = new ModelMapper();

    public UserPersistenceMysql(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public UserResponse registerNewUser(NewUserDTO newUserDTO) throws RoleNotFoundException, UserAlreadyExistingException {
        if(userRepository.findById(newUserDTO.getUserName()).isPresent()){
            throw new UserAlreadyExistingException(newUserDTO.getUserName());
        }

        RoleEntity role = roleRepository.findById("User").orElseThrow(() -> new RoleNotFoundException("User"));
        Set<RoleEntity> userRoles = new HashSet<>();
        userRoles.add(role);

        UserEntity user = modelMapper.map(newUserDTO,UserEntity.class);

        user.setRole(userRoles);
        user.setUserPassword(getEncodedPassword(newUserDTO.getUserPassword()));

        return modelMapper.map(userRepository.save(user),UserResponse.class);
    }

    public UserResponse createUser(NewUserDTO newUserDTO, String selectedRole) throws RoleNotFoundException, UserAlreadyExistingException {
        if(userRepository.findById(newUserDTO.getUserName()).isPresent()){
            throw new UserAlreadyExistingException(newUserDTO.getUserName());
        }
        RoleEntity role = roleRepository.findById(selectedRole).orElseThrow(() -> new RoleNotFoundException(selectedRole));
        Set<RoleEntity> userRoles = new HashSet<>();
        userRoles.add(role);

        UserEntity user = modelMapper.map(newUserDTO,UserEntity.class);

        user.setRole(userRoles);
        user.setUserPassword(getEncodedPassword(newUserDTO.getUserPassword()));

        return modelMapper.map(userRepository.save(user),UserResponse.class);
    }

    public List<UserResponse> getUsers() throws UsersNotFoundException {
        List<UserEntity> users = userRepository.findAll();

        if (users.isEmpty()){
            throw new UsersNotFoundException();
        }else{
            return users.stream()
                    .map(user -> modelMapper.map(user, UserResponse.class))
                    .collect(Collectors.toList());
        }
    }

    public UserResponse getUser(String userName) throws UserNotFoundException, UserNameNotValid {
        UserEntity user = userRepository.findById(userName).orElseThrow(() -> new UserNotFoundException(userName));
        if(user.getRole().isEmpty()){
            throw new UserNameNotValid(userName);
        }
        return modelMapper.map(user, UserResponse.class);
    }

    public UserResponse deleteUser(String userName) throws UserNotFoundException, UserNameNotValid {
        UserEntity user = userRepository.findById(userName).orElseThrow(() -> new UserNotFoundException(userName));
        if(user.getRole().isEmpty()){
            throw new UserNameNotValid(userName);
        }
        userRepository.deleteById(userName);
        return modelMapper.map(user, UserResponse.class);
    }
}
