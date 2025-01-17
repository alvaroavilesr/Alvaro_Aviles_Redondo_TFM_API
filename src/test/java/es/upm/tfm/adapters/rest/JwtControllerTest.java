package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.JwtRequestDTO;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.JwtResponse;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import es.upm.tfm.adapters.mysqldb.response.UserResponse;
import es.upm.tfm.domain.services.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtControllerTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtController jwtController;

    @Test
    void testCreateJwtTokenSuccess() throws Exception, UserNotFoundException {

        RoleResponse roleResponse = new RoleResponse("Admin",  "Admin role");
        Set<RoleResponse> roleResponses = new HashSet<>();
        roleResponses.add(roleResponse);
        UserResponse userResponse = new UserResponse(
                "User",
                "example",
                "example",
                "user@example.com",
                roleResponses
        );
        JwtResponse jwtResponse = new JwtResponse(userResponse, "iwuqlen09128un3c891273hc1928hc731298");
        JwtRequestDTO jwtRequestDTO = new JwtRequestDTO("User", "123");

        when(jwtService.createJwtToken(jwtRequestDTO)).thenReturn(jwtResponse);

        JwtResponse response = jwtController.createJwtToken(jwtRequestDTO);

        verify(jwtService, times(1)).createJwtToken(jwtRequestDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("iwuqlen09128un3c891273hc1928hc731298", response.getJwtToken());
        Assertions.assertEquals("User", response.getUserResponse().getUserName());
        Assertions.assertEquals("example", response.getUserResponse().getUserFirstName());
        Assertions.assertEquals("example", response.getUserResponse().getUserLastName());
        Assertions.assertEquals("user@example.com", response.getUserResponse().getEmail());
        Assertions.assertEquals(roleResponses, response.getUserResponse().getRole());
    }

    @Test
    void testCreateJwtTokenError() throws Exception, UserNotFoundException {

        JwtRequestDTO jwtRequestDTO = new JwtRequestDTO("User", "123");

        when(jwtService.createJwtToken(jwtRequestDTO)).thenThrow(Exception.class);

        Assertions.assertThrows(Exception.class, () -> {
            jwtController.createJwtToken(jwtRequestDTO);
        });

        verify(jwtService, times(1)).createJwtToken(jwtRequestDTO);
    }
}
