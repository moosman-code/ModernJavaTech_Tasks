package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookFinder implements BookFinderAPI {

    private final Set<Book> books;

    public BookFinder(Set<Book> books) {
        this.books = books;
    }

    @Override
    public Set<Book> allBooks() {
        return books;
    }

    @Override
    public Set<String> allGenres() {
        return books.stream()
                .map(Book::genres)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }


    @Override
    public List<Book> searchByAuthor(String authorName) {
        if (authorName == null ||  authorName.isBlank()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }

        return books.stream()
                .filter(book -> book.author().equals(authorName))
                .toList();
    }

    @Override
    public List<Book> searchByGenres(Set<String> genres, MatchOption option) {
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Genres cannot be null or empty");
        }

        return option == MatchOption.MATCH_ALL
                ? books.stream()
                .filter(book -> new HashSet<>(book.genres()).containsAll(genres))
                .toList()
                : books.stream()
                .filter(book -> book.genres().stream().anyMatch(genres::contains))
                .toList();
    }


    @Override
    public List<Book> searchByKeywords(Set<String> keywords, MatchOption option) {
        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords cannot be null or empty");
        }

        Set<String> lowerKeywords = keywords.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        return books.stream()
                .filter(book -> {
                    Set<String> words = Arrays.stream(book.description().split("\\s+"))
                            .map(String::toLowerCase)
                            .collect(Collectors.toSet());

                    return option == MatchOption.MATCH_ALL
                            ? words.containsAll(lowerKeywords)
                            : words.stream().anyMatch(lowerKeywords::contains);
                })
                .toList();
    }
}
