package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ZScoreRuleTest {

    @Test
    void applicableReturnsTrueWhenTransactionExceedsZScoreThreshold() {
        double zScoreThreshold = 2.0d;
        double weight = 4.5d;

        ZScoreRule rule = new ZScoreRule(zScoreThreshold, weight);

        List<Transaction> transactions = List.of(
                new Transaction("t1", "a1", 100, LocalDateTime.now(), "NY", Channel.ONLINE),
                new Transaction("t2", "a1", 110, LocalDateTime.now(), "NY", Channel.ATM),
                new Transaction("t3", "a1", 90, LocalDateTime.now(), "NY", Channel.ONLINE),
                new Transaction("t4", "a1", 300, LocalDateTime.now(), "NY", Channel.ONLINE) // outlier
        );

        assertFalse(rule.applicable(transactions));
    }

    @Test
    void applicableReturnsFalseWhenNoTransactionExceedsZScoreThreshold() {
        double zScoreThreshold = 2.0d;
        double weight = 1.0d;

        ZScoreRule rule = new ZScoreRule(zScoreThreshold, weight);

        List<Transaction> transactions = List.of(
                new Transaction("t1", "a1", 100, LocalDateTime.now(), "NY", Channel.ONLINE),
                new Transaction("t2", "a1", 105, LocalDateTime.now(), "NY", Channel.ATM),
                new Transaction("t3", "a1", 95, LocalDateTime.now(), "NY", Channel.ONLINE),
                new Transaction("t4", "a1", 110, LocalDateTime.now(), "NY", Channel.ONLINE)
        );

        assertFalse(rule.applicable(transactions));
    }

    @Test
    void applicableReturnsFalseWhenStdDevIsZero() {
        double zScoreThreshold = 1.0d;
        double weight = 2.0d;

        ZScoreRule rule = new ZScoreRule(zScoreThreshold, weight);

        List<Transaction> transactions = List.of(
                new Transaction("t1", "a1", 100, LocalDateTime.now(), "NY", Channel.ONLINE),
                new Transaction("t2", "a1", 100, LocalDateTime.now(), "NY", Channel.ATM),
                new Transaction("t3", "a1", 100, LocalDateTime.now(), "NY", Channel.ONLINE)
        );

        assertFalse(rule.applicable(transactions));
    }

    @Test
    void weightReturnsCorrectWeight() {
        double zScoreThreshold = 2.0d;
        double weight = 6.7d;

        ZScoreRule rule = new ZScoreRule(zScoreThreshold, weight);

        assertEquals(weight, rule.weight());
    }
}
