package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocationsRuleTest {

    @Test
    void applicableReturnsTrueWhenDistinctLocationsMoreThanThreshold() {
        int threshold = 2;
        double weight = 3.5d;

        LocationsRule rule = new LocationsRule(threshold, weight);

        List<Transaction> transactions = List.of(
                new Transaction("t1", "a1", 100, LocalDateTime.now(), "NY", Channel.ONLINE),
                new Transaction("t2", "a1", 200, LocalDateTime.now(), "LA", Channel.ATM),
                new Transaction("t3", "a1", 300, LocalDateTime.now(), "SF", Channel.ONLINE)
        );

        assertTrue(rule.applicable(transactions));
    }

    @Test
    void applicableReturnsFalseWhenDistinctLocationsEqualToThreshold() {
        int threshold = 3;
        double weight = 3.5d;

        LocationsRule rule = new LocationsRule(threshold, weight);

        List<Transaction> transactions = List.of(
                new Transaction("t1", "a1", 100, LocalDateTime.now(), "NY", Channel.ONLINE),
                new Transaction("t2", "a1", 200, LocalDateTime.now(), "LA", Channel.ATM),
                new Transaction("t3", "a1", 300, LocalDateTime.now(), "SF", Channel.ONLINE)
        );

        assertFalse(rule.applicable(transactions));
    }

    @Test
    void applicableReturnsFalseWhenDistinctLocationsLessThanThreshold() {
        int threshold = 4;
        double weight = 3.5d;

        LocationsRule rule = new LocationsRule(threshold, weight);

        List<Transaction> transactions = List.of(
                new Transaction("t1", "a1", 100, LocalDateTime.now(), "NY", Channel.ONLINE),
                new Transaction("t2", "a1", 200, LocalDateTime.now(), "LA", Channel.ATM),
                new Transaction("t3", "a1", 300, LocalDateTime.now(), "SF", Channel.ONLINE)
        );

        assertFalse(rule.applicable(transactions));
    }

    @Test
    void weightReturnsCorrectWeight() {
        int threshold = 3;
        double weight = 4.2d;

        LocationsRule rule = new LocationsRule(threshold, weight);

        assertEquals(weight, rule.weight());
    }
}
