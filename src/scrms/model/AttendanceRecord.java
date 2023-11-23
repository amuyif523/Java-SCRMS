package scrms.model;

import scrms.utils.IdGenerator;
import scrms.utils.JsonUtils;

import java.time.LocalDate;
import java.util.Map;

/**
 * Represents an attendance entry for a student in a course.
 */
public class AttendanceRecord {

    private final String recordId;
    private String studentId;
    private String courseId;
    private LocalDate date;
    private boolean present;

    public AttendanceRecord(String recordId, String studentId, String courseId, LocalDate date, boolean present) {
        this.recordId = recordId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.date = date;
        this.present = present;
    }

    public static AttendanceRecord create(String studentId, String courseId, LocalDate date, boolean present) {
        return new AttendanceRecord(IdGenerator.newId("ATT"), studentId, courseId, date, present);
    }

    public String getRecordId() {
        return recordId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String toJSON() {
        return "{" + "\"recordId\":" + JsonUtils.quote(recordId) + ","
                + "\"studentId\":" + JsonUtils.quote(studentId) + ","
                + "\"courseId\":" + JsonUtils.quote(courseId) + ","
                + "\"date\":" + JsonUtils.quote(date.toString()) + ","
                + "\"present\":" + present + "}";
    }

    public static AttendanceRecord fromJSON(String json) {
        Map<String, String> map = JsonUtils.parseJsonObject(json);
        String id = JsonUtils.unquote(map.get("recordId"));
        String studentId = JsonUtils.unquote(map.get("studentId"));
        String courseId = JsonUtils.unquote(map.get("courseId"));
        LocalDate date = LocalDate.parse(JsonUtils.unquote(map.get("date")));
        boolean present = Boolean.parseBoolean(map.get("present"));
        return new AttendanceRecord(id, studentId, courseId, date, present);
    }
}
