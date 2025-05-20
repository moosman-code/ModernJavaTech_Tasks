package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FrequencyRuleTest {

    @Test
    void applicableReturnsTrueIfMoreThanThresholdTransactionsBeforeFrom() {
        TemporalAmount timeWindow = Duration.ofHours(1);
        int threshold = 2;
        double weight = 5.0;

        FrequencyRule rule = new FrequencyRule(threshold, timeWindow, weight);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeWindow = now.minusHours(2); // before "from"

        List<Transaction> transactions = List.of(
                new Transaction("t1", "a1", 100, beforeWindow.minusMinutes(1), "NY", Channel.ONLINE),
                new Transaction("t2", "a1", 200, beforeWindow.minusMinutes(2), "NY", Channel.ATM),
                new Transaction("t3", "a1", 300, beforeWindow.minusMinutes(3), "NY", Channel.ONLINE)
        );

        assertTrue(rule.applicable(transactions));
    }

    @Test
    void applicableReturnsFalseIfLessOrEqualThresholdTransactionsBeforeFrom() {
        TemporalAmount timeWindow = Duration.ofHours(1);
        int threshold = 3;
        double weight = 1.0d;

        FrequencyRule rule = new FrequencyRule(threshold, timeWindow, weight);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeWindow = now.minusHours(2);

        List<Transaction> transactions = List.of(
                new Transaction("t1", "a1", 100, beforeWindow.minusMinutes(1), "NY", Channel.ONLINE),
                new Transaction("t2", "a1", 200, beforeWindow.minusMinutes(2), "NY", Channel.ATM),
                new Transaction("t3", "a1", 300, beforeWindow.minusMinutes(3), "NY", Channel.ONLINE)
        );

        assertFalse(rule.applicable(transactions));
    }

    @Test
    void applicableReturnsFalseIfNoTransactionsBeforeFrom() {
        TemporalAmount timeWindow = Duration.ofHours(1);
        int threshold = 1;
        double weight = 2.0d;

        FrequencyRule rule = new FrequencyRule(threshold, timeWindow, weight);

        LocalDateTime now = LocalDateTime.now();

        List<Transaction> transactions = List.of(
                new Transaction("t1", "a1", 100, now.plusMinutes(1), "NY", Channel.ONLINE),
                new Transaction("t2", "a1", 200, now.plusMinutes(2), "NY", Channel.ATM)
        );

        assertFalse(rule.applicable(transactions));
    }

    @Test
    void weightReturnsCorrectWeight() {
        TemporalAmount timeWindow = Duration.ofHours(1);
        int threshold = 1;
        double weight = 10.5d;

        FrequencyRule rule = new FrequencyRule(threshold, timeWindow, weight);

        assertEquals(weight, rule.weight());
    }
}
