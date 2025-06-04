package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookRecommenderTest {

    private SimilarityCalculator calculator;
    private BookRecommender recommender;

    private Book book1;
    private Book book2;
    private Book book3;
    private Book book4;

    @BeforeEach
    void setUp() {
        calculator = mock(SimilarityCalculator.class);

        book1 = new Book("1", "Book One", "Author A", "A story of adventure",
                List.of("Fantasy"), 4.2, 1000, "url1");

        book2 = new Book("2", "Book Two", "Author B", "Another great story",
                List.of("Fantasy"), 4.0, 800, "url2");

        book3 = new Book("3", "Book Three", "Author C", "A future story",
                List.of("Sci-Fi"), 4.5, 500, "url3");

        book4 = new Book("4", "Book Four", "Author D", "A mysterious tale",
                List.of("Mystery"), 3.9, 1200, "url4");

        Set<Book> books = Set.of(book1, book2, book3, book4);
        recommender = new BookRecommender(books, calculator);
    }

    @Test
    void testRecommendBooksThrowsOnNullOrigin() {
        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(null, 3),
                "Should throw when origin book is null");
    }

    @Test
    void testRecommendBooksThrowsOnNonPositiveMaxN() {
        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(book1, 0),
                "Should throw when maxN is not positive");
    }
}
