import bg.sofia.uni.fmi.mjt.sentimentanalyzer.*;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Main {
    private static final String RESOURCES_PATH = "src/bg/sofia/uni/fmi/mjt/sentimentanalyzer/resources/";

    public static void main(String[] args) {
        try  (BufferedReader bufferedReader1 = new BufferedReader(
                new FileReader(RESOURCES_PATH + "books/book1.txt"));
              BufferedReader bufferedReader2 = new BufferedReader(
                      new FileReader(RESOURCES_PATH + "books/book2.txt"));
              BufferedReader stopWordsReader = new BufferedReader(
                      new FileReader(RESOURCES_PATH + "stopwords.txt"));
              BufferedReader sentimentLexiconReader = new BufferedReader(
                      new FileReader(RESOURCES_PATH + "AFINN-111.txt"))) {

            AnalyzerInput book1 = new AnalyzerInput("book1.txt", bufferedReader1);
            AnalyzerInput book2 = new AnalyzerInput("book2.txt", bufferedReader2);

            Set<String> stopWords = AnalyzerUtil.getStopWords(stopWordsReader);
            Map<String, SentimentScore> sentimentLexicons = AnalyzerUtil.getSentimentLexicon(sentimentLexiconReader);

            SentimentAnalyzerAPI analyzer = new ParallelSentimentAnalyzer(20, stopWords, sentimentLexicons);

            long start = System.nanoTime();
            Map<String, SentimentScore> results = analyzer.analyze(book1, book2);
            long end = System.nanoTime();
            System.out.println((end - start) / 1000000 + "ms");

            for (var pair : results.entrySet()) {
                System.out.println(pair.getKey() + ": " + pair.getValue());
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
