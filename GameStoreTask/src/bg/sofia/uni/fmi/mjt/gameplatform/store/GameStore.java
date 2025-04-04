package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

import java.math.BigDecimal;

public class GameStore implements StoreAPI{
    private StoreItem[] games;

    private final static int PROCENTAGE_VAN40 = 40; //PERCENTAGE
    private final static int PROCENTAGE_100YO = 100;

    public GameStore(StoreItem[] games) {
        this.games = new StoreItem[games.length];
        for (int i = 0; i < games.length; ++i)  {
            this.games[i] = games[i];
        }
    }

    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] itemFilters) {
        StoreItem[] result = new StoreItem[itemFilters.length];
        int index = 0;
        for (int i = 0; i < itemFilters.length; ++i)  {
            for (int j = 0; j < games.length; ++j) {
                if (itemFilters[i].matches(games[j])) {
                    result[index] = games[j];
                    ++index;
                }
            }
        }

        return result;
    }

    @Override
    public void applyDiscount(String promoCode) {
            switch(promoCode) {
                case "VAN40" -> decreasePrice(PROCENTAGE_VAN40);
                case "100YO" -> decreasePrice(PROCENTAGE_100YO);
                default -> System.out.println("Incorrect promo code");
            }
    }

    @Override
    public boolean rateItem(StoreItem item, int rating) {
        for (int i = 0; i < games.length; ++i) {
            if (games[i].getTitle().equals(item.getTitle())) {
                games[i].rate(rating);
            }
        }

        return false;
    }

    private void decreasePrice(double procents) {
        BigDecimal currentPrice  = new BigDecimal(0);
        for (int i = 0; i < games.length; ++i)  {
            currentPrice = this.games[i].getPrice();
            this.games[i].setPrice(currentPrice.subtract(currentPrice.multiply(BigDecimal.valueOf(procents))));
        }
    }
}
