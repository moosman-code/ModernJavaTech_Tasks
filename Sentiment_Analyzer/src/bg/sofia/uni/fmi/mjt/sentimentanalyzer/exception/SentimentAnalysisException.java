package bg.sofia.uni.fmi.mjt.sentimentanalyzer.exception;

public class SentimentAnalysisException extends RuntimeException {
    public SentimentAnalysisException() {
        super("Exception during sentiment analysis");
    }

    public SentimentAnalysisException(String message) {
        super(message);
    }

    public SentimentAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
