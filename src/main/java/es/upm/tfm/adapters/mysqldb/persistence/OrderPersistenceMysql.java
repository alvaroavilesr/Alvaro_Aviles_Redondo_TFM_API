package es.upm.tfm.adapters.mysqldb.persistence;

import es.upm.tfm.adapters.mysqldb.dto.OrderDTO;
import es.upm.tfm.adapters.mysqldb.entity.ItemEntity;
import es.upm.tfm.adapters.mysqldb.entity.ItemOrderEntity;
import es.upm.tfm.adapters.mysqldb.entity.OrderEntity;
import es.upm.tfm.adapters.mysqldb.entity.UserEntity;
import es.upm.tfm.adapters.mysqldb.exception.item.ItemNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrderItemIdsAndMountsNotValidException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrderNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.order.OrdersNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNameNotValid;
import es.upm.tfm.adapters.mysqldb.exception.user.UserNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.OrderResponse;
import es.upm.tfm.adapters.mysqldb.respository.ItemRepository;
import es.upm.tfm.adapters.mysqldb.respository.OrderRepository;
import es.upm.tfm.adapters.mysqldb.respository.UserRepository;
import es.upm.tfm.domain.persistence_ports.OrderPersistence;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderPersistenceMysql implements OrderPersistence {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public OrderPersistenceMysql(OrderRepository orderRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public OrderResponse saveOrder(OrderDTO orderDTO, String userName) throws UserNotFoundException, UserNameNotValid, OrderItemIdsAndMountsNotValidException, ItemNotFoundException {
        UserEntity user = userRepository.findById(userName).orElseThrow(() -> new UserNotFoundException(userName));
        if(user.getRole().isEmpty()){
            throw new UserNameNotValid(userName);
        }

        OrderEntity order = modelMapper.map(orderDTO, OrderEntity.class);
        order.setUser(user);

        if(orderDTO.getItemIdsAndAmounts().size() % 2 != 0){
            throw new OrderItemIdsAndMountsNotValidException();
        }

        List<ItemEntity> items = new ArrayList<>();

        for (int i = 0; i < orderDTO.getItemIdsAndAmounts().size(); i = i + 2){
            Long itemId = orderDTO.getItemIdsAndAmounts().get(i);
            items.add(itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId)));
        }

        List<Long> amounts = new ArrayList<>();

        for(int i = 1; i < orderDTO.getItemIdsAndAmounts().size(); i = i + 2){
            amounts.add(orderDTO.getItemIdsAndAmounts().get(i));
        }

        List<ItemOrderEntity> itemsOrder = new ArrayList<>();

        for(int i = 0; i < items.size(); i++){
            ItemOrderEntity itemOrder = new ItemOrderEntity();
            itemOrder.setItem(items.get(i));
            itemOrder.setAmount(amounts.get(i));
            itemsOrder.add(itemOrder);
        }

        order.setItemsOrder(new HashSet<>(itemsOrder));

        OrderEntity savedOrder = orderRepository.save(order);
        OrderResponse orderResponse = modelMapper.map(savedOrder, OrderResponse.class);
        orderResponse.setUserName(userName);
        orderResponse.setPrice(itemsOrder.stream().mapToDouble(item -> item.getItem().getPrice() * item.getAmount()).sum());
        orderResponse.setItemAmount(itemsOrder.stream().mapToInt(item -> Math.toIntExact(item.getAmount())).sum());

        return orderResponse;
    }

    @Transactional
    public List<OrderResponse> getAllOrders() throws OrdersNotFoundException {
        List<OrderEntity> orders = orderRepository.findAll();

        if (orders.isEmpty()){
            throw new OrdersNotFoundException();
        }else{
            List<OrderResponse> orderResponses = orders.stream()
                    .map(order -> modelMapper.map(order, OrderResponse.class))
                    .toList();
            orderResponses.forEach(orderResponse -> orderResponse.setPrice(
                    orderResponse.getItemsOrder().stream().mapToDouble(item -> item.getAmount() * item.getItem().getPrice()).sum())
            );
            orderResponses.forEach(orderResponse -> orderResponse.setItemAmount(orderResponse.getItemsOrder().stream().mapToInt(
                    item -> Math.toIntExact(item.getAmount())
            ).sum()));
            return orderResponses;
        }
    }

    @Transactional
    public OrderResponse findById(Long id) throws OrderNotFoundException {
        OrderResponse orderResponse = orderRepository.findById(id)
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .orElseThrow(() -> new OrderNotFoundException(id));
        orderResponse.setPrice(orderResponse.getItemsOrder().stream().mapToDouble(item -> item.getItem().getPrice() * item.getAmount()).sum());
        orderResponse.setItemAmount(orderResponse.getItemsOrder().stream().mapToInt(
                item -> Math.toIntExact(item.getAmount())
        ).sum());
        return orderResponse;
    }

    @Transactional
    public List<OrderResponse> getAllOrdersOfAnUser(String userName) throws UserNotFoundException, OrdersNotFoundException, UserNameNotValid {
        UserEntity user = userRepository.findById(userName).orElseThrow(() -> new UserNotFoundException(userName));
        if(user.getRole().isEmpty()){
            throw new UserNameNotValid(userName);
        }
        List<OrderEntity> orders = orderRepository.findAll();

        List<OrderResponse> orderResponses = orders.stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .filter(orderResponse -> Objects.equals(orderResponse.getUserName(), user.getUserName()))
                .toList();
        if (orderResponses.isEmpty()){
            throw new OrdersNotFoundException();
        }
        orderResponses.forEach(orderResponse -> orderResponse.setPrice(
                orderResponse.getItemsOrder().stream().mapToDouble(item -> item.getAmount() * item.getItem().getPrice()).sum())
        );
        orderResponses.forEach(orderResponse -> orderResponse.setItemAmount(orderResponse.getItemsOrder().stream().mapToInt(
                item -> Math.toIntExact(item.getAmount())
        ).sum()));
        return orderResponses;
    }
}
