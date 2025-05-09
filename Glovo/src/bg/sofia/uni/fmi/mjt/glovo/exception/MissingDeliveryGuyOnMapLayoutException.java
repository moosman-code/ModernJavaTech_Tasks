package bg.sofia.uni.fmi.mjt.glovo.exception;

public class MissingDeliveryGuyOnMapLayoutException extends RuntimeException {
    public MissingDeliveryGuyOnMapLayoutException() {
        super("Delivery guy is missing on the map layout");
    }

    public MissingDeliveryGuyOnMapLayoutException(String message) {
        super(message + " is missing on the map layout");
    }

    public MissingDeliveryGuyOnMapLayoutException(String message, Throwable cause) {
        super(message + " is missing on the map layout", cause);
    }
}
