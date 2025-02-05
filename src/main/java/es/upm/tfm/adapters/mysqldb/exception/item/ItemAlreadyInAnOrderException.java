package es.upm.tfm.adapters.mysqldb.exception.item;

public class ItemAlreadyInAnOrderException extends Throwable{
    public ItemAlreadyInAnOrderException(Long id) {
        super(String.format("Item with Id %d is already in an order", id));
    }
}
