package es.upm.tfm.adapters.mysqldb.exception.order;

public class OrdersNotFoundException extends Throwable{

    public OrdersNotFoundException() {
        super("No orders found in the BBDD");
    }
}