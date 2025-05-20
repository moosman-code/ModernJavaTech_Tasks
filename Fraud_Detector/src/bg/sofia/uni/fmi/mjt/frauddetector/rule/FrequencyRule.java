package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

public class FrequencyRule implements Rule {

    private final int transactionCountThreshold;
    private final TemporalAmount timeWindow;
    private final double weight;

    public FrequencyRule(int transactionCountThreshold, TemporalAmount timeWindow, double weight)  {
        this.transactionCountThreshold = transactionCountThreshold;
        this.timeWindow = timeWindow;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        LocalDateTime from = LocalDateTime.now().minus(timeWindow);

        long count = transactions.stream()
                .filter(transaction -> transaction.transactionDate().isBefore(from))
                .count();

        return count > transactionCountThreshold;
    }

    @Override
    public double weight() {
        return this.weight;
    }
}
