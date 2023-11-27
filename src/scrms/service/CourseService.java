package scrms.service;

import scrms.data.DataStore;
import scrms.exceptions.ResourceNotFoundException;
import scrms.model.Course;
import scrms.model.Instructor;
import scrms.model.Room;
import scrms.model.Student;
import scrms.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles course lifecycle and enrollment operations.
 */
public class CourseService implements CrudService<Course> {

    private final DataStore<Course> dataStore;
    private final List<Course> courses;
    private final InstructorService instructorService;
    private final RoomService roomService;
    private final StudentService studentService;

    public CourseService(InstructorService instructorService, RoomService roomService, StudentService studentService) {
        this.dataStore = new DataStore<>("courses.json", Course::fromJSON, Course::toJSON);
        this.courses = new ArrayList<>(dataStore.load());
        this.instructorService = instructorService;
        this.roomService = roomService;
        this.studentService = studentService;
    }

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses);
    }

    @Override
    public Course findById(String id) {
        return courses.stream().filter(c -> c.getCourseId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Course create(Course course) {
        ValidationUtils.requireText(course.getTitle(), "Course title is required");
        ValidationUtils.requirePositiveNumber(course.getCredits(), "Credits must be positive");
        if (course.getInstructorId() != null && instructorService.findById(course.getInstructorId()) == null) {
            throw new ResourceNotFoundException("Instructor not found: " + course.getInstructorId());
        }
        if (course.getRoomId() != null && roomService.findById(course.getRoomId()) == null) {
            throw new ResourceNotFoundException("Room not found: " + course.getRoomId());
        }
        courses.add(course);
        if (course.getInstructorId() != null) {
            instructorService.assignCourse(course.getInstructorId(), course.getCourseId());
        }
        persist();
        return course;
    }

    public Course create(String title, int credits, String instructorId, String roomId) {
        Course course = Course.create(title, credits, instructorId, roomId);
        return create(course);
    }

    @Override
    public Course update(Course course) {
        Course existing = findById(course.getCourseId());
        if (existing == null) {
            throw new ResourceNotFoundException("Course not found: " + course.getCourseId());
        }
        existing.setTitle(course.getTitle());
        existing.setCredits(course.getCredits());
        if (course.getInstructorId() != null && instructorService.findById(course.getInstructorId()) == null) {
            throw new ResourceNotFoundException("Instructor not found: " + course.getInstructorId());
        }
        if (existing.getInstructorId() != null && !existing.getInstructorId().equals(course.getInstructorId())) {
            instructorService.unassignCourse(existing.getInstructorId(), existing.getCourseId());
        }
        if (course.getInstructorId() != null && !course.getInstructorId().equals(existing.getInstructorId())) {
            instructorService.assignCourse(course.getInstructorId(), existing.getCourseId());
        }
        existing.setInstructorId(course.getInstructorId());
        if (course.getRoomId() != null && roomService.findById(course.getRoomId()) == null) {
            throw new ResourceNotFoundException("Room not found: " + course.getRoomId());
        }
        existing.setRoomId(course.getRoomId());
        persist();
        return existing;
    }

    @Override
    public void delete(String id) {
        Course existing = findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Course not found: " + id);
        }
        if (existing.getInstructorId() != null) {
            instructorService.unassignCourse(existing.getInstructorId(), existing.getCourseId());
        }
        for (String studentId : existing.getEnrolledStudentIds()) {
            studentService.dropFromCourse(studentId, existing.getCourseId());
        }
        courses.remove(existing);
        persist();
    }

    public void enrollStudent(String courseId, String studentId) {
        Course course = findById(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found: " + courseId);
        }
        Student student = studentService.findById(studentId);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found: " + studentId);
        }
        course.enrollStudent(studentId);
        studentService.enrollInCourse(studentId, courseId);
        persist();
    }

    public void dropStudent(String courseId, String studentId) {
        Course course = findById(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found: " + courseId);
        }
        course.dropStudent(studentId);
        studentService.dropFromCourse(studentId, courseId);
        persist();
    }

    public void reload() {
        courses.clear();
        courses.addAll(dataStore.load());
    }

    public void flush() {
        persist();
    }

    private void persist() {
        dataStore.save(courses);
    }
}
