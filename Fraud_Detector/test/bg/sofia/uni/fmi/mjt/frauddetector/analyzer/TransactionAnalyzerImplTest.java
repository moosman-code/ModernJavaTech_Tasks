package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionAnalyzerImplTest {

    private static final String CSV_DATA =
            "transactionID,accountID,transactionAmount,transactionDate,location,channel\n" +
                    "t1,a1,100.0,2023-05-01 10:15:30,NY,ONLINE\n" +
                    "t2,a2,200.0,2023-05-02 11:15:30,LA,ATM\n" +
                    "t3,a1,50.0,2023-05-03 12:15:30,NY,ONLINE\n";

    private Rule rule1;
    private Rule rule2;
    private TransactionAnalyzerImpl analyzer;

    @BeforeEach
    void setUp() throws IOException {
        // Mock rules with fixed weights and behavior
        rule1 = mock(Rule.class);
        when(rule1.weight()).thenReturn(0.6);
        when(rule1.applicable(anyList())).thenReturn(true);

        rule2 = mock(Rule.class);
        when(rule2.weight()).thenReturn(0.4);
        when(rule2.applicable(anyList())).thenReturn(false);

        Reader reader = new StringReader(CSV_DATA);
        analyzer = new TransactionAnalyzerImpl(reader, List.of(rule1, rule2));
    }

    @Test
    void allTransactionsReturnsAllParsedTransactions() {
        List<Transaction> transactions = analyzer.allTransactions();
        assertEquals(3, transactions.size());

        assertEquals("t1", transactions.get(0).transactionID());
        assertEquals("a1", transactions.get(0).accountID());
    }

    @Test
    void allAccountIDsReturnsAllAccountIDs() {
        List<String> accountIDs = analyzer.allAccountIDs();
        assertEquals(List.of("a1", "a2", "a1"), accountIDs);
    }

    @Test
    void transactionCountByChannelReturnsCorrectCounts() {
        Map<Channel, Integer> counts = analyzer.transactionCountByChannel();
        assertEquals(2, counts.get(Channel.ONLINE));
        assertEquals(1, counts.get(Channel.ATM));
    }

    @Test
    void amountSpentByUserReturnsSumOfAmounts() {
        double amount = analyzer.amountSpentByUser("a1");
        assertEquals(150.0d, amount);

        amount = analyzer.amountSpentByUser("a2");
        assertEquals(200.0d, amount);
    }

    @Test
    void amountSpentByUserThrowsForNull() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.amountSpentByUser(null));
    }

    @Test
    void allTransactionsByUserReturnsCorrectTransactions() {
        List<Transaction> userTransactions = analyzer.allTransactionsByUser("a1");
        assertEquals(2, userTransactions.size());

        userTransactions = analyzer.allTransactionsByUser("a2");
        assertEquals(1, userTransactions.size());
    }

    @Test
    void allTransactionsByUserThrowsForNull() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.allTransactionsByUser(null));
    }

    @Test
    void accountRatingCalculatesWeightedSum() {
        when(rule1.applicable(anyList())).thenReturn(true);
        when(rule2.applicable(anyList())).thenReturn(false);

        double rating = analyzer.accountRating("a1");
        assertEquals(0.6d, rating);

        when(rule1.applicable(anyList())).thenReturn(false);
        when(rule2.applicable(anyList())).thenReturn(true);

        rating = analyzer.accountRating("a1");
        assertEquals(0.4d, rating);
    }

    @Test
    void accountRatingThrowsForNull() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.accountRating(null));
    }
}
