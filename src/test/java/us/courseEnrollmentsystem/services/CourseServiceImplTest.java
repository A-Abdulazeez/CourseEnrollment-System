package us.courseEnrollmentsystem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.courseEnrollmentsystem.data.models.Course;
import us.courseEnrollmentsystem.data.repositories.CourseRepository;
import us.courseEnrollmentsystem.dtos.requests.CreateCourseRequest;
import us.courseEnrollmentsystem.dtos.responses.CreateCourseResponse;
import us.courseEnrollmentsystem.exception.CourseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CourseServiceImplTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    public void setUp() {
        courseRepository.deleteAll();
    }


    @Test
    public void createCourseWithNullRequestThrowsExceptionTest() {
        assertThrows(CourseException.class, () -> courseService.createCourse(null));
    }

    @Test
    public void createCourseWithEmptyRequestThrowsExceptionTest() {
        CreateCourseRequest request = new CreateCourseRequest();
        assertThrows(CourseException.class, () -> courseService.createCourse(request));
    }


    @Test
    public void createCourseWithEmptyCourseIdThrowsExceptionTest() {
        CreateCourseRequest request = new CreateCourseRequest();

        request.setCourseCode("");
        request.setTitle("Metabolism");
        request.setCreditUnit(6);
        request.setDepartment("Biochemistry");

        assertThrows(CourseException.class, () -> courseService.createCourse(request));
    }

    @Test
    public void createCourseWithEmptyTitleThrowsExceptionTest() {
        CreateCourseRequest request = new CreateCourseRequest();

        request.setCourseCode("BCHM411");
        request.setTitle("");
        request.setCreditUnit(6);
        request.setDepartment("Biochemistry");

        assertThrows(CourseException.class, () -> courseService.createCourse(request));
    }

    @Test
    public void createCourseWithZeroCreditUnitThrowsExceptionTest() {
        CreateCourseRequest request = new CreateCourseRequest();

        request.setCourseCode("BCHM411");
        request.setTitle("Metabolism");
        request.setCreditUnit(0);
        request.setDepartment("Biochemistry");

        assertThrows(CourseException.class, () -> courseService.createCourse(request));
    }

    @Test
    public void createCourseWithEmptyDepartmentThrowsExceptionTest() {
        CreateCourseRequest request = new CreateCourseRequest();

        request.setCourseCode("BCHM411");
        request.setTitle("Metabolism");
        request.setCreditUnit(6);
        request.setDepartment("");

        assertThrows(CourseException.class, () -> courseService.createCourse(request));
    }

    @Test
    public void createCourseWithValidRequestSuccessfullyTest() {
        CreateCourseRequest request = new CreateCourseRequest();
        request.setCourseCode("BCHM411");
        request.setTitle("Metabolism");
        request.setCreditUnit(6);
        request.setDepartment("Biochemistry");
        CreateCourseResponse response = courseService.createCourse(request);


        assertEquals(request.getCourseCode(), response.getCourseCode());
        assertEquals(request.getTitle(), response.getTitle());
    }

    @Test
    public void getCourseByNullCourseCodeThrowsExceptionTest() {
        assertThrows(CourseException.class, () -> courseService.getCourseByCode(null));
    }

    @Test
    public void getCourseByEmptyCodeThrowsExceptionTest() {
        assertThrows(CourseException.class, () -> courseService.getCourseByCode(""));
    }

    @Test
    public void getCourseByWrongCodeThrowsExceptionTest() {
        assertThrows(CourseException.class, () -> courseService.getCourseByCode("BCHM999"));
    }

    @Test
    public void getCourseByCodeReturnsCourseTest() {
        CreateCourseRequest request = new CreateCourseRequest();

        request.setCourseCode("BCHM401");
        request.setTitle("Enzymology");
        request.setCreditUnit(8);
        request.setDepartment("Biochemistry");
        courseService.createCourse(request);

        Course course = courseService.getCourseByCode("BCHM401");

        assertEquals(request.getTitle(), course.getTitle());
    }

}