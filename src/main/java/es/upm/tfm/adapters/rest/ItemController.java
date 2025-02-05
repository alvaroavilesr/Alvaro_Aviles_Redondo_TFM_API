package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.ItemDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemAlreadyInAnOrderException;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.item.NoItemsFoundException;
import es.upm.tfm.adapters.mysqldb.response.ItemResponse;
import es.upm.tfm.domain.services.ItemService;
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
@Tag(name = "Items", description = "Endpoints for managing the items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item successfully created"),
            @ApiResponse(responseCode = "400", description = "Mandatory fields not supplied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "POST HTTP method endpoint for saving an item in the database - [ADMIN][VENDOR]")
    @PostMapping("/item/{category}")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor')")
    public ResponseEntity<ItemResponse> saveItem(@PathVariable String category, @Valid @RequestBody ItemDTO itemDTO) throws CategoryNotFoundException {
        return new ResponseEntity<>(itemService.saveItem(category, itemDTO), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Items not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "CGET HTTP method endpoint for listing all items - [ADMIN][VENDOR][USER]")
    @GetMapping("/items")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor') or hasRole('User')")
    public ResponseEntity<List<ItemResponse>> getItems() throws NoItemsFoundException {
        return new ResponseEntity<>(itemService.getStock(), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Item not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "GET HTTP method endpoint for getting an item by its id - [ADMIN][VENDOR][USER]")
    @GetMapping(value = "/item/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor') or hasRole('User')")
    public ResponseEntity<ItemResponse> getItem(@PathVariable Long id) throws ItemNotFoundException {
        return new ResponseEntity<>(itemService.findById(id), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Item not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "DELETE HTTP method endpoint for deleting an item by its id - [ADMIN][VENDOR]")
    @DeleteMapping(value = "/item/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor')")
    public ResponseEntity<ItemResponse> deleteItem(@PathVariable Long id) throws ItemNotFoundException, ItemAlreadyInAnOrderException {
        return new ResponseEntity<>(itemService.deleteById(id), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item successfully updated"),
            @ApiResponse(responseCode = "404", description = "Item not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "PUT HTTP method endpoint for updating an item by its id - [ADMIN][VENDOR]")
    @PutMapping(value = "/item/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor')")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDTO itemDTO) throws ItemNotFoundException {
        return new ResponseEntity<>(itemService.updateItem(id, itemDTO), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item category successfully updated"),
            @ApiResponse(responseCode = "404", description = "Item not found"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "PUT HTTP method endpoint for updating an item category by its id - [ADMIN][VENDOR]")
    @PutMapping(value = "/item/{id}/{category}")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor')")
    public ResponseEntity<ItemResponse> updateItemCategory (@PathVariable Long id, @PathVariable String category) throws ItemNotFoundException, CategoryNotFoundException {
        return new ResponseEntity<>(itemService.updateItemCategory(id, category), HttpStatus.OK);
    }
}
