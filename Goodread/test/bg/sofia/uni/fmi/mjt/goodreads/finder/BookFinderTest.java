package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BookFinderTest {

    private BookFinder finder;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        book1 = new Book("1", "Title1", "Author A", "This is a magic world",
                List.of("Fantasy", "Adventure"), 4.5, 1000, "url1");
        book2 = new Book("2", "Title2", "Author B", "Science meets magic in space",
                List.of("Sci-Fi", "Fantasy"), 4.2, 850, "url2");
        book3 = new Book("3", "Title3", "Author A", "Romance and emotions",
                List.of("Romance", "Drama"), 4.8, 1500, "url3");

        finder = new BookFinder(Set.of(book1, book2, book3));
    }

    @Test
    void testAllBooksReturnsCorrectSet() {
        Set<Book> all = finder.allBooks();
        assertEquals(3, all.size());
        assertTrue(all.contains(book1));
        assertTrue(all.contains(book2));
        assertTrue(all.contains(book3));
    }

    @Test
    void testAllGenresAggregatesAllGenres() {
        Set<String> genres = finder.allGenres();
        assertEquals(Set.of("Fantasy", "Adventure", "Sci-Fi", "Romance", "Drama"), genres);
    }

    @Test
    void testSearchByAuthorReturnsCorrectBooks() {
        List<Book> results = finder.searchByAuthor("Author A");
        assertEquals(2, results.size());
        assertTrue(results.contains(book1));
        assertTrue(results.contains(book3));
    }

    @Test
    void testSearchByAuthorThrowsIfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> finder.searchByAuthor(null));
        assertThrows(IllegalArgumentException.class, () -> finder.searchByAuthor("  "));
    }

    @Test
    void testSearchByGenresMatchAll() {
        List<Book> results = finder.searchByGenres(Set.of("Fantasy", "Adventure"), MatchOption.MATCH_ALL);
        assertEquals(1, results.size());
        assertEquals(book1, results.get(0));
    }

    @Test
    void testSearchByGenresMatchAny() {
        List<Book> results = finder.searchByGenres(Set.of("Fantasy"), MatchOption.MATCH_ANY);
        assertEquals(2, results.size());
        assertTrue(results.contains(book1));
        assertTrue(results.contains(book2));
    }

    @Test
    void testSearchByGenresThrowsIfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> finder.searchByGenres(null, MatchOption.MATCH_ANY));
        assertThrows(IllegalArgumentException.class, () -> finder.searchByGenres(Set.of(), MatchOption.MATCH_ALL));
    }

    @Test
    void testSearchByKeywordsMatchAll() {
        List<Book> results = finder.searchByKeywords(Set.of("magic", "world"), MatchOption.MATCH_ALL);
        assertEquals(1, results.size());
        assertEquals(book1, results.get(0));
    }

    @Test
    void testSearchByKeywordsMatchAny() {
        List<Book> results = finder.searchByKeywords(Set.of("science", "romance"), MatchOption.MATCH_ANY);
        assertEquals(2, results.size());
        assertTrue(results.contains(book2));
        assertTrue(results.contains(book3));
    }

    @Test
    void testSearchByKeywordsIsCaseInsensitive() {
        List<Book> results = finder.searchByKeywords(Set.of("MAGIC"), MatchOption.MATCH_ANY);
        assertEquals(2, results.size());
        assertTrue(results.contains(book1));
        assertTrue(results.contains(book2));
    }

    @Test
    void testSearchByKeywordsThrowsIfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> finder.searchByKeywords(null, MatchOption.MATCH_ANY));
        assertThrows(IllegalArgumentException.class, () -> finder.searchByKeywords(Set.of(), MatchOption.MATCH_ALL));
    }
}
