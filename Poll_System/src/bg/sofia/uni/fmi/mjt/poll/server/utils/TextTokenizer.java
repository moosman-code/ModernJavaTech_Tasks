package bg.sofia.uni.fmi.mjt.poll.server.utils;

import java.util.*;
import java.util.stream.Collectors;

public class TextTokenizer {

    public static Map.Entry<String, List<String>> getArguments(String input) {
        input = input.trim().replaceAll("\\s+", " ");

        int firstSpaceIndex = input.indexOf(' ');
        if (firstSpaceIndex == -1) {
            throw new IllegalArgumentException("Invalid client input");
        }

        String question = input.substring(0, firstSpaceIndex);
        String rest = input.substring(firstSpaceIndex + 1);

        List<String> answers = Arrays.stream(rest.split(" "))
                .collect(Collectors.toList());

        return new AbstractMap.SimpleEntry<>(question, answers);
    }
}
