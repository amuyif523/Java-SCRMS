package scrms.service;

import scrms.data.DataStore;
import scrms.exceptions.ResourceNotFoundException;
import scrms.model.Course;
import scrms.model.Room;
import scrms.model.ScheduleSlot;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles timetable generation and conflict detection.
 */
public class TimetableService {

    private final DataStore<ScheduleSlot> dataStore;
    private final List<ScheduleSlot> slots;
    private final CourseService courseService;
    private final RoomService roomService;

    public TimetableService(CourseService courseService, RoomService roomService) {
        this.dataStore = new DataStore<>("timetable.json", ScheduleSlot::fromJSON, ScheduleSlot::toJSON);
        this.slots = new ArrayList<>(dataStore.load());
        this.courseService = courseService;
        this.roomService = roomService;
    }

    /**
     * Generates a simple, conflict free timetable for all courses.
     *
     * @return list of schedule slots
     */
    public List<ScheduleSlot> generateAutomaticTimetable() {
        List<Course> courses = courseService.findAll();
        List<Room> rooms = roomService.findAll();
        if (rooms.isEmpty()) {
            return new ArrayList<>(slots);
        }
        LocalTime[] startTimes = {LocalTime.of(8, 0), LocalTime.of(10, 0),
                LocalTime.of(13, 0), LocalTime.of(15, 0)};
        DayOfWeek[] days = EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY).toArray(new DayOfWeek[0]);
        int pointer = 0;
        for (Course course : courses) {
            boolean alreadyScheduled = slots.stream().anyMatch(slot -> slot.getCourseId().equals(course.getCourseId()));
            if (alreadyScheduled) {
                continue;
            }
            Room preferredRoom = course.getRoomId() != null ? roomService.findById(course.getRoomId()) : null;
            Room room = preferredRoom != null ? preferredRoom : rooms.get(pointer % rooms.size());
            DayOfWeek day = days[pointer % days.length];
            LocalTime start = startTimes[pointer % startTimes.length];
            LocalTime end = start.plusHours(1).plusMinutes(45);
            pointer++;
            if (!hasConflict(room.getRoomId(), day, start, end)) {
                slots.add(ScheduleSlot.create(course.getCourseId(), room.getRoomId(), day, start, end));
            }
        }
        persist();
        return new ArrayList<>(slots);
    }

    /**
     * Creates a custom schedule slot for a course after validating conflicts.
     */
    public ScheduleSlot scheduleCourse(String courseId, String roomId, DayOfWeek day, LocalTime start, LocalTime end) {
        Course course = courseService.findById(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found: " + courseId);
        }
        Room room = roomService.findById(roomId);
        if (room == null) {
            throw new ResourceNotFoundException("Room not found: " + roomId);
        }
        if (hasConflict(roomId, day, start, end)) {
            throw new IllegalStateException("Time conflict detected for room " + room.getName());
        }
        ScheduleSlot slot = ScheduleSlot.create(courseId, roomId, day, start, end);
        slots.add(slot);
        persist();
        return slot;
    }

    /**
     * Checks if the provided booking conflicts with an existing slot.
     */
    public boolean hasConflict(String roomId, DayOfWeek day, LocalTime start, LocalTime end) {
        return slots.stream().anyMatch(slot ->
                slot.getRoomId().equals(roomId)
                        && slot.getDayOfWeek() == day
                        && timeOverlap(slot.getStartTime(), slot.getEndTime(), start, end));
    }

    /**
     * Checks if conflict occurs with specific date/time by converting date to DayOfWeek.
     */
    public boolean hasConflict(String roomId, DayOfWeek day, LocalTime start, LocalTime end, String ignoreSlotId) {
        return slots.stream().anyMatch(slot ->
                !slot.getSlotId().equals(ignoreSlotId)
                        && slot.getRoomId().equals(roomId)
                        && slot.getDayOfWeek() == day
                        && timeOverlap(slot.getStartTime(), slot.getEndTime(), start, end));
    }

    private boolean timeOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }

    /**
     * Returns slots for a specific course.
     */
    public List<ScheduleSlot> slotsForCourse(String courseId) {
        return slots.stream().filter(slot -> slot.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    public List<ScheduleSlot> findAll() {
        return new ArrayList<>(slots);
    }

    public void deleteSlot(String slotId) {
        slots.removeIf(slot -> slot.getSlotId().equals(slotId));
        persist();
    }

    public void reload() {
        slots.clear();
        slots.addAll(dataStore.load());
    }

    public void flush() {
        persist();
    }

    private void persist() {
        dataStore.save(slots);
    }
}
