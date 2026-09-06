package us.courseEnrollmentsystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.courseEnrollmentsystem.data.models.Course;
import us.courseEnrollmentsystem.data.repositories.CourseRepository;
import us.courseEnrollmentsystem.dtos.requests.CreateCourseRequest;
import us.courseEnrollmentsystem.dtos.requests.UpdateCourseRequest;
import us.courseEnrollmentsystem.dtos.responses.CreateCourseResponse;
import us.courseEnrollmentsystem.dtos.responses.UpdateCourseResponse;
import us.courseEnrollmentsystem.exception.CourseException;

import java.util.List;
import java.util.Optional;

import static us.courseEnrollmentsystem.utils.Mapper.map;
import static us.courseEnrollmentsystem.utils.Validator.validateCourseRequest;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;


    @Override
    public CreateCourseResponse createCourse(CreateCourseRequest courseRequest) {
        validateCourseRequest(courseRequest);

        Optional<Course> existingCourse = courseRepository.findById(courseRequest.getCourseCode());
        if (existingCourse.isPresent()) throw new CourseException("Course with code " + courseRequest.getCourseCode() + " already exists");

        Course course = map(courseRequest);
        courseRepository.save(course);

        return map(course);
    }

    @Override
    public Course getCourseByCode(String courseCode) {
        return null;
    }

    @Override
    public List<Course> getAllCourses() {
        return null;
    }

    @Override
    public UpdateCourseResponse updateCourse(String courseCode, UpdateCourseRequest updateRequest) {
        return null;
    }

    @Override
    public String deleteCourse(String courseId) {
        return null;
    }
}
