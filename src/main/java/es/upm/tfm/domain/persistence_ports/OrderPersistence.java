package es.upm.tfm.domain.persistence_ports;

import es.upm.tfm.adapters.mysqldb.dto.OrderDTO;
import es.upm.tfm.adapters.mysqldb.dto.OrderUpdateDTO;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrderItemIdsAndMountsNotValidException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrderNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrdersNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.OrderResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderPersistence {

    OrderResponse saveOrder(OrderDTO orderDTO, String userName) throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException;

    List<OrderResponse> getAllOrders() throws OrdersNotFoundException;

    OrderResponse findById(Long id) throws OrderNotFoundException;

    List<OrderResponse> getAllOrdersOfAnUser(String userName) throws OrdersNotFoundException, UserNotFoundException, UserNameNotValid;

    OrderResponse deleteById(Long id) throws OrderNotFoundException;

    OrderResponse updateOrder(OrderUpdateDTO orderUpdateDTO, Long id) throws OrderNotFoundException;
}

