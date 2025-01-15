package es.upm.tfm.adapters.mysqldb.exception.user;

public class UsersNotFoundException extends Throwable{
    public UsersNotFoundException(){
        super("No users found");
    }
}

