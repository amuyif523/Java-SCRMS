package scrms.service;

/**
 * Centralized place that wires together all services.
 */
public class ServiceRegistry {

    private final StudentService studentService;
    private final InstructorService instructorService;
    private final RoomService roomService;
    private final CourseService courseService;
    private final TimetableService timetableService;
    private final BookingService bookingService;
    private final AttendanceService attendanceService;
    private final GradeService gradeService;
    private final AuthenticationService authenticationService;

    public ServiceRegistry() {
        this.studentService = new StudentService();
        this.instructorService = new InstructorService();
        this.roomService = new RoomService();
        this.courseService = new CourseService(instructorService, roomService, studentService);
        this.timetableService = new TimetableService(courseService, roomService);
        this.bookingService = new BookingService(roomService, timetableService);
        this.attendanceService = new AttendanceService(studentService, courseService);
        this.gradeService = new GradeService(studentService, courseService);
        this.authenticationService = new AuthenticationService();
    }

    public StudentService getStudentService() {
        return studentService;
    }

    public InstructorService getInstructorService() {
        return instructorService;
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public CourseService getCourseService() {
        return courseService;
    }

    public TimetableService getTimetableService() {
        return timetableService;
    }

    public BookingService getBookingService() {
        return bookingService;
    }

    public AttendanceService getAttendanceService() {
        return attendanceService;
    }

    public GradeService getGradeService() {
        return gradeService;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    /**
     * Flushes all services to disk.
     */
    public void saveAll() {
        studentService.flush();
        instructorService.flush();
        roomService.flush();
        courseService.flush();
        timetableService.flush();
        bookingService.flush();
        attendanceService.flush();
        gradeService.flush();
        authenticationService.flush();
    }

    /**
     * Reloads all services from disk.
     */
    public void reloadAll() {
        studentService.reload();
        instructorService.reload();
        roomService.reload();
        courseService.reload();
        timetableService.reload();
        bookingService.reload();
        attendanceService.reload();
        gradeService.reload();
        authenticationService.reload();
    }
}
