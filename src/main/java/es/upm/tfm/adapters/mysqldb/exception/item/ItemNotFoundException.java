package es.upm.tfm.adapters.mysqldb.exception.item;

public class ItemNotFoundException extends Throwable{
    public ItemNotFoundException(Long id) {
        super(String.format("Item with Id %d not found", id));
    }
}
