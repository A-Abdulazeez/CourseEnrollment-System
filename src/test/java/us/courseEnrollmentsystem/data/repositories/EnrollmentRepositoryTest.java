package us.courseEnrollmentsystem.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.courseEnrollmentsystem.data.models.Enrollment;
import us.courseEnrollmentsystem.data.models.Semester;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private Enrollment createEnrollment(
            String studentId,
            Integer session,
            Semester semester,
            List<String> courseCodes) {

        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(studentId);
        enrollment.setSession(session);
        enrollment.setSemester(semester);
        enrollment.setCourseCodes(courseCodes);

        return enrollment;
    }

    @BeforeEach
    public void setUp() {
        enrollmentRepository.deleteAll();
    }

    @Test
    public void saveEnrollmentSuccessfulTest() {

        Enrollment enrollment = createEnrollment(
                "STUDENT001",
                2027,
                Semester.FIRST_SEMESTER,
                List.of("BCHM111", "MATH101")
        );

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        assertEquals(2, savedEnrollment.getCourseCodes().size());
        assertEquals(1, enrollmentRepository.count());
    }

    @Test
    public void findEnrollmentsByStudentIdTest() {
        enrollmentRepository.save(createEnrollment(
                "STUDENT001",
                2027,
                Semester.FIRST_SEMESTER,
                List.of("BCHM111")));

        enrollmentRepository.save(createEnrollment(
                "STUDENT001",
                2027,
                Semester.SECOND_SEMESTER,
                List.of("BCHM112")));

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId("STUDENT001");

        assertEquals(2, enrollments.size());
    }

    @Test
    public void findEnrollmentByStudentIdAndSessionAndSemesterTest() {

        enrollmentRepository.save(createEnrollment(
                "STUDENT001",
                2027,
                Semester.FIRST_SEMESTER,
                List.of("BCHM111", "MATH101")
        ));

        Enrollment foundEnrollment =
                enrollmentRepository
                        .findByStudentIdAndSessionAndSemester(
                                "STUDENT001",
                                2027,
                                Semester.FIRST_SEMESTER
                        );

        assertEquals(List.of("BCHM111", "MATH101"), foundEnrollment.getCourseCodes());
    }
}