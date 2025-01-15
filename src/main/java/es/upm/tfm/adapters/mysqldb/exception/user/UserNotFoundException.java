package es.upm.tfm.adapters.mysqldb.exception.user;

public class UserNotFoundException extends Throwable{
    public UserNotFoundException(String userName){
        super(String.format("User with username %s not found", userName));
    }
}
