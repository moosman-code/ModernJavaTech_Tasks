package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleItemFilter implements ItemFilter{
    private String title;
    private boolean caseSensitive;

    public TitleItemFilter(String title, boolean caseSensitive) {
        this.title = title;
        this.caseSensitive = caseSensitive;
    }

    @Override
    public boolean matches(StoreItem item) {
        Pattern pattern = Pattern.compile(title);
        if (!caseSensitive)  {
            pattern = Pattern.compile(title, Pattern.CASE_INSENSITIVE);
        }
        Matcher matcher = pattern.matcher(item.getTitle());

        return item.getTitle().matches(title);
    }
}
