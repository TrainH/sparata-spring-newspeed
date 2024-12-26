package spartaspringnewspeed.spartafacespeed.common.exception;

public class CannotLikeOwnContentException extends RuntimeException {
    public CannotLikeOwnContentException() {
        super("The owner can't press like.");
    }
}
