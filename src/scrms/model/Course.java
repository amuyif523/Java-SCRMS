package scrms.model;

import scrms.utils.IdGenerator;
import scrms.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a course offered on campus.
 */
public class Course {

    private final String courseId;
    private String title;
    private int credits;
    private String instructorId;
    private String roomId;
    private final List<String> enrolledStudentIds;

    public Course(String courseId, String title, int credits, String instructorId,
                  String roomId, List<String> enrolledStudentIds) {
        this.courseId = courseId;
        this.title = title;
        this.credits = credits;
        this.instructorId = instructorId;
        this.roomId = roomId;
        this.enrolledStudentIds = enrolledStudentIds != null ? new ArrayList<>(enrolledStudentIds) : new ArrayList<>();
    }

    public static Course create(String title, int credits, String instructorId, String roomId) {
        return new Course(IdGenerator.newId("CRS"), title, credits, instructorId, roomId, new ArrayList<>());
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<String> getEnrolledStudentIds() {
        return new ArrayList<>(enrolledStudentIds);
    }

    public void enrollStudent(String studentId) {
        if (!enrolledStudentIds.contains(studentId)) {
            enrolledStudentIds.add(studentId);
        }
    }

    public void dropStudent(String studentId) {
        enrolledStudentIds.remove(studentId);
    }

    public String toJSON() {
        return "{" + "\"courseId\":" + JsonUtils.quote(courseId) + ","
                + "\"title\":" + JsonUtils.quote(title) + ","
                + "\"credits\":" + credits + ","
                + "\"instructorId\":" + JsonUtils.quote(instructorId) + ","
                + "\"roomId\":" + JsonUtils.quote(roomId) + ","
                + "\"enrolledStudentIds\":" + JsonUtils.toStringArray(enrolledStudentIds)
                + "}";
    }

    public static Course fromJSON(String json) {
        Map<String, String> values = JsonUtils.parseJsonObject(json);
        String id = JsonUtils.unquote(values.get("courseId"));
        String title = JsonUtils.unquote(values.get("title"));
        int credits = Integer.parseInt(values.get("credits"));
        String instructor = JsonUtils.unquote(values.get("instructorId"));
        String room = JsonUtils.unquote(values.get("roomId"));
        List<String> students = JsonUtils.parseStringArray(values.get("enrolledStudentIds"));
        return new Course(id, title, credits, instructor, room, students);
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", title='" + title + '\'' +
                ", credits=" + credits +
                ", instructorId='" + instructorId + '\'' +
                ", roomId='" + roomId + '\'' +
                '}';
    }
}
