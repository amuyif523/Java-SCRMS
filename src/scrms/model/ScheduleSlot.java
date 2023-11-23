package scrms.model;

import scrms.utils.IdGenerator;
import scrms.utils.JsonUtils;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;

/**
 * Represents a scheduled block for a course.
 */
public class ScheduleSlot {

    private final String slotId;
    private String courseId;
    private String roomId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public ScheduleSlot(String slotId, String courseId, String roomId,
                        DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.slotId = slotId;
        this.courseId = courseId;
        this.roomId = roomId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static ScheduleSlot create(String courseId, String roomId,
                                      DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        return new ScheduleSlot(IdGenerator.newId("SLOT"), courseId, roomId, dayOfWeek, startTime, endTime);
    }

    public String getSlotId() {
        return slotId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    public String toJSON() {
        return "{" + "\"slotId\":" + JsonUtils.quote(slotId) + ","
                + "\"courseId\":" + JsonUtils.quote(courseId) + ","
                + "\"roomId\":" + JsonUtils.quote(roomId) + ","
                + "\"dayOfWeek\":" + JsonUtils.quote(dayOfWeek.name()) + ","
                + "\"startTime\":" + JsonUtils.quote(startTime.toString()) + ","
                + "\"endTime\":" + JsonUtils.quote(endTime.toString())
                + "}";
    }

    public static ScheduleSlot fromJSON(String json) {
        Map<String, String> values = JsonUtils.parseJsonObject(json);
        String slotId = JsonUtils.unquote(values.get("slotId"));
        String courseId = JsonUtils.unquote(values.get("courseId"));
        String roomId = JsonUtils.unquote(values.get("roomId"));
        DayOfWeek day = DayOfWeek.valueOf(JsonUtils.unquote(values.get("dayOfWeek")));
        LocalTime start = LocalTime.parse(JsonUtils.unquote(values.get("startTime")));
        LocalTime end = LocalTime.parse(JsonUtils.unquote(values.get("endTime")));
        return new ScheduleSlot(slotId, courseId, roomId, day, start, end);
    }

    @Override
    public String toString() {
        return "ScheduleSlot{" +
                "courseId='" + courseId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
