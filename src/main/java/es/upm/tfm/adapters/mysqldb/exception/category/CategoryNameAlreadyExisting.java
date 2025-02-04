package es.upm.tfm.adapters.mysqldb.exception.category;

public class CategoryNameAlreadyExisting extends Throwable{

    public CategoryNameAlreadyExisting(){
        super("Category name already existing");
    }
}
