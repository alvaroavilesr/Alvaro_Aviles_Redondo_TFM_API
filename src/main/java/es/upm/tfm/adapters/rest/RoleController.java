package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.RoleDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotValidException;
import es.upm.tfm.adapters.mysqldb.exception.role.RolesNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.RoleResponse;
import es.upm.tfm.domain.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Roles", description = "Endpoints for managing the different user roles of the application")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role successfully created"),
            @ApiResponse(responseCode = "400", description = "Mandatory fields not supplied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "POST HTTP method endpoint for creating a new role - [ADMIN]")
    @PostMapping({"/role"})
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleDTO roleDTO) throws RoleNotValidException, RoleAlreadyExistingException {
        return new ResponseEntity<>(roleService.createRole(roleDTO), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No roles found in the database"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "CGET HTTP method endpoint for listing all roles - [ADMIN]")
    @GetMapping("/roles")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<RoleResponse>> getRoles() throws RolesNotFoundException {
        return new ResponseEntity<>(roleService.getRoles(),HttpStatus.OK);
    }
}
