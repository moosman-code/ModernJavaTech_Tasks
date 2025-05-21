import bg.sofia.uni.fmi.mjt.frauddetector.analyzer.TransactionAnalyzer;
import bg.sofia.uni.fmi.mjt.frauddetector.analyzer.TransactionAnalyzerImpl;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.FrequencyRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.LocationsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.SmallTransactionsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.ZScoreRule;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Period;
import java.util.List;

public class Main {

    public static void main(String... args) throws IOException {
        String filePath = "src/dataset.csv";

        Reader reader = new FileReader(filePath);
        List<Rule> rules = List.of(
                new ZScoreRule(1.5, 0.3),
                new LocationsRule(3, 0.4),
                new FrequencyRule(4, Period.ofWeeks(4), 0.25),
                new SmallTransactionsRule(1, 10.20, 0.05)
        );

        try {
            TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, rules);

            System.out.println(analyzer.allAccountIDs());
            System.out.println(analyzer.allTransactionsByUser(analyzer.allTransactions().getFirst().accountID()));
            System.out.println(analyzer.accountsRisk());
        } catch (IOException e) {
            throw e;
        }
    }

}