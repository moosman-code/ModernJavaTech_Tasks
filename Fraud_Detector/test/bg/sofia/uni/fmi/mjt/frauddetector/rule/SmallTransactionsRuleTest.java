package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmallTransactionsRuleTest {

    @Test
    void applicableReturnsTrueWhenCountOfSmallTransactionsExceedsThreshold() {
        int countThreshold = 2;
        double amountThreshold = 100.0d;
        double weight = 7.5d;

        SmallTransactionsRule rule = new SmallTransactionsRule(countThreshold, amountThreshold, weight);

        List<Transaction> transactions = List.of(
                new Transaction("t1", "a1", 50.0, LocalDateTime.now(), "NY", Channel.ONLINE),
                new Transaction("t2", "a1", 75.0, LocalDateTime.now(), "LA", Channel.ATM),
                new Transaction("t3", "a1", 120.0, LocalDateTime.now(), "SF", Channel.ONLINE),
                new Transaction("t4", "a1", 90.0, LocalDateTime.now(), "LA", Channel.ONLINE)
        );

        assertTrue(rule.applicable(transactions));
    }

    @Test
    void applicableReturnsFalseWhenCountOfSmallTransactionsEqualsThreshold() {
        int countThreshold = 3;
        double amountThreshold = 100.0d;
        double weight = 1.5d;

        SmallTransactionsRule rule = new SmallTransactionsRule(countThreshold, amountThreshold, weight);

        List<Transaction> transactions = List.of(
                new Transaction("t1", "a1", 50.0, LocalDateTime.now(), "NY", Channel.ONLINE),
                new Transaction("t2", "a1", 75.0, LocalDateTime.now(), "LA", Channel.ATM),
                new Transaction("t3", "a1", 120.0, LocalDateTime.now(), "SF", Channel.ONLINE),
                new Transaction("t4", "a1", 90.0, LocalDateTime.now(), "LA", Channel.ONLINE)
        );

        assertFalse(rule.applicable(transactions));
    }

    @Test
    void applicableReturnsFalseWhenCountOfSmallTransactionsBelowThreshold() {
        int countThreshold = 4;
        double amountThreshold = 100.0d;
        double weight = 2.0d;

        SmallTransactionsRule rule = new SmallTransactionsRule(countThreshold, amountThreshold, weight);

        List<Transaction> transactions = List.of(
                new Transaction("t1", "a1", 50.0, LocalDateTime.now(), "NY", Channel.ONLINE),
                new Transaction("t2", "a1", 150.0, LocalDateTime.now(), "LA", Channel.ATM),
                new Transaction("t3", "a1", 120.0, LocalDateTime.now(), "SF", Channel.ONLINE),
                new Transaction("t4", "a1", 90.0, LocalDateTime.now(), "LA", Channel.ONLINE)
        );

        assertFalse(rule.applicable(transactions));
    }

    @Test
    void weightReturnsCorrectWeight() {
        int countThreshold = 1;
        double amountThreshold = 100.0d;
        double weight = 8.8d;

        SmallTransactionsRule rule = new SmallTransactionsRule(countThreshold, amountThreshold, weight);

        assertEquals(weight, rule.weight());
    }
}
