package us.courseEnrollmentsystem.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.courseEnrollmentsystem.data.models.Course;
import us.courseEnrollmentsystem.data.models.Enrollment;
import us.courseEnrollmentsystem.data.models.Semester;
import us.courseEnrollmentsystem.data.models.Student;
import us.courseEnrollmentsystem.data.repositories.CourseRepository;
import us.courseEnrollmentsystem.data.repositories.EnrollmentRepository;
import us.courseEnrollmentsystem.data.repositories.StudentRepository;
import us.courseEnrollmentsystem.dtos.requests.CreateEnrollmentRequest;
import us.courseEnrollmentsystem.dtos.responses.CreateEnrollmentResponse;
import us.courseEnrollmentsystem.exception.EnrollmentException;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EnrollmentServiceImplTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;


    @BeforeEach
    public void setUp() {
        enrollmentRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        enrollmentRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    public void createEnrollmentWithNullRequestThrowsExceptionTest() {
        assertThrows(EnrollmentException.class, () -> enrollmentService.createEnrollment(null));
    }

    @Test
    public void createEnrollmentWithNullSessionThrowsExceptionTest() {
        CreateEnrollmentRequest request = new CreateEnrollmentRequest();
        request.setSession(null);
        request.setSemester(Semester.FIRST_SEMESTER);
        request.setStudentId("12345");
        request.setCourseCodes(List.of("BCHM411"));

        assertThrows(EnrollmentException.class, () -> enrollmentService.createEnrollment(request));
    }


    @Test
    public void createEnrollmentWithNullSemesterThrowsExceptionTest() {
        CreateEnrollmentRequest request = new CreateEnrollmentRequest();
        request.setSession(2027);
        request.setSemester(null);
        request.setStudentId("12345");
        request.setCourseCodes(List.of("BCHM411"));

        assertThrows(EnrollmentException.class, () -> enrollmentService.createEnrollment(request));
    }


    @Test
    public void createEnrollmentWithEmptyStudentIdThrowsExceptionTest() {
        CreateEnrollmentRequest request = new CreateEnrollmentRequest();
        request.setSession(2027);
        request.setSemester(Semester.FIRST_SEMESTER);
        request.setStudentId("");
        request.setCourseCodes(List.of("BCHM411"));

        assertThrows(EnrollmentException.class, () -> enrollmentService.createEnrollment(request));
    }


    @Test
    public void createEnrollmentWithEmptyCourseListThrowsExceptionTest() {
        CreateEnrollmentRequest request = new CreateEnrollmentRequest();
        request.setSession(2027);
        request.setSemester(Semester.FIRST_SEMESTER);
        request.setStudentId("12345");
        request.setCourseCodes(List.of());

        assertThrows(EnrollmentException.class, () -> enrollmentService.createEnrollment(request));
    }


    @Test
    public void createEnrollmentWithStudentThatDoesNotExistThrowsExceptionTest() {
        Course course = new Course();
        course.setCourseCode("BCHM411");
        course.setTitle("Metabolism");
        course.setCreditUnit(6);
        course.setDepartment("Biochemistry");
        courseRepository.save(course);

        CreateEnrollmentRequest request = new CreateEnrollmentRequest();
        request.setSession(2027);
        request.setSemester(Semester.FIRST_SEMESTER);
        request.setStudentId("studentDoesNotExist");
        request.setCourseCodes(List.of("BCHM411"));

        assertThrows(EnrollmentException.class, () -> enrollmentService.createEnrollment(request));
    }


    @Test
    public void createEnrollmentWithCourseThatDoesNotExistThrowsExceptionTest() {
        Student student = new Student();
        student.setName("Az Maya");
        student.setEmail("maya@gmail.com");
        student.setPassword("Password123");
        student.setDepartment("Biochemistry");
        studentRepository.save(student);

        CreateEnrollmentRequest request = new CreateEnrollmentRequest();
        request.setSession(2027);
        request.setSemester(Semester.FIRST_SEMESTER);
        request.setStudentId(student.getStudentId());
        request.setCourseCodes(List.of("BCHM999"));

        assertThrows(EnrollmentException.class, () -> enrollmentService.createEnrollment(request));
    }


    @Test
    public void studentCannotHaveTwoEnrollmentsForSameSessionAndSemesterTest() {
        Student student = new Student();
        student.setName("John Doe");
        student.setEmail("john@gmail.com");
        student.setPassword("Password123");
        student.setDepartment("Biochemistry");
        studentRepository.save(student);

        Course course = new Course();
        course.setCourseCode("BCHM411");
        course.setTitle("Metabolism");
        course.setCreditUnit(6);
        course.setDepartment("Biochemistry");
        courseRepository.save(course);

        Enrollment enrollment = new Enrollment();
        enrollment.setSession(2027);
        enrollment.setSemester(Semester.FIRST_SEMESTER);
        enrollment.setStudentId(student.getStudentId());
        enrollment.setCourseCodes(List.of("BCHM411"));
        enrollmentRepository.save(enrollment);

        CreateEnrollmentRequest request = new CreateEnrollmentRequest();
        request.setSession(2027);
        request.setSemester(Semester.FIRST_SEMESTER);
        request.setStudentId(student.getStudentId());
        request.setCourseCodes(List.of("BCHM411"));

        assertThrows(EnrollmentException.class, () -> enrollmentService.createEnrollment(request));
    }

    @Test
    public void createEnrollmentSuccessfullyTest() {
        Student student = new Student();
        student.setName("Az Maya");
        student.setEmail("maya@gmail.com");
        student.setPassword("Password123");
        student.setDepartment("Biochemistry");
        studentRepository.save(student);

        Course course1 = new Course();
        course1.setCourseCode("BCHM411");
        course1.setTitle("Metabolism");
        course1.setCreditUnit(6);
        course1.setDepartment("Biochemistry");
        courseRepository.save(course1);

        Course course2 = new Course();
        course2.setCourseCode("BCHM412");
        course2.setTitle("Enzymology");
        course2.setCreditUnit(4);
        course2.setDepartment("Biochemistry");
        courseRepository.save(course2);

        CreateEnrollmentRequest request = new CreateEnrollmentRequest();
        request.setSession(2027);
        request.setSemester(Semester.FIRST_SEMESTER);
        request.setStudentId(student.getStudentId());
        request.setCourseCodes(List.of("BCHM411", "BCHM412"));
        CreateEnrollmentResponse response = enrollmentService.createEnrollment(request);

        assertEquals(2027, response.getSession());
        assertEquals(Semester.FIRST_SEMESTER, response.getSemester());
        assertEquals(student.getStudentId(), response.getStudentId());
        assertEquals(2, response.getCourseCodes().size());
    }

}