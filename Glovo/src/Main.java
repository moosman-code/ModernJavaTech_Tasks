import bg.sofia.uni.fmi.mjt.glovo.Glovo;
import bg.sofia.uni.fmi.mjt.glovo.GlovoApi;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;

public class Main {
    public static void main(String[] args) {
        char[][] layout = {
                {'#', '#', '#', '.', '#'},
                {'#', '.', 'A', 'R', '.'},
                {'.', '.', '#', '.', '#'},
                {'#', 'C', '.', 'B', '.'},
                {'#', '.', '#', '#', '#'}
        };

        MapEntity restaurant = new MapEntity(new Location(1, 3), MapEntityType.RESTAURANT);
        MapEntity client = new MapEntity(new Location(3, 1), MapEntityType.CLIENT);
        String foodItem = "Oreo";

        GlovoApi glovo = new Glovo(layout);
        Delivery cheapestDelivery = glovo.getCheapestDelivery(client, restaurant, foodItem);
        Delivery fastestDelivery = glovo.getFastestDelivery(client, restaurant, foodItem);
        Delivery fastestDeliveryWithPriceLimit = glovo.getFastestDeliveryUnderPrice(client, restaurant, foodItem, 15);
        Delivery cheapestDeliveryWithTimeLimit = glovo.getCheapestDeliveryWithinTimeLimit(client, restaurant, foodItem, 30);
    }
}