package scrms.service;

import scrms.data.DataStore;
import scrms.exceptions.ResourceNotFoundException;
import scrms.model.Instructor;
import scrms.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles instructor related operations.
 */
public class InstructorService implements CrudService<Instructor> {

    private final DataStore<Instructor> dataStore;
    private final List<Instructor> instructors;

    public InstructorService() {
        this.dataStore = new DataStore<>("instructors.json", Instructor::fromJSON, Instructor::toJSON);
        this.instructors = new ArrayList<>(dataStore.load());
    }

    @Override
    public List<Instructor> findAll() {
        return new ArrayList<>(instructors);
    }

    @Override
    public Instructor findById(String id) {
        return instructors.stream().filter(i -> i.getInstructorId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Instructor create(Instructor instructor) {
        ValidationUtils.requireText(instructor.getFullName(), "Instructor name is required");
        ValidationUtils.requireEmail(instructor.getEmail());
        instructors.add(instructor);
        persist();
        return instructor;
    }

    public Instructor create(String name, String department, String email) {
        return create(Instructor.create(name, department, email));
    }

    @Override
    public Instructor update(Instructor instructor) {
        Instructor existing = findById(instructor.getInstructorId());
        if (existing == null) {
            throw new ResourceNotFoundException("Instructor not found: " + instructor.getInstructorId());
        }
        existing.setFullName(instructor.getFullName());
        existing.setDepartment(instructor.getDepartment());
        existing.setEmail(instructor.getEmail());
        persist();
        return existing;
    }

    @Override
    public void delete(String id) {
        Instructor existing = findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Instructor not found: " + id);
        }
        instructors.remove(existing);
        persist();
    }

    public void assignCourse(String instructorId, String courseId) {
        Instructor instructor = findById(instructorId);
        if (instructor == null) {
            throw new ResourceNotFoundException("Instructor not found: " + instructorId);
        }
        instructor.assignCourse(courseId);
        persist();
    }

    public void unassignCourse(String instructorId, String courseId) {
        Instructor instructor = findById(instructorId);
        if (instructor == null) {
            throw new ResourceNotFoundException("Instructor not found: " + instructorId);
        }
        instructor.unassignCourse(courseId);
        persist();
    }

    private void persist() {
        dataStore.save(instructors);
    }
}
