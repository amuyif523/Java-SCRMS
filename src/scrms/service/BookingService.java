package scrms.service;

import scrms.data.DataStore;
import scrms.exceptions.BookingConflictException;
import scrms.exceptions.ResourceNotFoundException;
import scrms.model.BookingStatus;
import scrms.model.Room;
import scrms.model.RoomBooking;
import scrms.utils.ValidationUtils;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles booking of rooms and ensures conflicts are detected.
 */
public class BookingService {

    private final DataStore<RoomBooking> dataStore;
    private final List<RoomBooking> bookings;
    private final RoomService roomService;
    private final TimetableService timetableService;

    public BookingService(RoomService roomService, TimetableService timetableService) {
        this.dataStore = new DataStore<>("bookings.json", RoomBooking::fromJSON, RoomBooking::toJSON);
        this.bookings = new ArrayList<>(dataStore.load());
        this.roomService = roomService;
        this.timetableService = timetableService;
    }

    public List<RoomBooking> findAll() {
        return new ArrayList<>(bookings);
    }

    public RoomBooking findById(String id) {
        return bookings.stream().filter(b -> b.getBookingId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Requests a new booking for approval.
     */
    public RoomBooking requestBooking(String roomId, String requester, String purpose,
                                      java.time.LocalDate date, java.time.LocalTime start,
                                      java.time.LocalTime end) {
        ValidationUtils.requireText(requester, "Requester name is required");
        Room room = roomService.findById(roomId);
        if (room == null) {
            throw new ResourceNotFoundException("Room not found: " + roomId);
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        RoomBooking booking = RoomBooking.create(roomId, requester, purpose, date, start, end);
        bookings.add(booking);
        persist();
        return booking;
    }

    /**
     * Approves a booking after checking for conflicts.
     */
    public void approve(String bookingId) {
        RoomBooking booking = findByIdOrThrow(bookingId);
        ensureNoConflicts(booking);
        booking.setStatus(BookingStatus.APPROVED);
        persist();
    }

    public void reject(String bookingId) {
        RoomBooking booking = findByIdOrThrow(bookingId);
        booking.setStatus(BookingStatus.REJECTED);
        persist();
    }

    public void cancel(String bookingId) {
        RoomBooking booking = findByIdOrThrow(bookingId);
        bookings.remove(booking);
        persist();
    }

    private void ensureNoConflicts(RoomBooking booking) {
        DayOfWeek day = booking.getDate().getDayOfWeek();
        if (timetableService.hasConflict(booking.getRoomId(), day, booking.getStartTime(), booking.getEndTime())) {
            throw new BookingConflictException("Booking conflicts with the timetable");
        }
        boolean overlaps = bookings.stream()
                .filter(b -> !b.getBookingId().equals(booking.getBookingId()))
                .filter(b -> b.getRoomId().equals(booking.getRoomId()))
                .filter(b -> b.getDate().equals(booking.getDate()))
                .filter(b -> b.getStatus() == BookingStatus.APPROVED || b.getStatus() == BookingStatus.PENDING)
                .anyMatch(b -> overlaps(b, booking));
        if (overlaps) {
            throw new BookingConflictException("Booking conflicts with another booking");
        }
    }

    private boolean overlaps(RoomBooking first, RoomBooking second) {
        return !first.getStartTime().isAfter(second.getEndTime())
                && !second.getStartTime().isAfter(first.getEndTime());
    }

    private RoomBooking findByIdOrThrow(String id) {
        RoomBooking booking = findById(id);
        if (booking == null) {
            throw new ResourceNotFoundException("Booking not found: " + id);
        }
        return booking;
    }

    private void persist() {
        dataStore.save(bookings);
    }
}
