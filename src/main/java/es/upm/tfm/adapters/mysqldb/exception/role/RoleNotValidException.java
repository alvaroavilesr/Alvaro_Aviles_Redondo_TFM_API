package es.upm.tfm.adapters.mysqldb.exception.role;

public class RoleNotValidException extends Throwable{

    public RoleNotValidException(String id){
        super(String.format("Role %s not in permitted roles: [User, Vendor, Admin]", id));
    }
}
