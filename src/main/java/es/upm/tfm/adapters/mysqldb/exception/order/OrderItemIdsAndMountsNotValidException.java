package es.upm.tfm.adapters.mysqldb.exception.order;

public class OrderItemIdsAndMountsNotValidException extends Throwable{
    public OrderItemIdsAndMountsNotValidException() {

        super("The format of the list of ItemIds and Amounts is not correct. It should have even length" +
                " and the ItemIds should be in even positions, meanwhile amounts in odd positions.");
    }
}
