package in.anurag.groceryapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExecptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
     public ResponseEntity<?> handleIllegalArg(IllegalArgumentException ex){
         Map<String,Object> body = new HashMap<>();
         body.put("success",false);
         body.put("message",ex.getMessage());
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
     }

     @ExceptionHandler(MethodArgumentNotValidException.class)
     public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex){
        Map<String,Object> body = new HashMap<>();
        body.put("success",false);
        Map<String,String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        body.put("errors",errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
     }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("message", "Not authorized");
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthentication(AuthenticationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("message", "Not authorized");
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFound(ProductNotFoundException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
