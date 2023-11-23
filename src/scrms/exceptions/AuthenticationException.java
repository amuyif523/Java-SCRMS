package scrms.exceptions;

/**
 * Exception thrown when login credentials are invalid.
 */
public class AuthenticationException extends RuntimeException {

    /**
     * Creates a new instance with the provided message.
     *
     * @param message reason for authentication failure
     */
    public AuthenticationException(String message) {
        super(message);
    }
}
