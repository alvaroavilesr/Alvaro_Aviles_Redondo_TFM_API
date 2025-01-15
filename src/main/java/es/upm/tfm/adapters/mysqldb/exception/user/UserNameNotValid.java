package es.upm.tfm.adapters.mysqldb.exception.user;

public class UserNameNotValid extends Throwable{
    public UserNameNotValid(String userName){
        super(String.format("User with username %s not found", userName));
    }
}
