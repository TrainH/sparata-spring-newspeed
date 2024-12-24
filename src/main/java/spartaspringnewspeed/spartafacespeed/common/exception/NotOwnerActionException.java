package spartaspringnewspeed.spartafacespeed.common.exception;

public class NotOwnerActionException extends RuntimeException {
    public NotOwnerActionException() {
        super("You are not allowed this.");
    }
}
