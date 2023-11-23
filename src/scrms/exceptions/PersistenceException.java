package scrms.exceptions;

/**
 * Exception thrown when file persistence operations fail.
 */
public class PersistenceException extends RuntimeException {

    /**
     * Creates a new persistence exception.
     *
    * @param message explanation of the failure
     * @param cause   root cause
     */
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
