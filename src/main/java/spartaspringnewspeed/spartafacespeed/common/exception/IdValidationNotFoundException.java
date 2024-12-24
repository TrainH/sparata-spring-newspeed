package spartaspringnewspeed.spartafacespeed.common.exception;

public class IdValidationNotFoundException extends RuntimeException {
    public IdValidationNotFoundException(Long id) {super("Does not found id = " + id);}
}
