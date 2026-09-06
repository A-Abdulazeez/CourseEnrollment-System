package us.courseEnrollmentsystem.services;


import us.courseEnrollmentsystem.data.models.Student;
import us.courseEnrollmentsystem.dtos.requests.UpdateStudentRequest;
import us.courseEnrollmentsystem.dtos.responses.UpdateStudentResponse;

public interface StudentService {

    Student getStudentByEmail(String email);

    UpdateStudentResponse updateStudent(String email, UpdateStudentRequest updateRequest);


}
