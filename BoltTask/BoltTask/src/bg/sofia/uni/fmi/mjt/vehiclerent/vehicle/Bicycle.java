package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import java.time.Duration;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Bicycle extends Vehicle {
    private double pricePerDay;
    private double pricePerHour;

    public Bicycle(String id, String model, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    @Override
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
        if (rentalEnd == null) {
            throw new IllegalArgumentException("End of rent cannot be null");
        }
        if (rentalEnd.isBefore(startOfRent)) {
            throw new InvalidRentingPeriodException();
        }
        if (rentedBy == null) {
            throw new VehicleNotRentedException();
        }

        rentedBy = null;
        this.endOfRent = rentalEnd;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if (startOfRent == null || endOfRent == null) {
            throw new IllegalArgumentException("Start and end of date cannot be null");
        }
        Duration duration = Duration.between(startOfRent, endOfRent);

        long durationInDays = duration.toDays();
        if(endOfRent.isBefore(startOfRent) || durationInDays >= 7) {
            throw new InvalidRentingPeriodException();
        }

        double totalPriceForDays = durationInDays * pricePerDay;
        double totalPriceForHours = (duration.toHours() - durationInDays * 24) * pricePerHour;

        return totalPriceForDays + totalPriceForHours;
    }
}
