package bg.sofia.uni.fmi.mjt.eventbus.exception;

public class MissingSubscriptionException extends RuntimeException {
    public MissingSubscriptionException()  {
        super("The subscription is missing");
    }

    public MissingSubscriptionException(String message) {
        super(message);
    }

    public MissingSubscriptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
