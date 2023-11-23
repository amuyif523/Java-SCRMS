package scrms.exceptions;

/**
 * Exception thrown when validation rules fail.
 */
public class ValidationException extends RuntimeException {

    /**
     * Creates a new validation exception with a message.
     *
     * @param message description of the validation failure
     */
    public ValidationException(String message) {
        super(message);
    }
}
