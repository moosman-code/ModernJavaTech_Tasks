package bg.sofia.uni.fmi.mjt.vehiclerent.exception;

public class VehicleAlreadyRentedException extends Exception {
    public VehicleAlreadyRentedException() {
        super("The vehicle is already rented");
    }

    public VehicleAlreadyRentedException(String message) {
        super(message);
    }

    public VehicleAlreadyRentedException(String message, Throwable cause) {
        super(message, cause);
    }
}
