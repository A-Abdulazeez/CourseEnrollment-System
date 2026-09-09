package us.courseEnrollmentsystem.services;

import us.courseEnrollmentsystem.data.models.Enrollment;
import us.courseEnrollmentsystem.dtos.requests.CreateEnrollmentRequest;
import us.courseEnrollmentsystem.dtos.responses.CreateEnrollmentResponse;

import java.util.List;

public interface EnrollmentService {

    CreateEnrollmentResponse createEnrollment(CreateEnrollmentRequest enrollmentRequest);

    Enrollment addCourse(String enrollmentId, String courseCode);

    Enrollment removeCourse(String enrollmentId, String courseCode);

    Enrollment getEnrollmentById(String enrollmentId);

    List<Enrollment> getStudentEnrollments(String studentId);

    List<Enrollment> getAllEnrollments();
    }

