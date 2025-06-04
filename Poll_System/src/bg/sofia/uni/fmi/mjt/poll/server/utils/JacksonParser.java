package bg.sofia.uni.fmi.mjt.poll.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class JacksonParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String parseToJsonMessage(String status, String message) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("status", status);
        json.put("message", message);

        return toJson(json);
    }

    public static String parseToJSONObject(String status, String key, Object object) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("status", status);
        json.put(key, object);

        return toJson(json);
    }

    private static String toJson(Map<String, Object> json) {
        try {
            return mapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }
}
