package spartaspringnewspeed.spartafacespeed.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleLoginException(LoginException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<String> handleValidateException(ValidateException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IdValidationNotFoundException.class)
    public ResponseEntity<String> handleIdValidationNotFoundException(IdValidationNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotOwnerActionException.class)
    public ResponseEntity<String> handleNotOwnerActionException(NotOwnerActionException exception) {
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(CannotLikeOwnContentException.class)
    public ResponseEntity<String> handleCannotLikeOwnContentException(CannotLikeOwnContentException exception) {
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
