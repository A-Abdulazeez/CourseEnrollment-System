package us.courseEnrollmentsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.courseEnrollmentsystem.data.models.Enrollment;
import us.courseEnrollmentsystem.dtos.requests.CreateEnrollmentRequest;
import us.courseEnrollmentsystem.dtos.responses.AddCourseResponse;
import us.courseEnrollmentsystem.dtos.responses.CreateEnrollmentResponse;
import us.courseEnrollmentsystem.dtos.responses.RemoveCourseResponse;
import us.courseEnrollmentsystem.services.EnrollmentService;

import java.util.List;

@RestController
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/create-enrollment")
    public CreateEnrollmentResponse createEnrollment(@RequestBody CreateEnrollmentRequest enrollmentRequest) {
        return enrollmentService.createEnrollment(enrollmentRequest);
    }

    @PutMapping("/add-course/{enrollmentId}/{courseCode}")
    public AddCourseResponse addCourse(
            @RequestHeader("email") String email,
            @PathVariable("enrollmentId") String enrollmentId,
            @PathVariable("courseCode") String courseCode)
    {

        return enrollmentService.addCourse(email, enrollmentId, courseCode);
    }

    @PutMapping("/remove-course/{enrollmentId}/{courseCode}")
    public RemoveCourseResponse removeCourse(
            @RequestHeader("email") String email,
            @PathVariable("enrollmentId") String enrollmentId,
            @PathVariable("courseCode") String courseCode) {

        return enrollmentService.removeCourse(email, enrollmentId, courseCode);
    }

    @GetMapping("/get-enrollment/{enrollmentId}")
    public Enrollment getEnrollmentById(@PathVariable("enrollmentId") String enrollmentId) {
        return enrollmentService.getEnrollmentById(enrollmentId);
    }


    @GetMapping("/get-student-enrollments/{studentId}")
    public List<Enrollment> getStudentEnrollments(@PathVariable("studentId") String studentId) {
        return enrollmentService.getStudentEnrollments(studentId);
    }


    @GetMapping("/get-all-enrollments")
    public List<Enrollment> getAllEnrollments(@RequestHeader("email") String email) {
        return enrollmentService.getAllEnrollments(email);
    }

}
