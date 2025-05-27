package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CompositeSimilarityCalculatorTest {

    private SimilarityCalculator genreCalculator;
    private SimilarityCalculator descriptionCalculator;
    private CompositeSimilarityCalculator compositeCalculator;
    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        genreCalculator = Mockito.mock(SimilarityCalculator.class);
        descriptionCalculator = Mockito.mock(SimilarityCalculator.class);

        compositeCalculator = new CompositeSimilarityCalculator(Map.of(
                genreCalculator, 0.6,
                descriptionCalculator, 0.4
        ));

        book1 = new Book("1", "Title One", "Author A", "Description one",
                List.of("Fiction", "Adventure"), 4.5, 100, "url1");

        book2 = new Book("2", "Title Two", "Author B", "Description two",
                List.of("Adventure", "Mystery"), 4.3, 90, "url2");
    }

    @Test
    public void testCalculateSimilarityWeightedAverage() {
        Mockito.when(genreCalculator.calculateSimilarity(book1, book2)).thenReturn(0.8);
        Mockito.when(descriptionCalculator.calculateSimilarity(book1, book2)).thenReturn(0.5);

        double result = compositeCalculator.calculateSimilarity(book1, book2);

        // Expected 0.68
        assertEquals(0.68, result, 1e-6);
    }

    @Test
    public void testCalculateSimilarityThrowsForNullBooks() {
        assertThrows(IllegalArgumentException.class, () -> compositeCalculator.calculateSimilarity(null, book2));
        assertThrows(IllegalArgumentException.class, () -> compositeCalculator.calculateSimilarity(book1, null));
    }
}
