package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Vehicle {

    protected static final int DAYS_IN_YEAR = LocalDate.now().lengthOfYear();

    protected String id;
    protected String model;
    protected Driver rentedBy;
    protected LocalDateTime startOfRent;
    protected LocalDateTime endOfRent;

    public Vehicle(String id, String model) {
        this.id = id;
        this.model = model;
        this.rentedBy = null;
        this.startOfRent = null;
        this.endOfRent = null;
    }

    public LocalDateTime getStartOfRent() {
        return startOfRent;
    }

    public LocalDateTime getEndOfRent() {
        return endOfRent;
    }

    public int getDeposit() {
        return rentedBy.group().getTax();
    }

    public boolean isRented() {
        return rentedBy != null;
    }

    /**
     * Simulates rental of the vehicle. The vehicle now is considered rented by the provided driver and the start of the rental is the provided date.
     * @param driver the driver that wants to rent the vehicle.
     * @param startRentTime the start time of the rent
     * @throws VehicleAlreadyRentedException in case the vehicle is already rented by someone else or by the same driver.
     */
    public void rent(Driver driver, LocalDateTime startRentTime) throws VehicleAlreadyRentedException {
        if (rentedBy != null) {
            throw new VehicleAlreadyRentedException();
        }
        if (startRentTime == null) {
            throw new IllegalArgumentException("Start of rent cannot be null");
        }

        rentedBy = driver;
        startOfRent = startRentTime;
    }

    /**
     * Simulates end of rental for the vehicle - it is no longer rented by a driver.
     * @param rentalEnd time of end of rental
     * @throws IllegalArgumentException in case @rentalEnd is null
     * @throws VehicleNotRentedException in case the vehicle is not rented at all
     * @throws InvalidRentingPeriodException in case the rentalEnd is before the currently noted start date of rental or
     * in case the Vehicle does not allow the passed period for rental, e.g. Caravans must be rented for at least a day
     * and the driver tries to return them after an hour.
     */
    public abstract void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException;

    /**
     * Used to calculate potential rental price without the vehicle to be rented.
     * The calculation is based on the type of the Vehicle (Car/Caravan/Bicycle).
     *
     * @param startOfRent the beginning of the rental
     * @param endOfRent the end of the rental
     * @return potential price for rent
     * @throws InvalidRentingPeriodException in case the vehicle cannot be rented for that period of time or
     * the period is not valid (end date is before start date)
     */
    public abstract double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException;

}
