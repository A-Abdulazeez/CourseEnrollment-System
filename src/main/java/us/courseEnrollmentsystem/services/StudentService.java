package us.courseEnrollmentsystem.services;


import us.courseEnrollmentsystem.data.models.Student;
import us.courseEnrollmentsystem.dtos.requests.UpdateStudentRequest;
import us.courseEnrollmentsystem.dtos.responses.UpdateStudentResponse;

import java.util.List;

public interface StudentService {

    Student getStudentByEmail(String email);

    UpdateStudentResponse updateStudent(String email, UpdateStudentRequest updateRequest);

    List<Student> getAllStudents();



}
