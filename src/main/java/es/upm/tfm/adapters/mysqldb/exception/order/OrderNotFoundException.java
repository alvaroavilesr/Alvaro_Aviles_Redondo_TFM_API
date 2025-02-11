package es.upm.tfm.adapters.mysqldb.exception.order;

public class OrderNotFoundException extends Throwable{

    public OrderNotFoundException(Long id) {
        super(String.format("Order with id %d not found", id));
    }
}
