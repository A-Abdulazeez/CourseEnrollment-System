package us.courseEnrollmentsystem.services;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.courseEnrollmentsystem.data.models.Student;
import us.courseEnrollmentsystem.dtos.requests.RegisterStudentRequest;
import us.courseEnrollmentsystem.exception.StudentException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class StudentServiceImplTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AuthService authService;



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
}