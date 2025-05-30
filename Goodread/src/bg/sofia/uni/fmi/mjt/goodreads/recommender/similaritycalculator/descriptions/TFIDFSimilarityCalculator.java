package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.*;
import java.util.stream.Collectors;

public class TFIDFSimilarityCalculator implements SimilarityCalculator {

    private final Set<Book> books;
    private final TextTokenizer tokenizer;

    public TFIDFSimilarityCalculator(Set<Book> books, TextTokenizer tokenizer) {
        this.books = books;
        this.tokenizer = tokenizer;
    }

    /*
     * Do not modify!
     */
    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null)  {
            throw new IllegalArgumentException("Passed books cannot be null");
        }

        Map<String, Double> tfIdfScoresFirst = computeTFIDF(first);
        Map<String, Double> tfIdfScoresSecond = computeTFIDF(second);

        return cosineSimilarity(tfIdfScoresFirst, tfIdfScoresSecond);
    }

    public Map<String, Double> computeTFIDF(Book book) {
        Map<String, Double> wordsTF = computeTF(book);
        Map<String, Double> wordsIDF = computeIDF(book);

        return wordsTF.keySet().stream()
                .collect(Collectors.toMap(
                        word -> word,
                        word -> wordsIDF.getOrDefault(word, 0.0d) * wordsTF.get(word)
                ));
    }

    public Map<String, Double> computeTF(Book book) {
        List<String> descriptionWords = tokenizer.tokenize(book.description());
        int totalWords = descriptionWords.size();
        Set<String> uniqueWords = new HashSet<>(descriptionWords);

        return uniqueWords.stream()
                .collect(Collectors.toMap(
                        word -> word,
                        word -> (double) (Collections.frequency(descriptionWords, word) / totalWords)
                ));
    }


    public Map<String, Double> computeIDF(Book book) {
        Set<String> uniqueWords = new HashSet<>(tokenizer.tokenize(book.description()));

        int totalBooks = books.size();

        return uniqueWords.stream()
                .collect(Collectors.toMap(
                        word -> word,
                        word -> {
                            long containingBooks = countBookDescriptionsContainingWord(word);
                            if (containingBooks == 0) return 0.0d;
                            return Math.log10((double) totalBooks / containingBooks);
                        }
                ));
    }

    private long countBookDescriptionsContainingWord(String word) {
         return books.stream()
                 .map(Book::description)
                 .map(tokenizer::tokenize)
                 .filter(des -> des.contains(word))
                 .count();
    }

    private double cosineSimilarity(Map<String, Double> first, Map<String, Double> second) {
        double magnitudeFirst = magnitude(first.values());
        double magnitudeSecond = magnitude(second.values());

        return dotProduct(first, second) / (magnitudeFirst * magnitudeSecond);
    }

    private double dotProduct(Map<String, Double> first, Map<String, Double> second) {
        Set<String> commonKeys = new HashSet<>(first.keySet());
        commonKeys.retainAll(second.keySet());

        return commonKeys.stream()
                .mapToDouble(word -> first.get(word) * second.get(word))
                .sum();
    }

    private double magnitude(Collection<Double> input) {
        double squaredMagnitude = input.stream()
                .map(v -> v * v)
                .reduce(0.0, Double::sum);

        return Math.sqrt(squaredMagnitude);
    }
}