package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ParallelSentimentAnalyzer implements SentimentAnalyzerAPI {

    private final int workersCount;
    public static Set<String> stopWords;
    public static Map<String, SentimentScore> sentimentLexicon;

    /**
     * @param workersCount number of consumer workers
     * @param stopWords set containing stop words
     * @param sentimentLexicon map containing the sentiment lexicon, where the key is the word and the value is the sentiment score
     */
    public ParallelSentimentAnalyzer(int workersCount, Set<String> stopWords, Map<String, SentimentScore> sentimentLexicon) {
        this.workersCount = workersCount;
        this.stopWords = stopWords;
        this.sentimentLexicon = sentimentLexicon;
    }

    @Override
    public Map<String, SentimentScore> analyze(AnalyzerInput... input) {
        Map<String, SentimentScore> result = new ConcurrentHashMap<>();
        CustomBlockingQueue<AnalyzerInput> queue = new CustomBlockingQueue<>();

        List<Thread> producers = new ArrayList<>();
        List<Thread> consumers = new ArrayList<>();

        for (var analyzer : input) {
            Thread producer = new Thread(() -> {
                try {
                    queue.add(analyzer);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            producer.start();
            producers.add(producer);
        }

        for (int i = 0 ; i < workersCount; ++i) {
            Thread consumer = new Thread(() -> {
                try {
                    while(!queue.isEmpty()) {
                        AnalyzerInput analyzer = queue.take();
                        if (!result.containsKey(analyzer.inputID())) {
                            result.put(analyzer.inputID(), calculateSentimentScore(analyzer.inputReader()));
                        }
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            consumer.start();
            consumers.add(consumer);
        }

        for (Thread producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (Thread consumer : consumers) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    private SentimentScore calculateSentimentScore(Reader reader) {
        String text = ((BufferedReader) reader).lines().reduce("", (line1, line2)  ->  line1 + " " + line2);
        String[] words = text.replaceAll("[\\p{Punct}&&[^']]", "").toLowerCase().split("\\s+");
        OptionalDouble averageScore = Arrays.stream(words)
                .parallel()
                .filter(word -> !stopWords.contains(word) && sentimentLexicon.containsKey(word))
                .mapToInt(word -> sentimentLexicon.get(word).getScore())
                .average();
        double presence = averageScore.isPresent() ? averageScore.getAsDouble() : 0;
        return SentimentScore.fromScore(
                Math.clamp(Math.round(presence),
                        SentimentScore.VERY_NEGATIVE.getScore(),
                        SentimentScore.VERY_POSITIVE.getScore()));
    }
}
