package es.upm.tfm.adapters.mysqldb.exception.category;

public class CategoryAlreadyAttachedToAnItem extends Throwable{

    public CategoryAlreadyAttachedToAnItem(Long id) {

        super(String.format("Category with Id %d is already attached to an item.", id));
    }

}
