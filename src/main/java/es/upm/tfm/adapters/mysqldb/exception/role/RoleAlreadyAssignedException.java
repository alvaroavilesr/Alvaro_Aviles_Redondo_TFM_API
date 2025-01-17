package es.upm.tfm.adapters.mysqldb.exception.role;

public class RoleAlreadyAssignedException extends Throwable{
    public RoleAlreadyAssignedException(String role){
        super(String.format("Role %s already assigned to some user", role));
    }
}
