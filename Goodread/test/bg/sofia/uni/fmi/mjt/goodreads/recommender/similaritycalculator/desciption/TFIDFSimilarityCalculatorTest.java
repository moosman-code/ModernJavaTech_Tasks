package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TFIDFSimilarityCalculatorTest {

    private TFIDFSimilarityCalculator calculator;
    private TextTokenizer tokenizer;
    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        StringReader stopWordsReader = new StringReader("a\nthe\nis\nand");
        tokenizer = new TextTokenizer(stopWordsReader);

        book1 = new Book("1", "Title One", "Author A", "Cats are wonderful creatures", List.of("Animals", "Pets"), 4.5, 1000, "url1");
        book2 = new Book("2", "Title Two", "Author B", "Dogs are loyal and friendly", List.of("Animals", "Pets"), 4.3, 800, "url2");

        Set<Book> allBooks = Set.of(book1, book2);
        calculator = new TFIDFSimilarityCalculator(allBooks, tokenizer);
    }

    @Test
    public void testComputeIDF() {
        Map<String, Double> idf = calculator.computeIDF(book1);
        assertTrue(idf.containsKey("cats"));
        assertTrue(idf.get("cats") > 0);
    }

    @Test
    public void testComputeTFIDF() {
        Map<String, Double> tfidf = calculator.computeTFIDF(book1);
        assertFalse(tfidf.isEmpty());
        assertTrue(tfidf.containsKey("cats"));
    }

    @Test
    public void testCalculateSimilarityThrowsForNullBooks() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(null, book2));
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(book1, null));
    }
}
