package es.upm.tfm.adapters.rest;

import es.upm.tfm.adapters.mysqldb.dto.OrderDTO;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrderItemIdsAndMountsNotValidException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrdersNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.OrderResponse;
import es.upm.tfm.domain.services.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.management.ListenerNotFoundException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void testSaveOrderUserNotFound() throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {

        OrderDTO orderDTO = new OrderDTO();

        when(orderService.saveOrder(orderDTO, "User1")).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            orderController.saveOrder("User1", orderDTO);
        });

        verify(orderService, times(1)).saveOrder(orderDTO, "User1");
    }

    @Test
    void testSaveUserNameNotValid() throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {

        OrderDTO orderDTO = new OrderDTO();

        when(orderService.saveOrder(orderDTO, "User1")).thenThrow(UserNameNotValid.class);

        Assertions.assertThrows(UserNameNotValid.class, () -> {
            orderController.saveOrder("User1", orderDTO);
        });

        verify(orderService, times(1)).saveOrder(orderDTO, "User1");
    }

    @Test
    void testSaveOrderItemIdsAndMountsNotValidException() throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {

        OrderDTO orderDTO = new OrderDTO();

        when(orderService.saveOrder(orderDTO, "User1")).thenThrow(OrderItemIdsAndMountsNotValidException.class);

        Assertions.assertThrows(OrderItemIdsAndMountsNotValidException.class, () -> {
            orderController.saveOrder("User1", orderDTO);
        });

        verify(orderService, times(1)).saveOrder(orderDTO, "User1");
    }

    @Test
    void testSaveOrderItemNotFoundException() throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {

        OrderDTO orderDTO = new OrderDTO();

        when(orderService.saveOrder(orderDTO, "User1")).thenThrow(ItemNotFoundException.class);

        Assertions.assertThrows(ItemNotFoundException.class, () -> {
            orderController.saveOrder("User1", orderDTO);
        });

        verify(orderService, times(1)).saveOrder(orderDTO, "User1");
    }

    @Test
    void testSaveOrder() throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {

        OrderDTO orderDTO = new OrderDTO();
        OrderResponse orderResponse = new OrderResponse(1L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", "User1", 2.0, 2, null);

        when(orderService.saveOrder(orderDTO, "User1")).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = orderController.saveOrder("User1", orderDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderResponse, response.getBody());

        verify(orderService, times(1)).saveOrder(orderDTO, "User1");
    }

    @Test
    void testGetAllOrdersOrdersNotFound() throws OrdersNotFoundException {

        when(orderService.getAllOrders()).thenThrow(OrdersNotFoundException.class);

        Assertions.assertThrows(OrdersNotFoundException.class, () -> {
            orderController.getAllOrders();
        });

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testGetAllOrders() throws OrdersNotFoundException {

        OrderResponse orderResponse1 = new OrderResponse(1L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", "User1", 0, 0, null);
        OrderResponse orderResponse2 = new OrderResponse(2L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", "User1", 0, 0, null);

        when(orderService.getAllOrders()).thenReturn(List.of(orderResponse1, orderResponse2));

        ResponseEntity<List<OrderResponse>> response = orderController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(orderResponse1, orderResponse2), response.getBody());

        verify(orderService, times(1)).getAllOrders();
    }
}
