package us.courseEnrollmentsystem.services;


import us.courseEnrollmentsystem.data.models.Student;

public interface StudentService {

    Student getStudentByEmail(String email);


}
