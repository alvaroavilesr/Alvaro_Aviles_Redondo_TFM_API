package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.CategoryDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoriesNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryAlreadyAttachedToAnItem;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNameAlreadyExisting;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Categories", description = "Endpoints for managing the categories of the items")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Categories not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "CGET HTTP method endpoint for listing all categories - [ADMIN][VENDOR][USER]")
    @GetMapping("/categories")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor') or hasRole('User')")
    public ResponseEntity<List<CategoryResponse>> getCategories() throws CategoriesNotFoundException {
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Categories not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "GET HTTP method endpoint for getting a category by its id - [ADMIN][VENDOR][USER]")
    @GetMapping(value = "/category/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor') or hasRole('User')")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) throws CategoryNotFoundException {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "DELETE HTTP method endpoint for deleting a category by its id - [ADMIN][VENDOR]")
    @DeleteMapping(value = "/category/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor')")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Long id) throws CategoryAlreadyAttachedToAnItem, CategoryNotFoundException {
        return new ResponseEntity<>(categoryService.deleteById(id), HttpStatus.OK);
    }
}
