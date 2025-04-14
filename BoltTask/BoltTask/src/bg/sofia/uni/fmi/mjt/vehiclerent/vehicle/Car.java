package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Car extends Vehicle{

    private static final int PRICE_PER_SEAT = 5;

    private FuelType fuelType;
    private int numberOfSeats;
    private double pricePerWeek;
    private double pricePerDay;
    private double pricePerHour;

    public Car(String id,
               String model,
               FuelType fuelType,
               int numberOfSeats,
               double pricePerWeek,
               double pricePerDay,
               double pricePerHour) {
        super(id, model);
        this.fuelType = fuelType;
        this.numberOfSeats = numberOfSeats;
        this.pricePerWeek = pricePerWeek;
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

        long durationInWeeks = duration.toDays() / 7;
        if(endOfRent.isBefore(startOfRent)) {
            throw new InvalidRentingPeriodException();
        }

        double totalPriceForWeeks = durationInWeeks * pricePerWeek;
        double totalPriceForDays = (duration.toDays() - durationInWeeks * 7) * pricePerDay;
        double totalPriceForHours = (duration.toHours() - duration.toDays() * 24) * pricePerHour;

        return totalPriceForWeeks + totalPriceForDays + totalPriceForHours + (numberOfSeats * PRICE_PER_SEAT) + (fuelType.getTax() * duration.toDays());
    }
}
