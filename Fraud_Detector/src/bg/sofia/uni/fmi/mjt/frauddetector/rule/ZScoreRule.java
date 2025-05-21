package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ZScoreRule implements Rule {

    private final double zScoreThreshold;
    private final double weight;

    public ZScoreRule(double zScoreThreshold, double weight) {
        this.zScoreThreshold = zScoreThreshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        double mean = transactions.stream()
                .mapToDouble(Transaction::transactionAmount)
                .average()
                .orElse(0.0);

        double variance = transactions.stream()
                .mapToDouble(tx -> Math.pow(tx.transactionAmount() - mean, 2))
                .average()
                .orElse(0.0);

        double stdDev = Math.sqrt(variance);

        return transactions.stream()
                .mapToDouble(tx -> stdDev == 0.0 ? 0.0 : (tx.transactionAmount() - mean) / stdDev)
                .anyMatch(z -> Math.abs(z) > zScoreThreshold);
    }

    @Override
    public double weight() {
        return this.weight;
    }
}
