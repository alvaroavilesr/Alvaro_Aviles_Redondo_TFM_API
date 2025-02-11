package es.upm.tfm.adapters.mysqldb.exception.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class OrderControllerAdvisor {

    private static final String TIMESTAMP = "timestamp";
    private static final String MESSAGE = "message";

    @ExceptionHandler(OrdersNotFoundException.class)
    public ResponseEntity<Object> handleOrdersNotFoundException(
            OrdersNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(MESSAGE, "Orders not found");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFoundException(
            OrderNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(MESSAGE, "Order not found");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderItemIdsAndMountsNotValidException.class)
    public ResponseEntity<Object> handleOrderItemIdsAndMountsNotValid(
            OrderItemIdsAndMountsNotValidException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(MESSAGE, "ItemIds and Amounts list not valid");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
