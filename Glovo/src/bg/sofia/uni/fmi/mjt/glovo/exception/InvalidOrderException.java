package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException() {
        super("Invalid order");
    }

    public InvalidOrderException(String message) {
        super(message);
    }

    public InvalidOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
