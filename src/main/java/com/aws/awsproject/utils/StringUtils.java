package com.aws.awsproject.utils;

public class StringUtils {

    public static String clean(String value) {
        if (value == null) return null;
        return value.trim();
    }

    public static String cleanAndCapitalize(String value) {
        if (value == null) return null;
        String cleaned = value.trim();
        if (cleaned.isEmpty()) return cleaned;

        String[] words = cleaned.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());

                if (i < words.length - 1) {
                    result.append(" ");
                }
            }
        }

        return result.toString();
    }

    public static String cleanAndUpperCase(String value) {
        if (value == null) return null;
        return value.trim().toUpperCase();
    }
}
