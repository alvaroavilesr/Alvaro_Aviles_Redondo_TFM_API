package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.OrderDTO;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrderItemIdsAndMountsNotValidException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrderNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrdersNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.OrderResponse;
import es.upm.tfm.domain.services.OrderService;
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
@Tag(name = "Orders", description = "Endpoints for managing the orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully created"),
            @ApiResponse(responseCode = "400", description = "Mandatory fields not supplied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "POST HTTP method endpoint for saving an order in the database - [ADMIN][VENDOR][USER]")
    @PostMapping("/order/{userName}")
    @PreAuthorize("hasRole('Admin') or hasRole('User') or hasRole('Vendor')")
    public ResponseEntity<OrderResponse> saveOrder(@PathVariable String userName, @Valid @RequestBody OrderDTO orderDTO) throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {
        return new ResponseEntity<>(orderService.saveOrder(orderDTO, userName), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "CGET HTTP method endpoint for getting all orders - [ADMIN][VENDOR]")
    @GetMapping("/orders")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() throws OrdersNotFoundException {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Operation(summary = "GET HTTP method endpoint for getting an order - [ADMIN][VENDOR][USER]")
    @GetMapping("/order/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor') or hasRole('User')")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) throws OrderNotFoundException {
        return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "CGET HTTP method endpoint for getting all orders of a determined user - [ADMIN][VENDOR][USER]")
    @GetMapping("/orders/{userName}")
    @PreAuthorize("hasRole('Admin') or hasRole('Vendor') or hasRole('User')")
    public ResponseEntity<List<OrderResponse>> getAllOrdersOfAnUser(@PathVariable String userName) throws OrdersNotFoundException, UserNotFoundException, UserNameNotValid {
        return new ResponseEntity<>(orderService.getAllOrdersOfAnUser(userName), HttpStatus.OK);
    }
}
