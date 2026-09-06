package us.courseEnrollmentsystem.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.courseEnrollmentsystem.data.models.Course;
import us.courseEnrollmentsystem.data.repositories.CourseRepository;
import us.courseEnrollmentsystem.dtos.requests.CreateCourseRequest;
import us.courseEnrollmentsystem.dtos.requests.UpdateCourseRequest;
import us.courseEnrollmentsystem.dtos.responses.CreateCourseResponse;
import us.courseEnrollmentsystem.dtos.responses.UpdateCourseResponse;
import us.courseEnrollmentsystem.exception.CourseException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    @AfterEach
    public void tearDown() {
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
    public void createCourseWithExistingCourseCodeThrowsExceptionTest() {
        CreateCourseRequest request = new CreateCourseRequest();
        request.setCourseCode("BCHM411");
        request.setTitle("Metabolism");
        request.setCreditUnit(6);
        request.setDepartment("Biochemistry");

        courseService.createCourse(request);

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

    @Test
    public void getAllCoursesWithNoCoursesThrowsExceptionTest() {
        assertThrows(CourseException.class, () -> courseService.getAllCourses());
    }

    @Test
    public void getAllCoursesReturnsCoursesTest() {
        CreateCourseRequest request = new CreateCourseRequest();
        request.setCourseCode("BCHM411");
        request.setTitle("Metabolism");
        request.setCreditUnit(6);
        request.setDepartment("Biochemistry");
        courseService.createCourse(request);

        CreateCourseRequest request2 = new CreateCourseRequest();
        request2.setCourseCode("BCHM412");
        request2.setTitle("Food Biochemistry");
        request2.setCreditUnit(6);
        request2.setDepartment("Biochemistry");
        courseService.createCourse(request2);

        List<Course> courses = courseService.getAllCourses();

        assertEquals(2, courses.size());
    }

    @Test
    public void updateCourseWithNullCodeThrowsExceptionTest() {
        UpdateCourseRequest updateRequest = new UpdateCourseRequest();
        assertThrows(CourseException.class, () -> courseService.updateCourse(null, updateRequest));
    }

    @Test
    public void updateCourseWithEmptyCodeThrowsExceptionTest() {
        UpdateCourseRequest updateRequest = new UpdateCourseRequest();
        assertThrows(CourseException.class, () -> courseService.updateCourse("", updateRequest));
    }

    @Test
    public void updateCourseWithNullRequestThrowsExceptionTest() {
        assertThrows(CourseException.class, () -> courseService.updateCourse("BCHM411", null));
    }

    @Test
    public void updateCourseWithWrongCodeThrowsExceptionTest() {
        CreateCourseRequest createRequest = new CreateCourseRequest();
        createRequest.setCourseCode("BCHM411");
        createRequest.setTitle("Metabolism");
        createRequest.setCreditUnit(6);
        createRequest.setDepartment("Biochemistry");
        courseService.createCourse(createRequest);

        UpdateCourseRequest updateRequest = new UpdateCourseRequest();
        updateRequest.setTitle("Advanced Metabolism");
        updateRequest.setCreditUnit(4);
        updateRequest.setDepartment("Biochemistry");

        assertThrows(CourseException.class, () -> courseService.updateCourse("BCHM999", updateRequest));
    }


    @Test
    public void updateCourseUpdatesCourseSuccessfullyTest() {
        CreateCourseRequest createRequest = new CreateCourseRequest();
        createRequest.setCourseCode("BCHM411");
        createRequest.setTitle("Metabolism");
        createRequest.setCreditUnit(6);
        createRequest.setDepartment("Biochemistry");
        courseService.createCourse(createRequest);

        UpdateCourseRequest updateRequest = new UpdateCourseRequest();
        updateRequest.setTitle("Advanced Metabolism");
        updateRequest.setCreditUnit(4);
        updateRequest.setDepartment("Biochemistry");

        UpdateCourseResponse response = courseService.updateCourse("BCHM411", updateRequest);

        assertEquals(updateRequest.getTitle(), response.getTitle());
        assertEquals(updateRequest.getDepartment(), response.getDepartment());
        assertEquals(createRequest.getCourseCode(), response.getCourseCode());
    }

    @Test
    public void updateCourseUpdatesCourseSuccessfullyButCourseCodeRemainUnchangedTest() {
        CreateCourseRequest createRequest = new CreateCourseRequest();
        createRequest.setCourseCode("BCHM411");
        createRequest.setTitle("Metabolism");
        createRequest.setCreditUnit(6);
        createRequest.setDepartment("Biochemistry");
        courseService.createCourse(createRequest);

        UpdateCourseRequest updateRequest = new UpdateCourseRequest();
        updateRequest.setTitle("Advanced Metabolism");
        updateRequest.setCreditUnit(4);
        updateRequest.setDepartment("Biochemistry");
        courseService.updateCourse("BCHM411", updateRequest);

        Optional<Course> result = courseRepository.findById("BCHM411");

        assertEquals(result.get().getCourseCode(), createRequest.getCourseCode());
        assertEquals(result.get().getTitle(), updateRequest.getTitle());
        assertEquals(result.get().getDepartment(), updateRequest.getDepartment());

    }

    @Test
    public void deleteCourseWithNullCodeThrowsExceptionTest() {
        assertThrows(CourseException.class, () -> courseService.deleteCourse(null));
    }

    @Test
    public void deleteCourseWithEmptyCodeThrowsExceptionTest() {
        assertThrows(CourseException.class, () -> courseService.deleteCourse(""));
    }

    @Test
    public void deleteCourseWithWrongCodeThrowsExceptionTest() {
        assertThrows(CourseException.class, () -> courseService.deleteCourse("BCHM999"));
    }

    @Test
    public void deleteCourseSuccessfullyDeletesCourseTest() {
        CreateCourseRequest request = new CreateCourseRequest();
        request.setCourseCode("BCHM411");
        request.setTitle("Metabolism");
        request.setCreditUnit(6);
        request.setDepartment("Biochemistry");
        courseService.createCourse(request);

        courseService.deleteCourse("BCHM411");

        Optional<Course> result = courseRepository.findById("BCHM411");
        assertTrue(result.isEmpty());
    }

}