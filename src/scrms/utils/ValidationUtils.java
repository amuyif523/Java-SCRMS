package scrms.utils;

import scrms.exceptions.ValidationException;

import java.util.Collection;

/**
 * Helper methods for validating user input and domain entities.
 */
public final class ValidationUtils {

    private ValidationUtils() {
    }

    /**
     * Ensures the provided text is neither null nor blank.
     *
     * @param value   text to validate
     * @param message error message when validation fails
     */
    public static void requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(message);
        }
    }

    /**
     * Ensures the provided number is positive.
     *
     * @param number  number to validate
     * @param message error message when validation fails
     */
    public static void requirePositiveNumber(int number, String message) {
        if (number <= 0) {
            throw new ValidationException(message);
        }
    }

    /**
     * Ensures the provided email text contains a basic @ separator.
     *
     * @param email text to validate
     */
    public static void requireEmail(String email) {
        requireText(email, "Email is required");
        if (!email.contains("@")) {
            throw new ValidationException("Email must contain '@'");
        }
    }

    /**
     * Ensures the collection is not empty.
     *
     * @param collection collection to inspect
     * @param message    error message when validation fails
     */
    public static void requireNotEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new ValidationException(message);
        }
    }
}
