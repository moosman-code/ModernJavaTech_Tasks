package bg.sofia.uni.fmi.mjt.goodreads.book;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Book(
        String ID,
        String title,
        String author,
        String description,
        List<String> genres,
        double rating,
        int ratingCount,
        String URL
) {

    public static Book of(String[] tokens) {
        if (tokens.length != 8) {
            throw new IllegalArgumentException("Tokens not enough for new Book  instance");
        }

        try {
            List<String> genres = getGenres(tokens[4]);
            String ratingCount = tokens[6].replace("\"", "").replace(",", "");

            return new Book(tokens[0],
                    tokens[1],
                    tokens[2],
                    tokens[3].trim().replaceAll("\\s+", " "),
                    genres,
                    Double.parseDouble(tokens[5]),
                    Integer.parseInt(ratingCount),
                    tokens[7]);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getGenres(String token) throws IOException {
        String trimmed = token.replaceAll("\\[", "").replaceAll("]", "");
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();
        String[] values = parser.parseLine(trimmed);
        ArrayList<String> genres = new ArrayList<>(Arrays.asList(values));
        genres.replaceAll(String::trim);

        return genres;
    }
}