package es.upm.tfm.adapters.mysqldb.exception.item;

public class NoItemsFoundException extends RuntimeException{
    public NoItemsFoundException(){
        super("No data found!");
    }
}
