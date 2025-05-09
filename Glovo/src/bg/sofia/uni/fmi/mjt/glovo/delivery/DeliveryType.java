package bg.sofia.uni.fmi.mjt.glovo.delivery;

public enum DeliveryType {
    CAR(5, 3),
    BIKE(3, 5);

    private final int pricePerKilometer;
    private final int timePerKilometer;

    public int getPrice()  {
        return pricePerKilometer;
    }

    public int getTime() {
        return timePerKilometer;
    }

    private DeliveryType(int pricePerKilometer, int timePerKilometer) {
        this.pricePerKilometer = pricePerKilometer;
        this.timePerKilometer = timePerKilometer;
    }
}
