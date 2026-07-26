package util;

import java.time.LocalDate;

public class Validation {

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isValidDateRange(LocalDate postDate, LocalDate expiryDate) {
        if (postDate == null || expiryDate == null) return false;
        return !expiryDate.isBefore(postDate);
    }

    public static boolean isValidCategory(String category) {
        if (isNullOrEmpty(category)) return false;
        String c = category.toUpperCase();
        return c.equals("EXAM") || c.equals("EVENT") || c.equals("PLACEMENT")
                || c.equals("HOLIDAY") || c.equals("GENERAL");
    }
}
