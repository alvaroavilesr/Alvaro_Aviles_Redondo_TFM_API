package es.upm.tfm.adapters.mysqldb;

import es.upm.tfm.adapters.mysqldb.dto.OrderDTO;
import es.upm.tfm.adapters.mysqldb.entity.*;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrderItemIdsAndMountsNotValidException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrderNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrdersNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.persistence.OrderPersistenceMysql;
import es.upm.tfm.adapters.mysqldb.response.ItemOrderResponse;
import es.upm.tfm.adapters.mysqldb.response.OrderResponse;
import es.upm.tfm.adapters.mysqldb.respository.ItemRepository;
import es.upm.tfm.adapters.mysqldb.respository.OrderRepository;
import es.upm.tfm.adapters.mysqldb.respository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderPersistenceMysqlTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderPersistenceMysql orderPersistenceMysql;

    @Test
    void SaveOrderUserNotFound() {

        OrderDTO orderDTO = new OrderDTO();

        Mockito.when(userRepository.findById(Mockito.any(String.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderPersistenceMysql.saveOrder(orderDTO, "User1");
        });

        verify(userRepository, times(1)).findById(Mockito.any(String.class));
    }

    @Test
    void SaveOrderUserNotValid() {

        OrderDTO orderDTO = new OrderDTO();
        UserEntity user = new UserEntity("User1",  "User", "1", Collections.emptySet());

        Mockito.when(userRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(user));

        assertThrows(UserNameNotValid.class, () -> {
            orderPersistenceMysql.saveOrder(orderDTO, "User1");
        });

        verify(userRepository, times(1)).findById(Mockito.any(String.class));
    }

    @Test
    void SaveOrderOrderItemsIdsAndAmountsNotValid() {

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity role = new RoleEntity("Admin", "Role for admins");
        roles.add(role);

        OrderDTO orderDTO = new OrderDTO(new Date(-2023), "c/Alcalá 45, Madrid, 28001", Arrays.asList(1L, 2L, 3L));
        UserEntity user = new UserEntity("User1", "Alvaro", "Avilés", roles);

        Mockito.when(userRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(user));

        assertThrows(OrderItemIdsAndMountsNotValidException.class, () -> {
            orderPersistenceMysql.saveOrder(orderDTO, "User1");
        });

        verify(userRepository, times(1)).findById(Mockito.any(String.class));
    }

    @Test
    void SaveOrderItemNotFound() {

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity role = new RoleEntity("Admin", "Role for admins");
        roles.add(role);

        OrderDTO orderDTO = new OrderDTO(new Date(-2023), "c/Alcalá 45, Madrid, 28001", Arrays.asList(1L, 2L));
        UserEntity user = new UserEntity("User1", "Alvaro", "Avilés", roles);

        Mockito.when(userRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> {
            orderPersistenceMysql.saveOrder(orderDTO, "User1");
        });

        verify(userRepository, times(1)).findById(Mockito.any(String.class));
        verify(itemRepository, times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    void SaveOrder() throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity role = new RoleEntity("Admin", "Role for admins");
        roles.add(role);

        OrderDTO orderDTO = new OrderDTO(new Date(-2023), "c/Alcalá 45, Madrid, 28001", Arrays.asList(1L, 2L));
        UserEntity user = new UserEntity("User1", "Alvaro", "Avilés", roles);

        ItemEntity itemEntity = new ItemEntity(1L,"Item", "Item1", "Item1", "S", 1L, "Image", null);
        OrderEntity orderEntity = new OrderEntity(1L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", user, null);
        OrderResponse orderResponse = new OrderResponse(1L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", "User1", 2.0, 2, null);

        Mockito.when(userRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(itemEntity));
        Mockito.when(orderRepository.save(Mockito.any(OrderEntity.class))).thenReturn(orderEntity);

        OrderResponse response = orderPersistenceMysql.saveOrder(orderDTO, "User1");

        Assertions.assertEquals(response, orderResponse);

        verify(userRepository, times(1)).findById(Mockito.any(String.class));
        verify(itemRepository, times(1)).findById(Mockito.any(Long.class));
        verify(orderRepository, times(1)).save(Mockito.any(OrderEntity.class));
    }

    @Test
    void GetAllOrdersOrdersNotFound() throws OrdersNotFoundException {

        Mockito.when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(OrdersNotFoundException.class, () -> {
            orderPersistenceMysql.getAllOrders();
        });

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void GetAllOrders() throws OrdersNotFoundException {

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity role = new RoleEntity("Admin", "Role for admins");
        roles.add(role);

        UserEntity user = new UserEntity("User1", "Alvaro", "Avilés", roles);

        OrderEntity order1 = new OrderEntity(1L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", user, new HashSet<>());
        OrderEntity order2 = new OrderEntity(2L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", user, new HashSet<>());

        Set<ItemOrderResponse> itemOrders = new HashSet<>();

        OrderResponse orderResponse1 = new OrderResponse(1L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", "User1", 0, 0, itemOrders);
        OrderResponse orderResponse2 = new OrderResponse(2L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", "User1", 0, 0, itemOrders);

        Mockito.when(orderRepository.findAll()).thenReturn(List.of(order1,order2));

        List<OrderResponse> response = orderPersistenceMysql.getAllOrders();

        Assertions.assertEquals(List.of(orderResponse1,orderResponse2), response);

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void GetOrdersOrderNotFound() throws OrderNotFoundException {

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderPersistenceMysql.findById(1L);
        });

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void GetOrder() throws OrderNotFoundException {

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity role = new RoleEntity("Admin", "Role for admins");
        roles.add(role);

        UserEntity user = new UserEntity("User1", "Alvaro", "Avilés", roles);
        OrderEntity order = new OrderEntity(1L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", user, new HashSet<>());
        Set<ItemOrderResponse> itemOrders = new HashSet<>();
        OrderResponse orderResponse = new OrderResponse(1L, new Date(-2023), "c/Alcalá 45, Madrid, 28001", "User1", 0, 0, itemOrders);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderPersistenceMysql.findById(1L);

        Assertions.assertEquals(orderResponse, response);

        verify(orderRepository, times(1)).findById(1L);
    }
}
