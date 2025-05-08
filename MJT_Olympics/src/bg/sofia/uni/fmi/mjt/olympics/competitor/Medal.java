package bg.sofia.uni.fmi.mjt.olympics.competitor;

public enum Medal {
    GOLD(1),
    SILVER(2),
    BRONZE(3);

    private final int place;

    public int getPlace() {
        return place;
    }

    private Medal(int place) {
        this.place = place;
    }
}