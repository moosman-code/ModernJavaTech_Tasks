package bg.sofia.uni.fmi.mjt.goodreads.book;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    void testBookOfCreatesBookCorrectly() {
        String[] tokens = {
                "id123",
                "Some Title",
                "Author Name",
                "   A fantasy   world with dragons.   ",
                "[Fantasy, Adventure]",
                "4.5",
                "\"1,000\"",
                "http://example.com"
        };

        Book book = Book.of(tokens);

        assertEquals("id123", book.ID());
        assertEquals("Some Title", book.title());
        assertEquals("Author Name", book.author());
        assertEquals("A fantasy world with dragons.", book.description()); // whitespace normalized
        assertEquals(List.of("Fantasy", "Adventure"), book.genres());
        assertEquals(4.5, book.rating(), 0.001);
        assertEquals(1000, book.ratingCount());
        assertEquals("http://example.com", book.URL());
    }

    @Test
    void testBookOfThrowsIfTokensLengthInvalid() {
        String[] tooFew = {"id", "title", "author"};
        assertThrows(IllegalArgumentException.class, () -> Book.of(tooFew));
    }

    @Test
    void testBookOfHandlesGenresWithExtraWhitespace() {
        String[] tokens = {
                "id789",
                "Title",
                "Author",
                "Something descriptive",
                "[  Romance ,  Drama  ,Fantasy ]",
                "4.0",
                "600",
                "http://example.com"
        };

        Book book = Book.of(tokens);
        assertEquals(List.of("Romance", "Drama", "Fantasy"), book.genres());
    }

    @Test
    void testBookOfHandlesMalformedRatingCount() {
        String[] tokens = {
                "id890",
                "Title",
                "Author",
                "Some description",
                "[Sci-Fi]",
                "4.1",
                "\"2,500\"",
                "http://example.com"
        };

        Book book = Book.of(tokens);
        assertEquals(2500, book.ratingCount());
    }
}
