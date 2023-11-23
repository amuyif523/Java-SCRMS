package scrms.service;

import scrms.data.DataStore;
import scrms.exceptions.ResourceNotFoundException;
import scrms.model.Student;
import scrms.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for CRUD operations on students.
 */
public class StudentService implements CrudService<Student> {

    private final DataStore<Student> dataStore;
    private final List<Student> students;

    public StudentService() {
        this.dataStore = new DataStore<>("students.json", Student::fromJSON, Student::toJSON);
        this.students = new ArrayList<>(dataStore.load());
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students);
    }

    @Override
    public Student findById(String id) {
        return students.stream().filter(s -> s.getStudentId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Student create(Student student) {
        ValidationUtils.requireText(student.getFullName(), "Student name is required");
        ValidationUtils.requireText(student.getDepartment(), "Student department is required");
        ValidationUtils.requireEmail(student.getEmail());
        students.add(student);
        persist();
        return student;
    }

    /**
     * Convenience method that creates a student from field values.
     *
     * @param name       student name
     * @param department department
     * @param email      email
     * @return stored student
     */
    public Student create(String name, String department, String email) {
        Student student = Student.create(name, department, email);
        return create(student);
    }

    @Override
    public Student update(Student student) {
        Student existing = findById(student.getStudentId());
        if (existing == null) {
            throw new ResourceNotFoundException("Student not found: " + student.getStudentId());
        }
        existing.setFullName(student.getFullName());
        existing.setDepartment(student.getDepartment());
        existing.setEmail(student.getEmail());
        persist();
        return existing;
    }

    @Override
    public void delete(String id) {
        Student student = findById(id);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found: " + id);
        }
        students.remove(student);
        persist();
    }

    /**
     * Enrolls a student in a course.
     *
     * @param studentId student identifier
     * @param courseId  course identifier
     */
    public void enrollInCourse(String studentId, String courseId) {
        Student student = findById(studentId);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found: " + studentId);
        }
        student.enrollCourse(courseId);
        persist();
    }

    /**
     * Removes a student from a course.
     *
     * @param studentId student identifier
     * @param courseId  course identifier
     */
    public void dropFromCourse(String studentId, String courseId) {
        Student student = findById(studentId);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found: " + studentId);
        }
        student.dropCourse(courseId);
        persist();
    }

    private void persist() {
        dataStore.save(students);
    }
}
