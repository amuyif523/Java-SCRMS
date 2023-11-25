package scrms.service;

import scrms.data.DataStore;
import scrms.exceptions.ResourceNotFoundException;
import scrms.model.AttendanceRecord;
import scrms.model.Course;
import scrms.model.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages attendance records for every course.
 */
public class AttendanceService {

    private final DataStore<AttendanceRecord> dataStore;
    private final List<AttendanceRecord> records;
    private final StudentService studentService;
    private final CourseService courseService;

    public AttendanceService(StudentService studentService, CourseService courseService) {
        this.dataStore = new DataStore<>("attendance.json", AttendanceRecord::fromJSON, AttendanceRecord::toJSON);
        this.records = new ArrayList<>(dataStore.load());
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public List<AttendanceRecord> findAll() {
        return new ArrayList<>(records);
    }

    public List<AttendanceRecord> findByStudent(String studentId) {
        return records.stream()
                .filter(r -> r.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<AttendanceRecord> findByCourse(String courseId) {
        return records.stream()
                .filter(r -> r.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    public AttendanceRecord markAttendance(String studentId, String courseId, LocalDate date, boolean present) {
        Student student = studentService.findById(studentId);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found: " + studentId);
        }
        Course course = courseService.findById(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found: " + courseId);
        }
        AttendanceRecord record = AttendanceRecord.create(studentId, courseId, date, present);
        records.add(record);
        persist();
        return record;
    }

    public void delete(String recordId) {
        records.removeIf(record -> record.getRecordId().equals(recordId));
        persist();
    }

    private void persist() {
        dataStore.save(records);
    }
}
