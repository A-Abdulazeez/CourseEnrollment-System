package us.courseEnrollmentsystem.services;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.courseEnrollmentsystem.data.models.Student;
import us.courseEnrollmentsystem.data.repositories.StudentRepository;
import us.courseEnrollmentsystem.dtos.requests.RegisterStudentRequest;
import us.courseEnrollmentsystem.dtos.requests.UpdateStudentRequest;
import us.courseEnrollmentsystem.dtos.responses.UpdateStudentResponse;
import us.courseEnrollmentsystem.exception.StudentException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class StudentServiceImplTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AuthService authService;

    @Autowired
    private StudentRepository studentRepository;


    @BeforeEach
    public void setup(){
        studentRepository.deleteAll();
    }

    @AfterEach
    public void tearDown(){
        studentRepository.deleteAll();
    }



    @Test
    public void getStudentWithNullEmailThrowsException() {
        assertThrows(StudentException.class,() -> studentService.getStudentByEmail(null));
    }

    @Test
    public void getStudentWithEmptyEmailThrowsException() {
        assertThrows(StudentException.class,() -> studentService.getStudentByEmail(""));
    }

    @Test
    public void getStudentByWrongEmailThrowsExceptionTest() {
        assertThrows(StudentException.class, () -> studentService.getStudentByEmail("wrong@email"));
    }

    @Test
    public void getStudentByEmailFromDbReturnsStudentTest() {
        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setName("Danjuma");
        request.setEmail("dBoy@gmail.com");
        request.setDepartment("Biochemistry");
        request.setPassword("123456");

        authService.registerStudent(request);

        Student student = studentService.getStudentByEmail("dBoy@gmail.com");

        assertNotNull(student);
        assertEquals(request.getEmail(), student.getEmail());
        assertEquals(request.getName(), student.getName());
        assertEquals(request.getDepartment(), student.getDepartment());
    }

    @Test
    public void updateStudentWithNullEmailThrowsException() {
        UpdateStudentRequest updateRequest = new UpdateStudentRequest();
        assertThrows( StudentException.class, () -> studentService.updateStudent(null, updateRequest) );
    }

    @Test
    public void updateStudentWithEmptyEmailThrowsException() {
        UpdateStudentRequest updateRequest = new UpdateStudentRequest();
        assertThrows( StudentException.class, () -> studentService.updateStudent("", updateRequest) );
    }

    @Test
    public void updateStudentWithNullRequestThrowsException() {
        assertThrows( StudentException.class, () -> studentService.updateStudent("az@gmail.com", null) );
    }

    @Test
    public void updateStudentWithWrongEmailThrowsException() {
        UpdateStudentRequest updateRequest = new UpdateStudentRequest();
        updateRequest.setName("Azeez");
        updateRequest.setDepartment("Biochemistry");
        updateRequest.setPassword("123456");
        assertThrows( StudentException.class, () -> studentService.updateStudent( "wrong@email.com", updateRequest ) );
    }

    @Test
    public void updateStudentSuccessfullyFromDbTest() {
        RegisterStudentRequest registerRequest = new RegisterStudentRequest();
        registerRequest.setName("Azeez");
        registerRequest.setEmail("opeyemi@gmail.com");
        registerRequest.setDepartment("Biochemistry");
        registerRequest.setPassword("123456");
        authService.registerStudent(registerRequest);

        UpdateStudentRequest updateRequest = new UpdateStudentRequest();
        updateRequest.setName("Azeez Abdullahi");
        updateRequest.setDepartment("Computer Science");
        updateRequest.setPassword("changed");
        UpdateStudentResponse response = studentService.updateStudent( "opeyemi@gmail.com", updateRequest );

        assertNotNull(response);
        assertEquals("Azeez Abdullahi", response.getName());
        assertEquals("Computer Science", response.getDepartment());
        assertEquals("Update Successful", response.getMessage());
    }

    @Test
    public void updateStudentKeepsEmailUnchangedTest() {
        RegisterStudentRequest registerRequest = new RegisterStudentRequest();
        registerRequest.setName("Azeez");
        registerRequest.setEmail("az@gmail.com");
        registerRequest.setDepartment("Biochemistry");
        registerRequest.setPassword("123456");
        authService.registerStudent(registerRequest);

        UpdateStudentRequest updateRequest = new UpdateStudentRequest();
        updateRequest.setName("Azeez Abdullahi");
        updateRequest.setDepartment("Computer Science");
        updateRequest.setPassword("654321");
        studentService.updateStudent( "az@gmail.com", updateRequest );

        Student student = studentRepository.findByEmail("az@gmail.com");
        assertEquals(registerRequest.getEmail() , student.getEmail()); }

    @Test
    public void getAllStudentsWithNoStudentsThrowsExceptionTest() {
        assertThrows(StudentException.class, () -> studentService.getAllStudents());
    }

    @Test
    public void getAllStudentsReturnsStudentsListTest() {

        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setName("Azeez");
        request.setEmail("az@gmail.com");
        request.setDepartment("Biochemistry");
        request.setPassword("123456");
        authService.registerStudent(request);

        RegisterStudentRequest request2 = new RegisterStudentRequest();
        request2.setName("Azeez skipp");
        request2.setEmail("a2z@gmail.com");
        request2.setDepartment("Biochemistry");
        request2.setPassword("654321");
        authService.registerStudent(request2);

        List<Student> students = studentService.getAllStudents();

        assertEquals(2, students.size());
    }
}