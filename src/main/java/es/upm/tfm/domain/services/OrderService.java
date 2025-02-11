package es.upm.tfm.domain.services;

import es.upm.tfm.adapters.mysqldb.dto.OrderDTO;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrderItemIdsAndMountsNotValidException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrdersNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.OrderResponse;
import es.upm.tfm.domain.persistence_ports.OrderPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderPersistence orderPersistence;

    @Autowired
    @Lazy
    public OrderService(OrderPersistence orderPersistence) {
        this.orderPersistence = orderPersistence;
    }

    public OrderResponse saveOrder(OrderDTO orderDTO, String userName) throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {
        return this.orderPersistence.saveOrder(orderDTO, userName);
    }

    public List<OrderResponse> getAllOrders() throws OrdersNotFoundException {
        return this.orderPersistence.getAllOrders();
    }
}
