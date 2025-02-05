package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.ItemDTO;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
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
}
