package bg.sofia.uni.fmi.mjt.sentimentalanyzer;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerInput;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.ParallelSentimentAnalyzer;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentScore;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParallelSentimentAnalyzerTest {

    private Map<String, SentimentScore> getMockSentimentLexicon() {
        return Map.of(
                "love", SentimentScore.fromScore(3),
                "hate", SentimentScore.fromScore(-3),
                "happy", SentimentScore.fromScore(2),
                "sad", SentimentScore.fromScore(-2),
                "good", SentimentScore.fromScore(1),
                "fun", SentimentScore.fromScore(2),
                "bad", SentimentScore.fromScore(-1)
        );
    }

    private Set<String> getMockStopWords() {
        return Set.of(
                "sometimes",
                "work",
                "I"
        );
    }

    @Test
    public void testAnalyzeWithMultipleInputs() {
        ParallelSentimentAnalyzer analyzer = new ParallelSentimentAnalyzer(2, getMockStopWords(), getMockSentimentLexicon());

        String input1 =
                """
                I love programming, it's so fun. But sometimes I hate when the code doesn't work.
                Today was a good day. I felt happy and accomplished, though I had some challenges.
                I feel so sad today. Everything seems bad and nothing is going right.
                I love working on new projects. However, I hate the pressure of deadlines.
                Life is good. I am happy with my work and personal life.
                The weather is nice today, which makes me feel good. I love sunny days.
                I feel bad about the mistakes I made yesterday. It's tough to fix things.
                Hate is such a strong word. It's better to focus on good things.
                Good things come to those who wait. I am confident about the future.
                Sad to see my friends leave, but I know they will be successful in their new journey.
                """;

        String input2 =
                """
                I love programming, it's so fun. But sometimes I hate when the code doesn't work.
                Today was a good day. I felt happy and accomplished, though I had some challenges.
                I feel so sad today. Everything seems bad and nothing is going right.
                I love working on new projects. However, I hate the pressure of deadlines.
                Life is good. I am happy with my work and personal life.
                The weather is nice today, which makes me feel good. I love sunny days.
                I feel bad about the mistakes I made yesterday. It's tough to fix things.
                Hate is such a strong word. It's better to focus on good things.
                Good things come to those who wait. I am confident about the future.
                Sad to see my friends leave, but I know they will be successful in their new journey.
                """;

        AnalyzerInput[] analyzerInputs = new AnalyzerInput[2];
        analyzerInputs[0] = new AnalyzerInput("ID-" + 1, new BufferedReader(new StringReader(input1)));
        analyzerInputs[1] = new AnalyzerInput("ID-" + 2, new BufferedReader(new StringReader(input2)));

        Map<String, SentimentScore> results = analyzer.analyze(analyzerInputs);

        assertEquals(2, results.size(), "Expected 2 results");

        assertTrue(results.get("ID-1").getScore() >= 0, "Expected a positive sentiment for input 0");
        assertTrue(results.get("ID-2").getScore() >= 0, "Expected a positive sentiment for input 1");
    }
}