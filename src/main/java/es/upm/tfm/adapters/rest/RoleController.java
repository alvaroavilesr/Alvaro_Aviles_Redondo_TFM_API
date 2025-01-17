package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.RoleDTO;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleAlreadyExistingException;
import es.upm.tfm.adapters.mysqldb.exception.role.RoleNotValidException;
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
}
