package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidClientCoordinatesException extends RuntimeException {
    public InvalidClientCoordinatesException() {
        super("Client coordinates do not correspond to the correct MapEntity type");
    }

    public InvalidClientCoordinatesException(String message) {
        super(message);
    }

    public InvalidClientCoordinatesException(String message, Throwable cause) {
        super(message, cause);
    }
}
