package us.courseEnrollmentsystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.courseEnrollmentsystem.data.models.Enrollment;
import us.courseEnrollmentsystem.data.repositories.CourseRepository;
import us.courseEnrollmentsystem.data.repositories.EnrollmentRepository;
import us.courseEnrollmentsystem.data.repositories.StudentRepository;
import us.courseEnrollmentsystem.dtos.requests.CreateEnrollmentRequest;
import us.courseEnrollmentsystem.dtos.responses.AddCourseResponse;
import us.courseEnrollmentsystem.dtos.responses.CreateEnrollmentResponse;
import us.courseEnrollmentsystem.dtos.responses.RemoveCourseResponse;
import us.courseEnrollmentsystem.exception.EnrollmentException;

import java.util.List;

import static us.courseEnrollmentsystem.utils.Mapper.*;
import static us.courseEnrollmentsystem.utils.Validator.validateEnrollmentRequest;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;


    @Override
    public CreateEnrollmentResponse createEnrollment(CreateEnrollmentRequest enrollmentRequest) {
        validateEnrollmentRequest(enrollmentRequest);

        if (studentRepository.findById(enrollmentRequest.getStudentId()).isEmpty()) throw new EnrollmentException("Student not found");

        Enrollment existingEnrollment = enrollmentRepository.findByStudentIdAndSessionAndSemester(
                enrollmentRequest.getStudentId(), enrollmentRequest.getSession(), enrollmentRequest.getSemester()
        );

        if (existingEnrollment != null) throw new EnrollmentException("Student Already Enrolled For This Semester");

        for (String courseCode : enrollmentRequest.getCourseCodes()) {
            if (courseRepository.findById(courseCode).isEmpty()) throw new EnrollmentException("Course " + courseCode + " Not Found");
        }

        Enrollment enrollment = map(enrollmentRequest);
        enrollmentRepository.save(enrollment);

        return map(enrollment);
    }

    @Override
    public AddCourseResponse addCourse(String enrollmentId, String courseCode) {
        if (enrollmentId == null || enrollmentId.isEmpty()) throw new EnrollmentException("Enrollment id cannot be null or empty");
        if (courseCode == null || courseCode.isEmpty()) throw new EnrollmentException("Course code cannot be null or empty");

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> new EnrollmentException("Enrollment with id " + enrollmentId + " not found"));

        if (courseRepository.findById(courseCode).isEmpty()) throw new EnrollmentException("Course with code " + courseCode + " does not exist");
        if (enrollment.getCourseCodes().contains(courseCode)) throw new EnrollmentException("Course already added to enrollment");
        enrollment.getCourseCodes().add(courseCode);
        enrollmentRepository.save(enrollment);

        return mapAddCourse(enrollment);
    }

    @Override
    public RemoveCourseResponse removeCourse(String enrollmentId, String courseCode) {
        if (enrollmentId == null || enrollmentId.isEmpty()) throw new EnrollmentException("Enrollment id cannot be null or empty");
        if (courseCode == null || courseCode.isEmpty()) throw new EnrollmentException("Course code cannot be null or empty");

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> new EnrollmentException("Enrollment with id " + enrollmentId + " not found"));

        if (!enrollment.getCourseCodes().contains(courseCode)) throw new EnrollmentException("Course is not in this enrollment");
        enrollment.getCourseCodes().remove(courseCode);
        enrollmentRepository.save(enrollment);

        return mapRemoveCourse(enrollment);
    }

    @Override
    public Enrollment getEnrollmentById(String enrollmentId) {
        if (enrollmentId == null || enrollmentId.isEmpty()) throw new EnrollmentException("Enrollment id cannot be null or empty");

        return enrollmentRepository.findById(enrollmentId).orElseThrow(() ->
                        new EnrollmentException("Enrollment with id " + enrollmentId + " not found"));

    }

    @Override
    public List<Enrollment> getStudentEnrollments(String studentId) {
        if (studentId == null || studentId.isEmpty()) throw new EnrollmentException("Student id cannot be null or empty");

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        if (enrollments.isEmpty()) throw new EnrollmentException("No enrollments found for student " + studentId);

        return enrollments;
    }

    @Override
    public List<Enrollment> getAllEnrollments(String email) {
        if (!"admin@administration.com".equals(email)) throw new EnrollmentException("Only admin can view all enrollments");

        List<Enrollment> enrollments = enrollmentRepository.findAll();
        if (enrollments.isEmpty()) throw new EnrollmentException("Enrollment list is empty");

        return enrollments;
    }
}