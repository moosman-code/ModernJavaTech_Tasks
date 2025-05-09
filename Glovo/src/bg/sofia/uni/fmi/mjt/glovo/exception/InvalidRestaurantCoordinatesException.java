package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidRestaurantCoordinatesException extends RuntimeException {
    public InvalidRestaurantCoordinatesException() {
        super("Restaurant coordinates do not correspond to the correct MapEntity type");
    }

    public InvalidRestaurantCoordinatesException(String message) {
        super(message);
    }

    public InvalidRestaurantCoordinatesException(String message, Throwable cause) {
        super(message, cause);
    }
}
