package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Set;
import java.util.stream.Collectors;

public class GenresOverlapSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null)  {
            throw new IllegalArgumentException("Passed books cannot be null");
        }

        Set<String> firstGenres = first.genres().stream().distinct().collect(Collectors.toSet());
        Set<String> secondGenres = second.genres().stream().distinct().collect(Collectors.toSet());

        long numberIntersectWords = firstGenres.stream()
                .distinct()
                .filter(secondGenres::contains)
                .count();

        return (double) (numberIntersectWords / Math.min(firstGenres.size(), secondGenres.size()));
    }

}