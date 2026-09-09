package us.courseEnrollmentsystem.data.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import us.courseEnrollmentsystem.data.models.Enrollment;
import us.courseEnrollmentsystem.data.models.Semester;

import java.util.List;

public interface EnrollmentRepository extends MongoRepository<Enrollment,String> {

    List<Enrollment> findByStudentId(String studentId);

    Enrollment findByStudentIdAndSessionAndSemester(String studentId, Integer session, Semester semester);
}
