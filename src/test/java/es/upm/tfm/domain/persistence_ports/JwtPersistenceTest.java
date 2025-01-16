package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.JwtRequestDTO;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.JwtResponse;
import es.upm.tfm.domain.services.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtPersistenceTest {

    @Mock
    private JwtPersistence jwtPersistence;

    @InjectMocks
    private JwtService jwtService;


    @Test
    void testCreateJwtToken_Success() throws Exception, UserNotFoundException {
        JwtRequestDTO jwtRequestDTO = new JwtRequestDTO("User", "password");
        JwtResponse expectedResponse = new JwtResponse(null, "token123");

        when(jwtPersistence.createJwtToken(jwtRequestDTO)).thenReturn(expectedResponse);

        JwtResponse response = jwtService.createJwtToken(jwtRequestDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("token123", response.getJwtToken());
    }

    @Test
    void testCreateJwtToken_UserNotFoundException() throws Exception, UserNotFoundException {
        JwtRequestDTO jwtRequestDTO = new JwtRequestDTO("NonExistentUser", "password");

        when(jwtPersistence.createJwtToken(jwtRequestDTO)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> {
            jwtService.createJwtToken(jwtRequestDTO);
        });
    }
}
