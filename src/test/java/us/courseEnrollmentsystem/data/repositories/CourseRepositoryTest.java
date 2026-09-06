package us.courseEnrollmentsystem.data.repositories;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.courseEnrollmentsystem.data.models.Course;
import us.courseEnrollmentsystem.services.CourseService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    public void setup(){
        courseRepository.deleteAll();
    }

    @AfterEach
    public void tearDown(){
        courseRepository.deleteAll();
    }

    @Test
    public void createCourseRepositoryCountIsOneTest(){
        Course course = new Course();
        course.setCreditUnit(6);
        course.setCourseCode("BCHM411");
        course.setTitle("Metabolism");
        course.setDepartment("Biochemistry");

        courseRepository.save(course);

        assertEquals(1, courseRepository.count());
    }

    @Test
    public void createCourse_findByIdReturnsCourseTest(){
        Course course = new Course();
        course.setCreditUnit(6);
        course.setCourseCode("BCHM411");
        course.setTitle("Metabolism");
        course.setDepartment("Biochemistry");

        courseRepository.save(course);

        Optional<Course> result = courseRepository.findById("BCHM411");

        assertTrue(result.isPresent());
    }

}