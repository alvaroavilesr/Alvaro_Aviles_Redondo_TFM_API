package es.upm.tfm.adapters.mysqldb.exception.role;

public class RoleAlreadyExistingException extends Throwable{

    public RoleAlreadyExistingException(String role){
        super(String.format("Role %s already existing in BBDD", role));
    }
}
