package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Caravan extends Vehicle{
    private static final int PRICE_PER_SEAT = 5;
    private static final int PRICE_PER_BED = 10;

    private FuelType fuelType;
    private int numberOfSeats;
    private int numberOfBeds;
    private double pricePerWeek;
    private double pricePerDay;
    private double pricePerHour;

    public Caravan(String id,
                   String model,
                   FuelType fuelType,
                   int numberOfSeats,
                   int numberOfBeds,
                   double pricePerWeek,
                   double pricePerDay,
                   double pricePerHour) {
        super(id, model);
        this.fuelType = fuelType;
        this.numberOfSeats = numberOfSeats;
        this.numberOfBeds = numberOfBeds;
        this.pricePerWeek = pricePerWeek;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    @Override
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
        if (rentalEnd == null) {
            throw new IllegalArgumentException("End of rent cannot be null");
        }
        if (rentedBy == null) {
            throw new VehicleNotRentedException();
        }

        Duration duration = Duration.between(startOfRent, rentalEnd);
        if (rentalEnd.isBefore(startOfRent) || duration.toDays() < 1) {
            throw new InvalidRentingPeriodException();
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
        if(endOfRent.isBefore(startOfRent) || duration.toDays() < 1) {
            throw new InvalidRentingPeriodException();
        }

        double totalPriceForWeeks = durationInWeeks * pricePerWeek;
        double totalPriceForDays = (duration.toDays() - durationInWeeks * 7) * pricePerDay;
        double totalPriceForHours = (duration.toHours() - duration.toDays() * 24) * pricePerHour;

        return totalPriceForWeeks + totalPriceForDays + totalPriceForHours + (numberOfSeats * PRICE_PER_SEAT)
                + (numberOfBeds * PRICE_PER_BED)
                + (fuelType.getTax() * duration.toDays());
    }
}
