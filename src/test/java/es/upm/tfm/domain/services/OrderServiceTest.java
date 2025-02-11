package es.upm.tfm.domain.services;

import es.upm.tfm.adapters.mysqldb.dto.OrderDTO;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrderItemIdsAndMountsNotValidException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrdersNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.OrderResponse;
import es.upm.tfm.domain.persistence_ports.OrderPersistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderPersistence orderPersistence;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testSaveOrderUserNotFound() throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {

        OrderDTO orderDTO = new OrderDTO();

        when(orderPersistence.saveOrder(orderDTO, "User1")).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            orderService.saveOrder(orderDTO, "User1");
        });
    }

    @Test
    void testSaveUserNameNotValid() throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {

        OrderDTO orderDTO = new OrderDTO();

        when(orderPersistence.saveOrder(orderDTO, "User1")).thenThrow(UserNameNotValid.class);

        Assertions.assertThrows(UserNameNotValid.class, () -> {
            orderService.saveOrder(orderDTO, "User1");
        });
    }

    @Test
    void testSaveOrderItemIdsAndMountsNotValidException() throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {

        OrderDTO orderDTO = new OrderDTO();

        when(orderPersistence.saveOrder(orderDTO, "User1")).thenThrow(OrderItemIdsAndMountsNotValidException.class);

        Assertions.assertThrows(OrderItemIdsAndMountsNotValidException.class, () -> {
            orderService.saveOrder(orderDTO, "User1");
        });
    }

    @Test
    void testSaveOrderItemNotFoundException() throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {

        OrderDTO orderDTO = new OrderDTO();

        when(orderPersistence.saveOrder(orderDTO, "User1")).thenThrow(ItemNotFoundException.class);

        Assertions.assertThrows(ItemNotFoundException.class, () -> {
            orderService.saveOrder(orderDTO, "User1");
        });
    }

    @Test
    void testSaveOrder() throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {

        OrderDTO orderDTO = new OrderDTO();
        OrderResponse orderResponse = new OrderResponse(1L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", "User1", 2.0, 2, null);

        when(orderPersistence.saveOrder(orderDTO, "User1")).thenReturn(orderResponse);

        OrderResponse response = orderService.saveOrder(orderDTO, "User1");
        assertEquals(orderResponse, response);
    }

    @Test
    void testGetAllOrdersOrdersNotFound() throws OrdersNotFoundException {

        when(orderPersistence.getAllOrders()).thenThrow(OrdersNotFoundException.class);

        Assertions.assertThrows(OrdersNotFoundException.class, () -> {
            orderService.getAllOrders();
        });
    }

    @Test
    void testGetAllOrders() throws OrdersNotFoundException {

        OrderResponse orderResponse1 = new OrderResponse(1L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", "User1", 0, 0, null);
        OrderResponse orderResponse2 = new OrderResponse(2L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", "User1", 0, 0, null);

        when(orderPersistence.getAllOrders()).thenReturn(List.of(orderResponse1, orderResponse2));

        List<OrderResponse> response = orderService.getAllOrders();

        assertEquals(List.of(orderResponse1, orderResponse2), response);

    }
}
