package scrms.exceptions;

/**
 * Exception thrown when a booking collides with an existing reservation or timetable entry.
 */
public class BookingConflictException extends RuntimeException {

    /**
     * Creates a new conflict exception.
     *
     * @param message explanation of the conflict
     */
    public BookingConflictException(String message) {
        super(message);
    }
}
