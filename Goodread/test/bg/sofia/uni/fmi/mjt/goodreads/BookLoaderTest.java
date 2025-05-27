package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookLoaderTest {

    private static final String CSV_CONTENT = """
            ID,title,author,description,genres,rating,ratingCount,URL
            1,The Hobbit,J.R.R. Tolkien,"A hobbit goes on an adventure","[Fantasy, Adventure]",4.7,"2,500",http://example.com/the-hobbit
            2,1984,George Orwell,"A dystopian society","[Dystopia, Science Fiction]",4.5,"1,000",http://example.com/1984
            """;

    @Test
    void testLoadParsesBooksCorrectly() {
        Reader reader = new StringReader(CSV_CONTENT);
        Set<Book> books = BookLoader.load(reader);

        assertEquals(2, books.size(), "Should load 2 books");

        assertTrue(books.stream().anyMatch(b -> b.title().equals("The Hobbit")), "The Hobbit should be loaded");
        assertTrue(books.stream().anyMatch(b -> b.title().equals("1984")), "1984 should be loaded");
    }

    @Test
    void testLoadThrowsForMalformedCSV() {
        String malformed = "ID,title\n1"; // not enough fields
        Reader reader = new StringReader(malformed);

        assertThrows(IllegalArgumentException.class, () -> BookLoader.load(reader));
    }

    @Test
    void testLoadSkipsHeaderRow() {
        Reader reader = new StringReader(CSV_CONTENT);
        Set<Book> books = BookLoader.load(reader);

        assertFalse(books.stream().anyMatch(b -> b.ID().equals("ID")), "Header row should be skipped");
    }

    @Test
    void testLoadEmptyDataset() {
        Reader reader = new StringReader("ID,title,author,description,genres,rating,ratingCount,URL\n");
        Set<Book> books = BookLoader.load(reader);
        assertTrue(books.isEmpty(), "Should return empty set for no books");
    }
}
