package us.courseEnrollmentsystem.services;

import us.courseEnrollmentsystem.data.models.Course;
import us.courseEnrollmentsystem.dtos.requests.CreateCourseRequest;
import us.courseEnrollmentsystem.dtos.requests.UpdateCourseRequest;
import us.courseEnrollmentsystem.dtos.responses.CreateCourseResponse;
import us.courseEnrollmentsystem.dtos.responses.UpdateCourseResponse;

import java.util.List;

public interface CourseService {

    CreateCourseResponse createCourse(CreateCourseRequest courseRequest);

    Course getCourseByCode(String courseCode);

    List<Course> getAllCourses();

    UpdateCourseResponse updateCourse(String courseCode, UpdateCourseRequest updateRequest);

    String deleteCourse(String courseCode);
}
