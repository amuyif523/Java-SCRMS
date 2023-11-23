package scrms.exceptions;

/**
 * Exception thrown when a requested entity does not exist in the system.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a new exception for the missing resource.
     *
     * @param message information about the resource that was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
