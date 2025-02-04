package es.upm.tfm.adapters.mysqldb.exception.category;

public class CategoryNotFoundException extends Throwable{

    public CategoryNotFoundException(Long id){

        super(String.format("Category with Id %d not found", id));
    }

}
