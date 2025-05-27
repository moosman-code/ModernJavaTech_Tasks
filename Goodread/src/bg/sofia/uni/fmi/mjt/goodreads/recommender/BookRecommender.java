package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.TreeMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;

public class BookRecommender implements BookRecommenderAPI{

    private final Set<Book> books;
    private final SimilarityCalculator calculator;

    public BookRecommender(Set<Book> books, SimilarityCalculator calculator) {
        this.books = books;
        this.calculator = calculator;
    }

    @Override
    public SortedMap<Book, Double> recommendBooks(Book originBook, int maxN) {
        if (originBook == null || maxN <= 0) {
            throw new IllegalArgumentException("Book of origin cannot be null and number of entries must be positive");
        }

        return books.stream()
                .filter(book -> !book.equals(originBook))
                .map(book -> Map.entry(book, calculator.calculateSimilarity(book, originBook)))
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(maxN)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        TreeMap::new
                ));

    }

}
