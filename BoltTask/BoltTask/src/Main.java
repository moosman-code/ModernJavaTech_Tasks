import bg.sofia.uni.fmi.mjt.vehiclerent.RentalService;
import bg.sofia.uni.fmi.mjt.vehiclerent.driver.AgeGroup;
import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.Bicycle;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.Car;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.FuelType;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.Vehicle;

import java.time.LocalDateTime;

import static bg.sofia.uni.fmi.mjt.vehiclerent.driver.AgeGroup.EXPERIENCED;
import static bg.sofia.uni.fmi.mjt.vehiclerent.driver.AgeGroup.JUNIOR;

public class Main {
    public static void main(String[] args) throws VehicleAlreadyRentedException, InvalidRentingPeriodException {
        RentalService rentalService = new RentalService();
        LocalDateTime rentStart = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        Driver experiencedDriver = new Driver(EXPERIENCED);
        Driver junior = new Driver(JUNIOR);

        Vehicle electricCar = new Car("1", "Tesla Model 3", FuelType.ELECTRICITY, 4, 1000, 150, 10);
        rentalService.rentVehicle(experiencedDriver, electricCar, rentStart);
        double priceToPay = rentalService.returnVehicle(electricCar, rentStart.plusDays(5)); // 770.0

        System.out.println(priceToPay);

        Vehicle dieselCar = new Car("2", "Toyota Auris", FuelType.DIESEL, 4, 500, 80, 5);
        rentalService.rentVehicle(experiencedDriver, dieselCar, rentStart);
        priceToPay = rentalService.returnVehicle(dieselCar, rentStart.plusDays(5)); // 80*5 + 3*5 + 4*5 = 435.0

        System.out.println(priceToPay);

        Vehicle bicycle = new Bicycle("3", "Balkanche", 4, 500);
        rentalService.rentVehicle(junior, bicycle, rentStart);
        priceToPay = rentalService.returnVehicle(bicycle, rentStart.plusHours(26));

        System.out.println(priceToPay);
    }
}