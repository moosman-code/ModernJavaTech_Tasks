package bg.sofia.uni.fmi.mjt.poll.server.model;

import java.util.LinkedHashMap;
import java.util.Map;

public record Poll(String question, Map<String, Integer> options) {

    public static Poll of(String input) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid poll format");
        }

        String question = parts[0].replace('-', ' ');
        String[] optionTokens = parts[1].split(" ");

        Map<String, Integer> options = new LinkedHashMap<>();
        for (String opt : optionTokens) {
            if (options.containsKey(opt)) {
                throw new IllegalArgumentException("Duplicate option: " + opt);
            }
            options.put(opt, 0);
        }

        return new Poll(question, options);
    }

    public void updateVote(String input) {

    }
}