package es.upm.tfm.adapters.mysqldb;

import es.upm.tfm.adapters.mysqldb.dto.JwtRequestDTO;
import es.upm.tfm.adapters.mysqldb.entity.UserEntity;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.persistence.JwtPersistenceMysql;
import es.upm.tfm.adapters.mysqldb.response.JwtResponse;
import es.upm.tfm.adapters.mysqldb.respository.UserRepository;
import es.upm.tfm.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtPersistenceMysqlTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private JwtPersistenceMysql jwtPersistenceMysql;

    private JwtRequestDTO jwtRequestDTO;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        jwtRequestDTO = new JwtRequestDTO("User", "password");
        userEntity = new UserEntity();
        userEntity.setUserName("User");
        userEntity.setUserPassword("password");
        userEntity.setRole(new HashSet<>());
    }

    @Test
    void testCreateJwtTokenSuccess() throws Exception, UserNotFoundException {
        when(userRepository.findById("User")).thenReturn(Optional.of(userEntity));
        when(userRepository.findById("User")).thenReturn(Optional.of(userEntity));
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("generatedToken");

        JwtResponse response = jwtPersistenceMysql.createJwtToken(jwtRequestDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("generatedToken", response.getJwtToken());

        verify(userRepository, times(2)).findById("User");
        verify(jwtUtil, times(1)).generateToken(any(UserDetails.class));
    }

    @Test
    void testCreateJwtTokenUserNotFoundException() {
        when(userRepository.findById("User")).thenReturn(Optional.empty());
        when(userRepository.findById("User")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            jwtPersistenceMysql.createJwtToken(jwtRequestDTO);
        });

        verify(userRepository, times(1)).findById("User");
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        when(userRepository.findById("User")).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = jwtPersistenceMysql.loadUserByUsername("User");

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("User", userDetails.getUsername());

        verify(userRepository, times(1)).findById("User");
    }

    @Test
    void testLoadUserByUsernameUsernameNotFoundException() {
        when(userRepository.findById("User")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            jwtPersistenceMysql.loadUserByUsername("User");
        });

        verify(userRepository, times(1)).findById("User");
    }
}
