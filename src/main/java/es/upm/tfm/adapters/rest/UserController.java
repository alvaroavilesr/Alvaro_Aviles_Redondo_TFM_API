package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.NewUserDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.user.UsersNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.UserResponse;
import es.upm.tfm.domain.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Users", description = "Endpoints for managing the users of the application")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Mandatory fields not supplied"),
            @ApiResponse(responseCode = "400", description = "UserName already existing")
    })
    @Operation(summary = "POST HTTP method endpoint for registering a new user with USER role - [NON-SECURED]")
    @PostMapping({"/user/register"})
    public ResponseEntity<UserResponse> registerNewUser(@Valid @RequestBody NewUserDTO newUserDTO) throws UserAlreadyExistingException, RoleNotFoundException {
        return new ResponseEntity<>(userService.registerNewUser(newUserDTO), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Mandatory fields not supplied"),
            @ApiResponse(responseCode = "400", description = "UserName already existing"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "POST HTTP method endpoint for creating a new user - [ADMIN]")
    @PostMapping({"/api/user/{role}"})
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody NewUserDTO newUserDTO, @PathVariable("role") String role) throws RoleNotFoundException, UserAlreadyExistingException {
        return new ResponseEntity<>(userService.createUser(newUserDTO,role), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No users found in the database"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "CGET HTTP method endpoint for listing all users - [ADMIN]")
    @GetMapping("/api/users")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<UserResponse>> getUsers() throws UsersNotFoundException {
        return new ResponseEntity<>(userService.getUsers(),HttpStatus.OK);
    }

}
