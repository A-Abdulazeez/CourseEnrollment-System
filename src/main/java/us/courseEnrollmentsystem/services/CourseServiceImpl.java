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
import static us.courseEnrollmentsystem.utils.Mapper.mapUpdate;
import static us.courseEnrollmentsystem.utils.Validator.validateCourseRequest;
import static us.courseEnrollmentsystem.utils.Validator.validateUpdateCourseRequest;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;


    @Override
    public CreateCourseResponse createCourse(String email, CreateCourseRequest courseRequest) {
        if (!email.equals("admin@administration.com")) throw new CourseException("Only admin can create courses");
        validateCourseRequest(courseRequest);

        Optional<Course> existingCourse = courseRepository.findById(courseRequest.getCourseCode());
        if (existingCourse.isPresent()) throw new CourseException("Course with code " + courseRequest.getCourseCode() + " already exists");

        Course course = map(courseRequest);
        courseRepository.save(course);

        return map(course);
    }

    @Override
    public Course getCourseByCode(String courseCode) {
        if (courseCode == null || courseCode.isEmpty() ) throw new CourseException("Course code cannot be null or empty");

        Optional<Course> existingCourse = courseRepository.findById(courseCode);
        if (existingCourse.isEmpty()) throw new CourseException("Course with code " + courseCode + " does not exist");

        return existingCourse.get();
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        if (courses.isEmpty()) throw new CourseException("Course list is empty");

        return courses;
    }

    @Override
    public UpdateCourseResponse updateCourse(String email, String courseCode, UpdateCourseRequest updateRequest) {
        if (!email.equals("admin@administration.com")) throw new CourseException("Only admin can update courses");
        if (courseCode == null || courseCode.isEmpty() ) throw new CourseException("Course code cannot be null or empty");
        validateUpdateCourseRequest(updateRequest);

        Optional<Course> course = courseRepository.findById(courseCode);
        if (course.isEmpty()) throw new CourseException("Course with code " + courseCode + " not found");

        Course existingCourse = course.get();
        Course updatedCourse = map(updateRequest);

        existingCourse.setTitle(updatedCourse.getTitle());
        existingCourse.setCreditUnit(updatedCourse.getCreditUnit());
        existingCourse.setDepartment(updatedCourse.getDepartment());
        courseRepository.save(existingCourse);

        return mapUpdate(existingCourse);
    }

    @Override
    public String deleteCourse(String email, String courseCode) {
        if (!email.equals("admin@administration.com")) throw new CourseException("Only admin can delete courses");
        if (courseCode == null || courseCode.isEmpty()) throw new CourseException("Course code cannot be null or empty");

        Optional<Course> existingCourse = courseRepository.findById(courseCode);
        if (existingCourse.isEmpty()) throw new CourseException("Course with code " + courseCode + " not found");

        courseRepository.delete(existingCourse.get());

        return "course with code " + courseCode + " deleted";
    }
}
