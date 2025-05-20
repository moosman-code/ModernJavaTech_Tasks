package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {

    private List<Rule> rules;
    private List<Transaction> transactions;

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) throws IOException {
        double rulesWeight = rules.stream().
                map(Rule::weight).
                reduce(0.0d, Double::sum);
        if (rulesWeight !=  1.0d) {
            throw new IllegalArgumentException("Rules weights must be summed up to 1");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.rules = rules;
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            this.transactions = bufferedReader.lines()
                    .skip(1)
                    .map(line ->  line.split(","))
                    .map(part -> new Transaction(
                            part[0],
                            part[1],
                            Double.parseDouble(part[2]),
                            LocalDateTime.parse(part[3], formatter),
                            part[4],
                            Channel.valueOf(part[5].toUpperCase())))
            .toList();

        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public List<Transaction> allTransactions() {
        return transactions;
    }

    @Override
    public List<String> allAccountIDs() {
        return transactions.stream()
                .map(Transaction::accountID)
                .toList();
    }

    @Override
    public Map<Channel, Integer> transactionCountByChannel() {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::channel,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue
                        )
                ));
    }

    @Override
    public double amountSpentByUser(String accountID) {
        if (accountID == null) {
            throw new IllegalArgumentException("Account Id cannot be null");
        }

        return transactions.stream()
                .filter(transaction -> transaction.accountID().equals(accountID))
                .map(Transaction::transactionAmount)
                .reduce(0.0d, Double::sum);
    }

    @Override
    public List<Transaction> allTransactionsByUser(String accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account Id cannot be null");
        }

        return transactions.stream()
                .filter(transaction -> transaction.accountID().equals(accountId))
                .toList();
    }

    @Override
    public double accountRating(String accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account Id cannot be null");
        }

        List<Transaction> accountTransactions = transactions.stream()
                .filter(tx -> tx.accountID().equals(accountId))
                .toList();

        return rules.stream()
                .mapToDouble(rule -> rule.applicable(accountTransactions) ? rule.weight() : 0.0d)
                .sum();
    }

    @Override
    public SortedMap<String, Double> accountsRisk() {
        Map<String, List<Transaction>> transactionsByAccount = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::accountID));

        Map<String, Double> riskMap = transactionsByAccount.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> rules.stream()
                                .mapToDouble(rule -> rule.applicable(e.getValue()) ? rule.weight() :  0.0d)
                                .sum()
                ));

        return riskMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        TreeMap::new
                ));
    }
}
