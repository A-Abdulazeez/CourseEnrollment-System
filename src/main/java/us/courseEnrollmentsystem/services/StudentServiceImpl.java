package us.courseEnrollmentsystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.courseEnrollmentsystem.data.models.Student;
import us.courseEnrollmentsystem.data.repositories.StudentRepository;
import us.courseEnrollmentsystem.exception.StudentException;


@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;


    @Override
    public Student getStudentByEmail(String email) {
        if (email == null || email.isEmpty()) throw new StudentException("email cannot be null or empty");
        Student student = studentRepository.findByEmail(email);
        if(student == null) throw new StudentException("Student with email " + email + " not found");

        return student;
    }
}
