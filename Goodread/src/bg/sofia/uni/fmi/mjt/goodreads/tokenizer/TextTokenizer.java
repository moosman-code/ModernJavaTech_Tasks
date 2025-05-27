package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TextTokenizer {

    private final Set<String> stopWords;

    public TextTokenizer(Reader stopWordsReader) {
        try (var br = new BufferedReader(stopWordsReader)) {
            stopWords = br.lines().collect(Collectors.toSet());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load dataset", ex);
        }
    }

    public List<String> tokenize(String input) {
        List<String> words = Arrays.stream(input.replaceAll("\\p{Punct}", "")
                .replaceAll("\\s+", " ")
                .toLowerCase()
                .split(" "))
                .toList();

        return words.stream()
                .filter(word -> !stopWords.contains(word))
                .toList();
    }

    public Set<String> stopWords() {
        return stopWords;
    }

}