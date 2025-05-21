package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class SmallTransactionsRule implements Rule {

    private final int countThreshold;
    private final double amountThreshold;
    private final double weight;

    public SmallTransactionsRule(int countThreshold, double amountThreshold, double weight) {
        this.countThreshold = countThreshold;
        this.amountThreshold = amountThreshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        long count = transactions.stream()
                .map(Transaction::transactionAmount)
                .filter(amount -> amount <= amountThreshold)
                .count();

        return count > countThreshold;
    }

    @Override
    public double weight() {
        return this.weight;
    }
}
