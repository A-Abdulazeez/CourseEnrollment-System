package us.courseEnrollmentsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.courseEnrollmentsystem.data.models.Student;
import us.courseEnrollmentsystem.dtos.requests.UpdateStudentRequest;
import us.courseEnrollmentsystem.dtos.responses.UpdateStudentResponse;
import us.courseEnrollmentsystem.services.StudentService;

import java.util.List;


@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;


    @GetMapping("/get-student/{email}")
    public Student getStudentByEmail(@PathVariable("email") String email) {
        return studentService.getStudentByEmail(email);
    }


    @PutMapping("/update-student/{email}")
    public UpdateStudentResponse updateStudent(@PathVariable("email") String email, @RequestBody UpdateStudentRequest updateRequest) {
        return studentService.updateStudent(email, updateRequest);
    }


    @GetMapping("/get-all-students")
    public List<Student> getAllStudents(@RequestHeader("email") String email) {
        return studentService.getAllStudents(email);
    }

}
