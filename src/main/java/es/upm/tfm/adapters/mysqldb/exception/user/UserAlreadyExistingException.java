package es.upm.tfm.adapters.mysqldb.exception.user;

public class UserAlreadyExistingException extends Throwable{
    public UserAlreadyExistingException(String userName){
        super(String.format("User with username %s already exists", userName));
    }
}
