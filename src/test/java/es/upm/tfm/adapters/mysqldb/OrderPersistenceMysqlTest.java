package es.upm.tfm.adapters.mysqldb;

import es.upm.tfm.adapters.mysqldb.dto.OrderDTO;
import es.upm.tfm.adapters.mysqldb.entity.ItemEntity;
import es.upm.tfm.adapters.mysqldb.entity.OrderEntity;
import es.upm.tfm.adapters.mysqldb.entity.RoleEntity;
import es.upm.tfm.adapters.mysqldb.entity.UserEntity;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrderItemIdsAndMountsNotValidException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.persistence.OrderPersistenceMysql;
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
    }

    @Test
    void SaveOrderUserNotValid() {

        OrderDTO orderDTO = new OrderDTO();
        UserEntity user = new UserEntity("User1",  "User", "1", Collections.emptySet());

        Mockito.when(userRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(user));

        assertThrows(UserNameNotValid.class, () -> {
            orderPersistenceMysql.saveOrder(orderDTO, "User1");
        });
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
    }
}

