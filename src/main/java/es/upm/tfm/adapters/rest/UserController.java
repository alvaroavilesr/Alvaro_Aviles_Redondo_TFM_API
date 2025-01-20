package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.NewUserDTO;
import es.upm.tfm.adapters.mysqldb.dto.UpdateUserDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found in the database"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "GET HTTP method endpoint for getting an user - [ADMIN]")
    @GetMapping("/api/user/{userName}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<UserResponse> getUser(@PathVariable("userName") String userName) throws UserNotFoundException, UserNameNotValid {
        return new ResponseEntity<>(userService.getUser(userName),HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "400", description = "No user found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "DELETE HTTP method endpoint for deleting an user by its username - [ADMIN]")
    @DeleteMapping("/api/user/{userName}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable("userName") String userName) throws UserNotFoundException, UserNameNotValid {
        return new ResponseEntity<>(userService.deleteUser(userName),HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User data successfully updated"),
            @ApiResponse(responseCode = "400", description = "Mandatory fields not supplied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "PUT HTTP method endpoint for updating the data of an user - [ADMIN][VENDOR][USER]")
    @PutMapping("/api/user/{userName}")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor') or hasRole('User')")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserDTO updateUserDTO, @PathVariable("userName") String userName) throws UserNotFoundException, UserNameNotValid {
        return new ResponseEntity<>(userService.updateUser(updateUserDTO, userName),HttpStatus.OK);
    }
}
