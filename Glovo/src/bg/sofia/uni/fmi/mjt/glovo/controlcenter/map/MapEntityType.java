package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

public enum MapEntityType {
    ROAD('.'),
    WALL('#'),
    RESTAURANT('R'),
    CLIENT('C'),
    DELIVERY_GUY_CAR('A'),
    DELIVERY_GUY_BIKE('B');

    private final char type;

    public char getType()  {
        return type;
    }

    private MapEntityType(char type) {
        this.type = type;
    }
}