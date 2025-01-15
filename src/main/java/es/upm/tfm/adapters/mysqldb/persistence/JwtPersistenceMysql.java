package es.upm.tfm.adapters.mysqldb.persistence;

import es.upm.tfm.adapters.mysqldb.dto.JwtRequestDTO;
import es.upm.tfm.adapters.mysqldb.entity.UserEntity;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.JwtResponse;
import es.upm.tfm.adapters.mysqldb.response.UserResponse;
import es.upm.tfm.adapters.mysqldb.respository.UserRepository;
import es.upm.tfm.domain.persistence_ports.JwtPersistence;
import es.upm.tfm.utils.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository("JwtPersistence")
public class JwtPersistenceMysql implements JwtPersistence, UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    ModelMapper modelMapper = new ModelMapper();

    public JwtResponse createJwtToken(JwtRequestDTO jwtRequest) throws Exception, UserNotFoundException {
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getUserPassword();
        authenticate(userName, userPassword);

        UserDetails userDetails = loadUserByUsername(userName);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        UserEntity user = userRepository.findById(userName).orElseThrow(() -> new UserNotFoundException(userName));
        return new JwtResponse(modelMapper.map(user, UserResponse.class), newGeneratedToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findById(username);

        if (user.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    user.get().getUserName(),
                    user.get().getUserPassword(),
                    getAuthority(user.get())
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Set getAuthority(UserEntity user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

    private void authenticate(String userName, String userPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
