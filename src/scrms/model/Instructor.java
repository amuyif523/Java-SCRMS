package scrms.model;

import scrms.utils.IdGenerator;
import scrms.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents instructors that can teach courses.
 */
public class Instructor {

    private final String instructorId;
    private String fullName;
    private String department;
    private String email;
    private final List<String> courseIds;

    public Instructor(String instructorId, String fullName, String department, String email, List<String> courseIds) {
        this.instructorId = instructorId;
        this.fullName = fullName;
        this.department = department;
        this.email = email;
        this.courseIds = courseIds != null ? new ArrayList<>(courseIds) : new ArrayList<>();
    }

    public static Instructor create(String fullName, String department, String email) {
        return new Instructor(IdGenerator.newId("INS"), fullName, department, email, new ArrayList<>());
    }

    public String getInstructorId() {
        return instructorId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getCourseIds() {
        return new ArrayList<>(courseIds);
    }

    public void assignCourse(String courseId) {
        if (!courseIds.contains(courseId)) {
            courseIds.add(courseId);
        }
    }

    public void unassignCourse(String courseId) {
        courseIds.remove(courseId);
    }

    public String toJSON() {
        return "{" + "\"instructorId\":" + JsonUtils.quote(instructorId) + ","
                + "\"fullName\":" + JsonUtils.quote(fullName) + ","
                + "\"department\":" + JsonUtils.quote(department) + ","
                + "\"email\":" + JsonUtils.quote(email) + ","
                + "\"courseIds\":" + JsonUtils.toStringArray(courseIds) + "}";
    }

    public static Instructor fromJSON(String json) {
        Map<String, String> values = JsonUtils.parseJsonObject(json);
        String id = JsonUtils.unquote(values.get("instructorId"));
        String fullName = JsonUtils.unquote(values.get("fullName"));
        String dept = JsonUtils.unquote(values.get("department"));
        String email = JsonUtils.unquote(values.get("email"));
        List<String> courses = JsonUtils.parseStringArray(values.get("courseIds"));
        return new Instructor(id, fullName, dept, email, courses);
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "instructorId='" + instructorId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", department='" + department + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
