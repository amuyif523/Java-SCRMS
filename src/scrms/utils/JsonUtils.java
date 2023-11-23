package scrms.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Very small JSON helper tailored for the data persistence requirements of SCRMS.
 * The helper intentionally supports only the subset of JSON used inside the project.
 */
public final class JsonUtils {

    private JsonUtils() {
    }

    /**
     * Wraps a list of JSON object strings into a JSON array.
     *
     * @param objects serialized JSON objects
     * @return JSON array string
     */
    public static String wrapArray(List<String> objects) {
        return "[" + String.join(",", objects) + "]";
    }

    /**
     * Converts the string values into a JSON array.
     *
     * @param values list of values
     * @return JSON array representing the provided values
     */
    public static String toStringArray(List<String> values) {
        if (values == null) {
            return "[]";
        }
        List<String> quoted = new ArrayList<>();
        for (String value : values) {
            quoted.add(quote(value));
        }
        return "[" + String.join(",", quoted) + "]";
    }

    /**
     * Quotes and escapes the provided string according to JSON rules.
     *
     * @param value text to quote
     * @return quoted string literal
     */
    public static String quote(String value) {
        if (value == null) {
            return "null";
        }
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    /**
     * Splits a JSON array into the contained objects or values without parsing them.
     *
     * @param jsonArray JSON array text
     * @return list of element strings
     */
    public static List<String> splitJsonArray(String jsonArray) {
        List<String> elements = new ArrayList<>();
        if (jsonArray == null || jsonArray.isBlank()) {
            return elements;
        }
        String trimmed = jsonArray.trim();
        if (trimmed.length() < 2 || trimmed.charAt(0) != '[') {
            return elements;
        }
        trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
        if (trimmed.isEmpty()) {
            return elements;
        }
        boolean inQuotes = false;
        int braces = 0;
        int brackets = 0;
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c == '"' && (i == 0 || trimmed.charAt(i - 1) != '\\')) {
                inQuotes = !inQuotes;
            }
            if (!inQuotes) {
                if (c == '{') {
                    braces++;
                } else if (c == '}') {
                    braces--;
                } else if (c == '[') {
                    brackets++;
                } else if (c == ']') {
                    brackets--;
                } else if (c == ',' && braces == 0 && brackets == 0) {
                    elements.add(current.toString().trim());
                    current.setLength(0);
                    continue;
                }
            }
            current.append(c);
        }
        if (current.length() > 0) {
            elements.add(current.toString().trim());
        }
        return elements;
    }

    /**
     * Parses a simple JSON object containing only primitive values and arrays.
     *
     * @param json object text
     * @return map of key to raw value string
     */
    public static Map<String, String> parseJsonObject(String json) {
        Map<String, String> values = new LinkedHashMap<>();
        if (json == null || json.isBlank()) {
            return values;
        }
        String trimmed = json.trim();
        if (trimmed.charAt(0) == '{') {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        boolean inQuotes = false;
        int braces = 0;
        int brackets = 0;
        StringBuilder keyBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();
        boolean readingKey = true;
        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c == '"' && (i == 0 || trimmed.charAt(i - 1) != '\\')) {
                inQuotes = !inQuotes;
            }
            if (readingKey) {
                if (c == ':' && !inQuotes) {
                    readingKey = false;
                } else if (c != '{' || inQuotes) {
                    keyBuilder.append(c);
                }
            } else {
                if (!inQuotes) {
                    if (c == '{') {
                        braces++;
                    } else if (c == '}') {
                        braces--;
                    } else if (c == '[') {
                        brackets++;
                    } else if (c == ']') {
                        brackets--;
                    } else if (c == ',' && braces == 0 && brackets == 0) {
                        values.put(cleanKey(keyBuilder.toString()), valueBuilder.toString().trim());
                        keyBuilder.setLength(0);
                        valueBuilder.setLength(0);
                        readingKey = true;
                        continue;
                    }
                }
                valueBuilder.append(c);
            }
        }
        if (keyBuilder.length() > 0) {
            values.put(cleanKey(keyBuilder.toString()), valueBuilder.toString().trim());
        }
        return values;
    }

    private static String cleanKey(String raw) {
        return unquote(raw.trim());
    }

    /**
     * Converts a JSON array of strings into a list.
     *
     * @param jsonArray JSON array text
     * @return list of unquoted string values
     */
    public static List<String> parseStringArray(String jsonArray) {
        List<String> values = new ArrayList<>();
        for (String raw : splitJsonArray(jsonArray)) {
            values.add(unquote(raw));
        }
        return values;
    }

    /**
     * Removes leading and trailing quotes from the provided text.
     *
     * @param value string literal
     * @return unquoted text
     */
    public static String unquote(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.equals("null")) {
            return null;
        }
        if (trimmed.length() >= 2 && trimmed.charAt(0) == '"' && trimmed.charAt(trimmed.length() - 1) == '"') {
            String body = trimmed.substring(1, trimmed.length() - 1);
            return body.replace("\\\"", "\"").replace("\\\\", "\\");
        }
        return trimmed;
    }
}
