package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.CategoryDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNameAlreadyExisting;
import es.upm.tfm.adapters.mysqldb.response.CategoryResponse;
import es.upm.tfm.domain.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Categories", description = "Endpoints for managing the categories of the items")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category successfully created"),
            @ApiResponse(responseCode = "400", description = "Mandatory fields not supplied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "POST HTTP method endpoint for saving a category in the database - [ADMIN][VENDOR]")
    @PostMapping("/category")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor')")
    public ResponseEntity<CategoryResponse> saveCategory(@Valid @RequestBody CategoryDTO categoryDTO) throws CategoryNameAlreadyExisting {
        return new ResponseEntity<>(categoryService.saveCategory(categoryDTO), HttpStatus.OK);
    }

}
