package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;

public record Transaction(String transactionID, String accountID,
                          double transactionAmount, LocalDateTime transactionDate,
                          String location, Channel channel) {

    public static Transaction of(String line) {
        String[] parts = line.split(",");

        return new Transaction(
                parts[0],
                parts[1],
                Double.parseDouble(parts[2]),
                LocalDateTime.parse(parts[3]),
                parts[4],
                Channel.valueOf(parts[5])
        );
    }
}
