package es.upm.tfm.adapters.mysqldb.exception.role;

public class RoleNotFoundException extends Throwable{
    public RoleNotFoundException(String roleName){
        super(String.format("Role %s not found", roleName));
    }
}
