package scrms.model;

import scrms.utils.IdGenerator;
import scrms.utils.JsonUtils;

import java.util.Map;

/**
 * Represents the grading outcome for a student inside a course.
 */
public class GradeReport {

    private final String reportId;
    private String studentId;
    private String courseId;
    private double score;
    private String letterGrade;
    private String remarks;

    public GradeReport(String reportId, String studentId, String courseId,
                       double score, String letterGrade, String remarks) {
        this.reportId = reportId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.score = score;
        this.letterGrade = letterGrade;
        this.remarks = remarks;
    }

    public static GradeReport create(String studentId, String courseId, double score, String letterGrade, String remarks) {
        return new GradeReport(IdGenerator.newId("GRD"), studentId, courseId, score, letterGrade, remarks);
    }

    public String getReportId() {
        return reportId;
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String toJSON() {
        return "{" + "\"reportId\":" + JsonUtils.quote(reportId) + ","
                + "\"studentId\":" + JsonUtils.quote(studentId) + ","
                + "\"courseId\":" + JsonUtils.quote(courseId) + ","
                + "\"score\":" + score + ","
                + "\"letterGrade\":" + JsonUtils.quote(letterGrade) + ","
                + "\"remarks\":" + JsonUtils.quote(remarks) + "}";
    }

    public static GradeReport fromJSON(String json) {
        Map<String, String> values = JsonUtils.parseJsonObject(json);
        String id = JsonUtils.unquote(values.get("reportId"));
        String studentId = JsonUtils.unquote(values.get("studentId"));
        String courseId = JsonUtils.unquote(values.get("courseId"));
        double score = Double.parseDouble(values.get("score"));
        String letter = JsonUtils.unquote(values.get("letterGrade"));
        String remarks = JsonUtils.unquote(values.get("remarks"));
        return new GradeReport(id, studentId, courseId, score, letter, remarks);
    }
}
