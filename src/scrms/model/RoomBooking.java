package scrms.model;

import scrms.utils.IdGenerator;
import scrms.utils.JsonUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * Booking request for a room on a specific date and time window.
 */
public class RoomBooking {

    private final String bookingId;
    private String roomId;
    private String requester;
    private String purpose;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;

    public RoomBooking(String bookingId, String roomId, String requester, String purpose,
                       LocalDate date, LocalTime startTime, LocalTime endTime, BookingStatus status) {
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.requester = requester;
        this.purpose = purpose;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public static RoomBooking create(String roomId, String requester, String purpose,
                                     LocalDate date, LocalTime startTime, LocalTime endTime) {
        return new RoomBooking(IdGenerator.newId("RBK"), roomId, requester, purpose, date, startTime, endTime, BookingStatus.PENDING);
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String toJSON() {
        return "{" + "\"bookingId\":" + JsonUtils.quote(bookingId) + ","
                + "\"roomId\":" + JsonUtils.quote(roomId) + ","
                + "\"requester\":" + JsonUtils.quote(requester) + ","
                + "\"purpose\":" + JsonUtils.quote(purpose) + ","
                + "\"date\":" + JsonUtils.quote(date.toString()) + ","
                + "\"startTime\":" + JsonUtils.quote(startTime.toString()) + ","
                + "\"endTime\":" + JsonUtils.quote(endTime.toString()) + ","
                + "\"status\":" + JsonUtils.quote(status.name())
                + "}";
    }

    public static RoomBooking fromJSON(String json) {
        Map<String, String> values = JsonUtils.parseJsonObject(json);
        String id = JsonUtils.unquote(values.get("bookingId"));
        String roomId = JsonUtils.unquote(values.get("roomId"));
        String requester = JsonUtils.unquote(values.get("requester"));
        String purpose = JsonUtils.unquote(values.get("purpose"));
        LocalDate date = LocalDate.parse(JsonUtils.unquote(values.get("date")));
        LocalTime start = LocalTime.parse(JsonUtils.unquote(values.get("startTime")));
        LocalTime end = LocalTime.parse(JsonUtils.unquote(values.get("endTime")));
        BookingStatus status = BookingStatus.valueOf(JsonUtils.unquote(values.get("status")));
        return new RoomBooking(id, roomId, requester, purpose, date, start, end, status);
    }
}
