package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidClientCoordinatesException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidRestaurantCoordinatesException;
import bg.sofia.uni.fmi.mjt.glovo.exception.MissingDeliveryGuyOnMapLayoutException;

import java.util.*;

public class ControlCenter implements ControlCenterApi {

    MapEntity[][] mapLayout;

    private final static double IGNORE_PRICE_CONSTRAINT =  -1.0d;
    private final static int IGNORE_TIME_CONSTRAINT =  -1;

    public ControlCenter(char[][] mapLayout) {
        this.mapLayout = new MapEntity[mapLayout.length][];

        for (int i = 0; i < mapLayout.length; ++i)  {
            this.mapLayout[i] = new MapEntity[mapLayout[i].length];

            for (int j = 0; j < mapLayout[i].length; ++j)  {
                this.mapLayout[i][j] = new MapEntity(new Location(i, j), getMapEntityType(mapLayout[i][j]));
            }
        }
    }

    private MapEntityType getMapEntityType(char value)  {
        for (MapEntityType type : MapEntityType.values()) {
            if (type.getType() == value) {
                return type;
            }
        }

        throw new EnumConstantNotPresentException(MapEntityType.class,
                "No MapEntityType constant was found for the given char value");
    }

    @Override
    public DeliveryInfo findOptimalDeliveryGuy(Location clientLocation, Location restaurantLocation,
                                               double maxPrice, int maxTime, ShippingMethod shippingMethod) {

        validateArguments(restaurantLocation, clientLocation, maxPrice, maxTime, shippingMethod);

        Location bikeLocation = getDeliveryGuyLocation(MapEntityType.DELIVERY_GUY_BIKE);
        Location carLocation = getDeliveryGuyLocation(MapEntityType.DELIVERY_GUY_CAR);

        Map.Entry<Integer, Integer> priceAndTimeForBike = findOptimalDeliveryPath(clientLocation, restaurantLocation, bikeLocation,
                DeliveryType.BIKE);
        Map.Entry<Integer, Integer> priceAndTimeForCar = findOptimalDeliveryPath(clientLocation, restaurantLocation, carLocation,
                DeliveryType.CAR);

        int timeBike = priceAndTimeForBike.getKey();
        int priceBike = priceAndTimeForBike.getValue();
        int timeCar = priceAndTimeForCar.getKey();
        int priceCar = priceAndTimeForCar.getValue();

        if (shippingMethod == ShippingMethod.CHEAPEST) {
            if ((priceBike <= maxPrice ||  maxPrice == IGNORE_PRICE_CONSTRAINT) && priceBike < priceCar) {
                return new DeliveryInfo(bikeLocation, timeBike, priceBike, DeliveryType.BIKE);
            } else if (priceCar <= maxPrice ||  maxPrice == IGNORE_PRICE_CONSTRAINT) {
                return new DeliveryInfo(carLocation, timeCar, priceCar, DeliveryType.CAR);
            } else {
                return null;
            }
        }

        if ((timeBike <= maxTime || maxTime == IGNORE_TIME_CONSTRAINT) && timeBike < timeCar) {
            return new DeliveryInfo(bikeLocation, timeBike, priceBike, DeliveryType.BIKE);
        } else if (timeCar <= maxTime ||  maxTime == IGNORE_TIME_CONSTRAINT) {
            return new DeliveryInfo(carLocation, timeCar, priceCar, DeliveryType.CAR);
        } else {
            return null;
        }
    }

    private Map.Entry<Integer, Integer> findOptimalDeliveryPath(Location clientLocation,
                                                                Location restaurantLocation,
                                                                Location deliveryGuyLocation,
                                                                DeliveryType type) {
        // Find optimal path to restaurant
        int pathToRestaurant = findOptimalPath(deliveryGuyLocation, restaurantLocation);

        // Find optimal path to client
        int pathToClient = findOptimalPath(restaurantLocation, clientLocation);

        int price = (pathToRestaurant + pathToClient) * type.getPrice();
        int time = (pathToRestaurant + pathToClient) * type.getTime();

        return new AbstractMap.SimpleEntry<>(price, time);
    }

    private int findOptimalPath(Location start, Location end) {
        int rows = mapLayout.length;
        int cols = mapLayout[0].length;

        Map<Location, Integer> gScore = new HashMap<>();
        Set<Location> visited = new HashSet<>();

        PriorityQueue<Location> openSet = new PriorityQueue<>(Comparator.comparingInt(loc -> gScore.get(loc) + heuristic(loc, end)));

        gScore.put(start, 0);
        openSet.add(start);

        int[][] directions = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };

        while (!openSet.isEmpty()) {
            Location current = openSet.poll();

            if (current.equals(end)) {
                return gScore.get(current); // shortest steps
            }

            if (!visited.add(current)) continue;

            for (int[] dir : directions) {
                int nx = current.x() + dir[0];
                int ny = current.y() + dir[1];

                if (!isInBounds(nx, ny, rows, cols) || mapLayout[nx][ny].type() == MapEntityType.WALL) continue;

                Location neighbor = new Location(nx, ny);
                int tentativeG = gScore.get(current) + 1;

                if (tentativeG < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    gScore.put(neighbor, tentativeG);
                    openSet.add(neighbor);
                }
            }
        }

        return -1; // No path found
    }

    private static boolean isInBounds(int x, int y, int rows, int cols) {
        return x >= 0 && y >= 0 && x < rows && y < cols;
    }

    private static int heuristic(Location a, Location b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

    private Location getDeliveryGuyLocation(MapEntityType type)  {
        for (int i = 0; i < mapLayout.length; ++i)  {
            for (int j = 0; j < mapLayout[i].length; ++j)  {
                if (mapLayout[i][j].type() == type)  {
                    return new Location(i, j);
                }
            }
        }

        throw new MissingDeliveryGuyOnMapLayoutException(type.name());
    }

    private void validateArguments(Location restaurantLocation, Location clientLocation,
                                      double maxPrice, int maxTime, ShippingMethod shippingMethod)  {
        if  (restaurantLocation == null || clientLocation  == null ||  shippingMethod == null) {
            throw new IllegalArgumentException("Shipping method, restaurant and client location cannot be null");
        }
        if (mapLayout[restaurantLocation.x()][restaurantLocation.y()].type() != MapEntityType.RESTAURANT)  {
            throw new InvalidRestaurantCoordinatesException();
        }
        if (mapLayout[clientLocation.x()][clientLocation.y()].type() != MapEntityType.CLIENT)  {
            throw new InvalidClientCoordinatesException();
        }
        if ((maxPrice != -1 && maxPrice <= 0) || (maxTime != -1 && maxTime <= 0)) {
            throw new IllegalArgumentException("Max price and max time must be positive numbers or -1 (unset)");
        }

    }

    @Override
    public MapEntity[][] getLayout() {
        return mapLayout;
    }
}
