package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenresOverlapSimilarityCalculatorTest {

    private GenresOverlapSimilarityCalculator calculator;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        calculator = new GenresOverlapSimilarityCalculator();

        book1 = new Book("1", "Book One", "Author A", "Desc A",
                List.of("Fantasy", "Adventure"), 4.5, 100, "url1");

        book2 = new Book("2", "Book Two", "Author B", "Desc B",
                List.of("Fantasy", "Mystery"), 4.0, 200, "url2");

        book3 = new Book("3", "Book Three", "Author C", "Desc C",
                List.of("Romance", "Drama"), 3.8, 150, "url3");
    }

    @Test
    void testCalculateSimilarity_NoCommonGenres() {
        double similarity = calculator.calculateSimilarity(book1, book3);
        assertEquals(0.0, similarity, 1e-6);
    }

    @Test
    void testCalculateSimilarity_IdenticalGenres() {
        Book copy = new Book("4", "Copy", "Copy Author", "Copy Desc",
                List.of("Fantasy", "Adventure"), 4.6, 130, "url4");

        double similarity = calculator.calculateSimilarity(book1, copy);
        assertEquals(1.0, similarity, 1e-6);
    }

    @Test
    void testCalculateSimilarity_NullFirstBook_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateSimilarity(null, book1));
    }

    @Test
    void testCalculateSimilarity_NullSecondBook_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateSimilarity(book1, null));
    }
}
