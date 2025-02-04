package es.upm.tfm.adapters.mysqldb.exception.category;

public class CategoriesNotFoundException extends Throwable{

    public CategoriesNotFoundException(){
        super("No categories found!");
    }

}
