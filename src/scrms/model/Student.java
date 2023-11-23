package scrms.model;

import scrms.utils.IdGenerator;
import scrms.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a student registered in the campus.
 */
public class Student {

    private final String studentId;
    private String fullName;
    private String department;
    private String email;
    private final List<String> enrolledCourseIds;

    public Student(String studentId, String fullName, String department, String email, List<String> enrolledCourseIds) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.department = department;
        this.email = email;
        this.enrolledCourseIds = enrolledCourseIds != null ? new ArrayList<>(enrolledCourseIds) : new ArrayList<>();
    }

    public static Student create(String fullName, String department, String email) {
        return new Student(IdGenerator.newId("STD"), fullName, department, email, new ArrayList<>());
    }

    public String getStudentId() {
        return studentId;
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

    public List<String> getEnrolledCourseIds() {
        return new ArrayList<>(enrolledCourseIds);
    }

    public void enrollCourse(String courseId) {
        if (!enrolledCourseIds.contains(courseId)) {
            enrolledCourseIds.add(courseId);
        }
    }

    public void dropCourse(String courseId) {
        enrolledCourseIds.remove(courseId);
    }

    /**
     * Serializes the student into JSON.
     *
     * @return JSON object string
     */
    public String toJSON() {
        return "{" + "\"studentId\":" + JsonUtils.quote(studentId) + ","
                + "\"fullName\":" + JsonUtils.quote(fullName) + ","
                + "\"department\":" + JsonUtils.quote(department) + ","
                + "\"email\":" + JsonUtils.quote(email) + ","
                + "\"enrolledCourseIds\":" + JsonUtils.toStringArray(enrolledCourseIds)
                + "}";
    }

    /**
     * Creates a student from JSON.
     *
     * @param json JSON object
     * @return student instance
     */
    public static Student fromJSON(String json) {
        Map<String, String> map = JsonUtils.parseJsonObject(json);
        String id = JsonUtils.unquote(map.get("studentId"));
        String fullName = JsonUtils.unquote(map.get("fullName"));
        String department = JsonUtils.unquote(map.get("department"));
        String email = JsonUtils.unquote(map.get("email"));
        List<String> courses = JsonUtils.parseStringArray(map.get("enrolledCourseIds"));
        return new Student(id, fullName, department, email, courses);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", department='" + department + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
