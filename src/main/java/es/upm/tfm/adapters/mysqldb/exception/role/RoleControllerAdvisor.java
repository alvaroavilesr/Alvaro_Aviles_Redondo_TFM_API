package es.upm.tfm.adapters.mysqldb.exception.role;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class RoleControllerAdvisor {

    private static final String TIMESTAMP = "timestamp";
    private static final String MESSAGE = "message";

    @ExceptionHandler(RoleNotValidException.class)
    public ResponseEntity<Object> handleRoleNotValidException(
            RoleNotValidException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(MESSAGE, "Role not in permitted roles list: [User, Vendor, Admin]");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleAlreadyExistingException.class)
    public ResponseEntity<Object> handleRoleAlreadyExisting(
            RoleAlreadyExistingException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(MESSAGE, "Role already existing in BBDD");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
