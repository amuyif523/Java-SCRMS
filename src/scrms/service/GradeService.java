package scrms.service;

import scrms.data.DataStore;
import scrms.exceptions.ResourceNotFoundException;
import scrms.model.Course;
import scrms.model.GradeReport;
import scrms.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates and stores grade reports for students.
 */
public class GradeService {

    private final DataStore<GradeReport> dataStore;
    private final List<GradeReport> reports;
    private final StudentService studentService;
    private final CourseService courseService;

    public GradeService(StudentService studentService, CourseService courseService) {
        this.dataStore = new DataStore<>("grades.json", GradeReport::fromJSON, GradeReport::toJSON);
        this.reports = new ArrayList<>(dataStore.load());
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public List<GradeReport> findAll() {
        return new ArrayList<>(reports);
    }

    public List<GradeReport> findByStudent(String studentId) {
        return reports.stream()
                .filter(report -> report.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<GradeReport> findByCourse(String courseId) {
        return reports.stream()
                .filter(report -> report.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    public GradeReport recordGrade(String studentId, String courseId, double score, String remarks) {
        Student student = studentService.findById(studentId);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found: " + studentId);
        }
        Course course = courseService.findById(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found: " + courseId);
        }
        String letter = calculateLetter(score);
        GradeReport report = GradeReport.create(studentId, courseId, score, letter, remarks);
        reports.add(report);
        persist();
        return report;
    }

    public void delete(String reportId) {
        reports.removeIf(report -> report.getReportId().equals(reportId));
        persist();
    }

    private String calculateLetter(double score) {
        if (score >= 90) {
            return "A";
        } else if (score >= 80) {
            return "B";
        } else if (score >= 70) {
            return "C";
        } else if (score >= 60) {
            return "D";
        }
        return "F";
    }

    private void persist() {
        dataStore.save(reports);
    }
}
