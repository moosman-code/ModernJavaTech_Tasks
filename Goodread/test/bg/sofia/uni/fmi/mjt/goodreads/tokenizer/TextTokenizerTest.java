package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TextTokenizerTest {

    private TextTokenizer tokenizer;

    @BeforeEach
    void setUp() {
        Reader stopWordsReader = new StringReader("the\nand\nis\nof");
        tokenizer = new TextTokenizer(stopWordsReader);
    }

    @Test
    void testStopWordsLoadedCorrectly() {
        Set<String> expected = Set.of("the", "and", "is", "of");
        assertEquals(expected, tokenizer.stopWords(), "Stop words should be loaded correctly");
    }

    @Test
    void testTokenizeFiltersStopWordsAndLowercases() {
        String input = "The Quick brown, fox jumps! over the lazy dog AND runs.";
        List<String> expected = List.of("quick", "brown", "fox", "jumps", "over", "lazy", "dog", "runs");

        List<String> actual = tokenizer.tokenize(input);

        assertEquals(expected, actual, "Tokenized output should exclude stop words and punctuation, and be lowercase");
    }

    @Test
    void testTokenizeHandlesMultipleSpacesAndEmptyTokens() {
        String input = "This     is   a   test.";
        List<String> expected = List.of("this", "a", "test");

        List<String> actual = tokenizer.tokenize(input);

        assertEquals(expected, actual, "Should correctly handle multiple spaces and exclude stop words");
    }
}
