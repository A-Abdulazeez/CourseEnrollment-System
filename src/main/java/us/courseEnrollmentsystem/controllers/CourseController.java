package us.courseEnrollmentsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.courseEnrollmentsystem.data.models.Course;
import us.courseEnrollmentsystem.dtos.requests.CreateCourseRequest;
import us.courseEnrollmentsystem.dtos.requests.UpdateCourseRequest;
import us.courseEnrollmentsystem.dtos.responses.CreateCourseResponse;
import us.courseEnrollmentsystem.dtos.responses.UpdateCourseResponse;
import us.courseEnrollmentsystem.services.CourseService;

import java.util.List;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/create-course")
    public CreateCourseResponse createCourse (@RequestBody CreateCourseRequest createCourseRequest){
        return courseService.createCourse(createCourseRequest);
    }

    @GetMapping("/course-code/{courseCode}")
    public Course getCourse(@PathVariable("courseCode") String courseCode) {
        return courseService.getCourseByCode(courseCode);
    }

    @GetMapping("/get-courses")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PutMapping("/update-course/{courseCode}")
    public UpdateCourseResponse updateCourse(@PathVariable("courseCode") String courseCode, @RequestBody UpdateCourseRequest updateCourseRequest) {
        return courseService.updateCourse(courseCode, updateCourseRequest);
    }

    @DeleteMapping("/delete-course/{courseCode}")
    public String deleteCourse(@PathVariable("courseCode") String courseCode) {
        return courseService.deleteCourse(courseCode);
    }
}
