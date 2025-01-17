package es.upm.tfm.adapters.mysqldb.exception.role;

public class RolesNotFoundException extends Throwable{
    public RolesNotFoundException(){
        super("No roles found");
    }
}
