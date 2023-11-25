package scrms.controller;

import scrms.exceptions.BookingConflictException;
import scrms.exceptions.ResourceNotFoundException;
import scrms.exceptions.ValidationException;
import scrms.model.*;
import scrms.service.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

/**
 * Simple console controller that interacts with every service to provide a menu driven experience.
 */
public class ConsoleController {

    private final ServiceRegistry services;
    private final Scanner scanner = new Scanner(System.in);
    private Admin authenticatedAdmin;

    public ConsoleController(ServiceRegistry services) {
        this.services = services;
    }

    /**
     * Entry point for the console experience.
     */
    public void start() {
        System.out.println("=== Smart Campus Resource Management System (SCRMS) ===");
        authenticate();
        showMainMenu();
    }

    private void authenticate() {
        AuthenticationService authService = services.getAuthenticationService();
        while (authenticatedAdmin == null) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            try {
                authenticatedAdmin = authService.login(username, password);
                System.out.println("Welcome, " + authenticatedAdmin.getFullName() + "!");
            } catch (Exception e) {
                System.out.println("Login failed: " + e.getMessage());
            }
        }
    }

    private void showMainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nMain Menu");
            System.out.println("1. Manage Students");
            System.out.println("2. Manage Instructors");
            System.out.println("3. Manage Courses");
            System.out.println("4. Manage Rooms");
            System.out.println("5. Manage Timetables");
            System.out.println("6. Book Rooms");
            System.out.println("7. Attendance");
            System.out.println("8. Grade Reports");
            System.out.println("9. Save & Load Data");
            System.out.println("0. Exit");
            String choice = prompt("Select an option: ");
            switch (choice) {
                case "1" -> manageStudents();
                case "2" -> manageInstructors();
                case "3" -> manageCourses();
                case "4" -> manageRooms();
                case "5" -> manageTimetables();
                case "6" -> manageBookings();
                case "7" -> manageAttendance();
                case "8" -> manageGrades();
                case "9" -> manageData();
                case "0" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
        System.out.println("Goodbye!");
    }

    private void manageStudents() {
        StudentService service = services.getStudentService();
        boolean back = false;
        while (!back) {
            System.out.println("\nStudents Menu");
            System.out.println("1. List students");
            System.out.println("2. Create student");
            System.out.println("3. Update student");
            System.out.println("4. Delete student");
            System.out.println("5. Enroll student in course");
            System.out.println("0. Back");
            String choice = prompt("Select: ");
            try {
                switch (choice) {
                    case "1" -> listStudents(service);
                    case "2" -> createStudent(service);
                    case "3" -> updateStudent(service);
                    case "4" -> deleteStudent(service);
                    case "5" -> enrollStudent(service);
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (ValidationException | ResourceNotFoundException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void listStudents(StudentService service) {
        List<Student> students = service.findAll();
        System.out.println("Students (" + students.size() + ")");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    private void createStudent(StudentService service) {
        String name = prompt("Full name: ");
        String dept = prompt("Department: ");
        String email = prompt("Email: ");
        Student student = service.create(name, dept, email);
        System.out.println("Created " + student.getStudentId());
    }

    private void updateStudent(StudentService service) {
        String id = prompt("Student ID: ");
        Student existing = service.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Student not found: " + id);
        }
        String name = promptOptional("Full name", existing.getFullName());
        String dept = promptOptional("Department", existing.getDepartment());
        String email = promptOptional("Email", existing.getEmail());
        existing.setFullName(name);
        existing.setDepartment(dept);
        existing.setEmail(email);
        service.update(existing);
        System.out.println("Updated student.");
    }

    private void deleteStudent(StudentService service) {
        String id = prompt("Student ID to delete: ");
        service.delete(id);
        System.out.println("Removed student.");
    }

    private void enrollStudent(StudentService service) {
        String studentId = prompt("Student ID: ");
        String courseId = prompt("Course ID: ");
        services.getCourseService().enrollStudent(courseId, studentId);
        System.out.println("Enrollment completed.");
    }

    private void manageInstructors() {
        InstructorService service = services.getInstructorService();
        boolean back = false;
        while (!back) {
            System.out.println("\nInstructors Menu");
            System.out.println("1. List instructors");
            System.out.println("2. Create instructor");
            System.out.println("3. Update instructor");
            System.out.println("4. Delete instructor");
            System.out.println("0. Back");
            String choice = prompt("Select: ");
            try {
                switch (choice) {
                    case "1" -> listInstructors(service);
                    case "2" -> createInstructor(service);
                    case "3" -> updateInstructor(service);
                    case "4" -> deleteInstructor(service);
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (ValidationException | ResourceNotFoundException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void listInstructors(InstructorService service) {
        List<Instructor> instructors = service.findAll();
        System.out.println("Instructors (" + instructors.size() + ")");
        for (Instructor instructor : instructors) {
            System.out.println(instructor);
        }
    }

    private void createInstructor(InstructorService service) {
        String name = prompt("Full name: ");
        String dept = prompt("Department: ");
        String email = prompt("Email: ");
        Instructor instructor = service.create(name, dept, email);
        System.out.println("Created " + instructor.getInstructorId());
    }

    private void updateInstructor(InstructorService service) {
        String id = prompt("Instructor ID: ");
        Instructor instructor = service.findById(id);
        if (instructor == null) {
            throw new ResourceNotFoundException("Instructor not found: " + id);
        }
        String name = promptOptional("Full name", instructor.getFullName());
        String dept = promptOptional("Department", instructor.getDepartment());
        String email = promptOptional("Email", instructor.getEmail());
        instructor.setFullName(name);
        instructor.setDepartment(dept);
        instructor.setEmail(email);
        service.update(instructor);
        System.out.println("Updated instructor.");
    }

    private void deleteInstructor(InstructorService service) {
        String id = prompt("Instructor ID to delete: ");
        service.delete(id);
        System.out.println("Instructor deleted.");
    }

    private void manageCourses() {
        CourseService service = services.getCourseService();
        boolean back = false;
        while (!back) {
            System.out.println("\nCourses Menu");
            System.out.println("1. List courses");
            System.out.println("2. Create course");
            System.out.println("3. Update course");
            System.out.println("4. Delete course");
            System.out.println("5. Enroll student");
            System.out.println("0. Back");
            String choice = prompt("Select: ");
            try {
                switch (choice) {
                    case "1" -> listCourses(service);
                    case "2" -> createCourse(service);
                    case "3" -> updateCourse(service);
                    case "4" -> deleteCourse(service);
                    case "5" -> enrollStudentInCourse(service);
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void listCourses(CourseService service) {
        List<Course> courses = service.findAll();
        System.out.println("Courses (" + courses.size() + ")");
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    private void createCourse(CourseService service) {
        String title = prompt("Course title: ");
        int credits = promptInt("Credits: ");
        String instructorId = prompt("Instructor ID (optional, blank to skip): ");
        if (instructorId.isBlank()) {
            instructorId = null;
        }
        String roomId = prompt("Room ID (optional, blank to skip): ");
        if (roomId.isBlank()) {
            roomId = null;
        }
        Course course = service.create(title, credits, instructorId, roomId);
        System.out.println("Created course " + course.getCourseId());
    }

    private void updateCourse(CourseService service) {
        String courseId = prompt("Course ID: ");
        Course course = service.findById(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found: " + courseId);
        }
        String title = promptOptional("Title", course.getTitle());
        int credits = promptInt("Credits (" + course.getCredits() + "): ");
        String instructor = promptOptional("Instructor ID", course.getInstructorId());
        String roomId = promptOptional("Room ID", course.getRoomId());
        course.setTitle(title);
        course.setCredits(credits);
        course.setInstructorId(instructor);
        course.setRoomId(roomId);
        service.update(course);
        System.out.println("Course updated.");
    }

    private void deleteCourse(CourseService service) {
        String id = prompt("Course ID to delete: ");
        service.delete(id);
        System.out.println("Course removed.");
    }

    private void enrollStudentInCourse(CourseService service) {
        String courseId = prompt("Course ID: ");
        String studentId = prompt("Student ID: ");
        service.enrollStudent(courseId, studentId);
        System.out.println("Student enrolled.");
    }

    private void manageRooms() {
        RoomService service = services.getRoomService();
        boolean back = false;
        while (!back) {
            System.out.println("\nRooms Menu");
            System.out.println("1. List rooms");
            System.out.println("2. Create room");
            System.out.println("3. Update room");
            System.out.println("4. Delete room");
            System.out.println("0. Back");
            String choice = prompt("Select: ");
            try {
                switch (choice) {
                    case "1" -> listRooms(service);
                    case "2" -> createRoom(service);
                    case "3" -> updateRoom(service);
                    case "4" -> deleteRoom(service);
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void listRooms(RoomService service) {
        List<Room> rooms = service.findAll();
        System.out.println("Rooms (" + rooms.size() + ")");
        for (Room room : rooms) {
            System.out.println(room);
        }
    }

    private void createRoom(RoomService service) {
        String name = prompt("Room name: ");
        int capacity = promptInt("Capacity: ");
        RoomType type = promptRoomType();
        Room room = service.create(name, capacity, type);
        System.out.println("Created room " + room.getRoomId());
    }

    private void updateRoom(RoomService service) {
        String roomId = prompt("Room ID: ");
        Room room = service.findById(roomId);
        if (room == null) {
            throw new ResourceNotFoundException("Room not found: " + roomId);
        }
        String name = promptOptional("Name", room.getName());
        int capacity = promptInt("Capacity (" + room.getCapacity() + "): ");
        RoomType type = promptRoomType();
        room.setName(name);
        room.setCapacity(capacity);
        room.setType(type);
        service.update(room);
        System.out.println("Room updated.");
    }

    private void deleteRoom(RoomService service) {
        String id = prompt("Room ID to delete: ");
        service.delete(id);
        System.out.println("Room deleted.");
    }

    private void manageTimetables() {
        TimetableService service = services.getTimetableService();
        boolean back = false;
        while (!back) {
            System.out.println("\nTimetable Menu");
            System.out.println("1. List slots");
            System.out.println("2. Generate automatic timetable");
            System.out.println("3. Create custom slot");
            System.out.println("4. Delete slot");
            System.out.println("0. Back");
            String choice = prompt("Select: ");
            try {
                switch (choice) {
                    case "1" -> listSlots(service);
                    case "2" -> generateTimetable(service);
                    case "3" -> createSlot(service);
                    case "4" -> deleteSlot(service);
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void listSlots(TimetableService service) {
        List<ScheduleSlot> slots = service.findAll();
        System.out.println("Schedule Slots (" + slots.size() + ")");
        for (ScheduleSlot slot : slots) {
            System.out.println(slot);
        }
    }

    private void generateTimetable(TimetableService service) {
        List<ScheduleSlot> slots = service.generateAutomaticTimetable();
        System.out.println("Generated timetable entries: " + slots.size());
    }

    private void createSlot(TimetableService service) {
        String courseId = prompt("Course ID: ");
        String roomId = prompt("Room ID: ");
        DayOfWeek day = promptDayOfWeek();
        LocalTime start = promptTime("Start time (HH:MM): ");
        LocalTime end = promptTime("End time (HH:MM): ");
        service.scheduleCourse(courseId, roomId, day, start, end);
        System.out.println("Slot created.");
    }

    private void deleteSlot(TimetableService service) {
        String slotId = prompt("Slot ID to delete: ");
        service.deleteSlot(slotId);
        System.out.println("Slot deleted.");
    }

    private void manageBookings() {
        BookingService service = services.getBookingService();
        boolean back = false;
        while (!back) {
            System.out.println("\nBooking Menu");
            System.out.println("1. List bookings");
            System.out.println("2. Request booking");
            System.out.println("3. Approve booking");
            System.out.println("4. Reject booking");
            System.out.println("5. Cancel booking");
            System.out.println("0. Back");
            String choice = prompt("Select: ");
            try {
                switch (choice) {
                    case "1" -> listBookings(service);
                    case "2" -> requestBooking(service);
                    case "3" -> approveBooking(service);
                    case "4" -> rejectBooking(service);
                    case "5" -> cancelBooking(service);
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (BookingConflictException | ResourceNotFoundException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void listBookings(BookingService service) {
        List<RoomBooking> bookings = service.findAll();
        System.out.println("Bookings (" + bookings.size() + ")");
        for (RoomBooking booking : bookings) {
            System.out.println(booking.toJSON());
        }
    }

    private void requestBooking(BookingService service) {
        String roomId = prompt("Room ID: ");
        String requester = prompt("Requester: ");
        String purpose = prompt("Purpose: ");
        LocalDate date = promptDate("Date (YYYY-MM-DD): ");
        LocalTime start = promptTime("Start time (HH:MM): ");
        LocalTime end = promptTime("End time (HH:MM): ");
        RoomBooking booking = service.requestBooking(roomId, requester, purpose, date, start, end);
        System.out.println("Booking requested: " + booking.getBookingId());
    }

    private void approveBooking(BookingService service) {
        String bookingId = prompt("Booking ID to approve: ");
        service.approve(bookingId);
        System.out.println("Booking approved.");
    }

    private void rejectBooking(BookingService service) {
        String bookingId = prompt("Booking ID to reject: ");
        service.reject(bookingId);
        System.out.println("Booking rejected.");
    }

    private void cancelBooking(BookingService service) {
        String bookingId = prompt("Booking ID to cancel: ");
        service.cancel(bookingId);
        System.out.println("Booking canceled.");
    }

    private void manageAttendance() {
        AttendanceService service = services.getAttendanceService();
        boolean back = false;
        while (!back) {
            System.out.println("\nAttendance Menu");
            System.out.println("1. List records");
            System.out.println("2. Mark attendance");
            System.out.println("3. Delete record");
            System.out.println("0. Back");
            String choice = prompt("Select: ");
            try {
                switch (choice) {
                    case "1" -> listAttendance(service);
                    case "2" -> markAttendance(service);
                    case "3" -> deleteAttendance(service);
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void listAttendance(AttendanceService service) {
        List<AttendanceRecord> records = service.findAll();
        System.out.println("Attendance Records (" + records.size() + ")");
        for (AttendanceRecord record : records) {
            System.out.println(record.toJSON());
        }
    }

    private void markAttendance(AttendanceService service) {
        String studentId = prompt("Student ID: ");
        String courseId = prompt("Course ID: ");
        LocalDate date = promptDate("Date (YYYY-MM-DD): ");
        boolean present = prompt("Present (y/n): ").equalsIgnoreCase("y");
        AttendanceRecord record = service.markAttendance(studentId, courseId, date, present);
        System.out.println("Attendance recorded: " + record.getRecordId());
    }

    private void deleteAttendance(AttendanceService service) {
        String recordId = prompt("Record ID to delete: ");
        service.delete(recordId);
        System.out.println("Attendance record deleted.");
    }

    private void manageGrades() {
        GradeService service = services.getGradeService();
        boolean back = false;
        while (!back) {
            System.out.println("\nGrades Menu");
            System.out.println("1. List reports");
            System.out.println("2. Record grade");
            System.out.println("3. Delete report");
            System.out.println("0. Back");
            String choice = prompt("Select: ");
            try {
                switch (choice) {
                    case "1" -> listGrades(service);
                    case "2" -> recordGrade(service);
                    case "3" -> deleteGrade(service);
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void listGrades(GradeService service) {
        List<GradeReport> reports = service.findAll();
        System.out.println("Grade Reports (" + reports.size() + ")");
        for (GradeReport report : reports) {
            System.out.println(report.toJSON());
        }
    }

    private void recordGrade(GradeService service) {
        String studentId = prompt("Student ID: ");
        String courseId = prompt("Course ID: ");
        double score = promptDouble("Score: ");
        String remarks = prompt("Remarks: ");
        GradeReport report = service.recordGrade(studentId, courseId, score, remarks);
        System.out.println("Grade recorded: " + report.getReportId());
    }

    private void deleteGrade(GradeService service) {
        String reportId = prompt("Report ID to delete: ");
        service.delete(reportId);
        System.out.println("Report deleted.");
    }

    private void manageData() {
        boolean back = false;
        while (!back) {
            System.out.println("\nData Menu");
            System.out.println("1. Save all data");
            System.out.println("2. Reload data from disk");
            System.out.println("0. Back");
            String choice = prompt("Select: ");
            switch (choice) {
                case "1" -> {
                    services.saveAll();
                    System.out.println("Data saved to JSON files.");
                }
                case "2" -> {
                    services.reloadAll();
                    System.out.println("Data reloaded from JSON files.");
                }
                case "0" -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    private String promptOptional(String field, String current) {
        System.out.print(field + " (" + current + "): ");
        String value = scanner.nextLine();
        if (value.isBlank()) {
            return current;
        }
        return value;
    }

    private int promptInt(String message) {
        while (true) {
            try {
                return Integer.parseInt(prompt(message));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private double promptDouble(String message) {
        while (true) {
            try {
                return Double.parseDouble(prompt(message));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private LocalDate promptDate(String message) {
        while (true) {
            try {
                return LocalDate.parse(prompt(message));
            } catch (Exception e) {
                System.out.println("Enter date in YYYY-MM-DD format.");
            }
        }
    }

    private LocalTime promptTime(String message) {
        while (true) {
            try {
                return LocalTime.parse(prompt(message));
            } catch (Exception e) {
                System.out.println("Enter time in HH:MM format.");
            }
        }
    }

    private DayOfWeek promptDayOfWeek() {
        while (true) {
            String text = prompt("Day of week (e.g., MONDAY): ").toUpperCase();
            try {
                return DayOfWeek.valueOf(text);
            } catch (Exception e) {
                System.out.println("Invalid day.");
            }
        }
    }

    private RoomType promptRoomType() {
        while (true) {
            String text = prompt("Room type (LECTURE/LAB/AUDITORIUM/MEETING): ").toUpperCase();
            try {
                return RoomType.valueOf(text);
            } catch (Exception e) {
                System.out.println("Invalid room type.");
            }
        }
    }
}
