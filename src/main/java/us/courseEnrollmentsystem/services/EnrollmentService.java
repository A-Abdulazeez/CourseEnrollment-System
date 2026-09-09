package us.courseEnrollmentsystem.services;

import us.courseEnrollmentsystem.data.models.Enrollment;
import us.courseEnrollmentsystem.dtos.requests.CreateEnrollmentRequest;
import us.courseEnrollmentsystem.dtos.responses.AddCourseResponse;
import us.courseEnrollmentsystem.dtos.responses.CreateEnrollmentResponse;
import us.courseEnrollmentsystem.dtos.responses.RemoveCourseResponse;

import java.util.List;

public interface EnrollmentService {

    CreateEnrollmentResponse createEnrollment(CreateEnrollmentRequest enrollmentRequest);

    AddCourseResponse addCourse( String email, String enrollmentId, String courseCode);

    RemoveCourseResponse removeCourse(String enrollmentId, String courseCode);

    Enrollment getEnrollmentById(String enrollmentId);

    List<Enrollment> getStudentEnrollments(String studentId);

    List<Enrollment> getAllEnrollments(String email);
    }

