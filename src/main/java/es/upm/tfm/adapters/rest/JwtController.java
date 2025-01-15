package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.JwtRequestDTO;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.JwtResponse;
import es.upm.tfm.domain.services.JwtService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@OpenAPIDefinition(
        info = @Info(
                title = "WebShop API",
                version = "0.0",
                description = "API for the stock management of the web application",
                license = @License(name = "Apache 2.0", url = "http://foo.bar"),
                contact = @Contact(name = "Alvaro", email = "alvaro.aviles@alumnos.upm.es")
        )
)
@RestController
@CrossOrigin
@RequestMapping("/login/authenticate")
@Tag(name = "Login", description = "Endpoint for logging in and getting a valid JWT")
public class JwtController {


    private final JwtService jwtService;

    @Autowired
    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged successfully"),
            @ApiResponse(responseCode = "400", description = "Mandatory fields not supplied"),
            @ApiResponse(responseCode = "401", description = "Not existing user")
    })
    @Operation(summary = "POST HTTP method endpoint for getting a valid JWT - [NOT SECURED]")
    @PostMapping
    public JwtResponse createJwtToken(@Valid @RequestBody JwtRequestDTO jwtRequest) throws Exception, UserNotFoundException {
        return jwtService.createJwtToken(jwtRequest);
    }
}
