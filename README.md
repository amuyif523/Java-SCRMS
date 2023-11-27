# Smart Campus Resource Management System (SCRMS)

SCRMS is a console-based Java application that helps campus administrators manage students, instructors, courses, rooms, timetables, bookings, attendance, and grades. The system persists every entity as JSON, enforces validations via a service layer, and secures access with admin authentication.

## Project Structure

```
src/
 └─ scrms/
    ├─ controller/      // Console menus and flows
    ├─ data/            // Generic persistence helpers
    ├─ exceptions/      // Custom runtime exceptions
    ├─ model/           // Domain entities, enums, JSON adapters
    ├─ service/         // Business logic + validation + persistence
    └─ utils/           // Common helpers (IDs, hashing, JSON parsing)
data/                   // Sample JSON data files used at runtime
```

## Key Features

- Admin authentication (default login `admin` / `admin123`)
- CRUD for students, instructors, courses, rooms
- Automatic timetable generation and manual slot creation
- Room booking workflow with conflict detection
- Attendance tracking and grade reporting
- Manual save/reload of JSON data via the UI

## Building & Running

1. **Compile**
   ```powershell
   javac -d out $(Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })
   ```
2. **Run**
   ```powershell
   java -cp out scrms.Main
   ```

All JSON files in `data/` will be updated as you work through the menus.

## Persistence Files

| File | Description |
|------|-------------|
| `data/students.json` | Student records and enrollments |
| `data/instructors.json` | Instructor profiles and assigned courses |
| `data/courses.json` | Course definitions, instructors, and enrolled students |
| `data/rooms.json` | Room metadata with capacities and types |
| `data/timetable.json` | Generated schedule slots |
| `data/bookings.json` | Room booking requests and statuses |
| `data/attendance.json` | Attendance entries per student/course |
| `data/grades.json` | Grade reports |
| `data/admins.json` | Admin credentials (passwords stored as SHA-256 hashes) |

## UML-Style Class Diagram

```
                +-------------------+
                |     abstract      |
                |      User         |
                +-------------------+
                | - username        |
                | - passwordHash    |
                +-------------------+
                           ^
                           |
                   +---------------+
                   |     Admin     |
                   +---------------+
                   | - adminId     |
                   | - fullName    |
                   +---------------+

Student <--> GradeReport <-- Course --> Instructor
   |            ^             ^
   |            |             |
AttendanceRecord|             |
                |             |
              Timetable (ScheduleSlot) --> Room
                               ^
                         RoomBooking (status)
```

The services (`*Service` classes) encapsulate CRUD logic, call validators, detect conflicts, and manage persistence via `DataStore`. `ConsoleController` orchestrates menu flows and invokes services, while `Main` simply wires the registry and launches the controller.

## Sprints / Implementation Phases

1. **Sprint 1 – Foundations**: Set up packages, domain models, utilities, custom JSON handling, and persistence scaffolding.
2. **Sprint 2 – Service Layer**: Implement CRUD/validation logic for students, instructors, rooms, courses, bookings, timetables, attendance, grades, and admin authentication.
3. **Sprint 3 – Console UI**: Build the interactive `ConsoleController` and `Main` entry point with menu-driven flows for every feature.
4. **Sprint 4 – Documentation & Samples**: Create README, UML diagram, and provide example JSON data files for immediate experimentation.

Enjoy exploring SCRMS and tailor it to your campus needs!
