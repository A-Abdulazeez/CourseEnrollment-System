package us.courseEnrollmentsystem.utils;

import us.courseEnrollmentsystem.dtos.requests.*;
import us.courseEnrollmentsystem.exception.CourseException;
import us.courseEnrollmentsystem.exception.StudentException;

import javax.security.auth.login.LoginException;

public class Validator {

    private static final int MIN_PASSWORD_LENGTH = 6;

    public static void validateStudentRequest(RegisterStudentRequest studentRequest){
        if (studentRequest == null) throw new StudentException("Student request cannot be null");
        if (studentRequest.getName() == null || studentRequest.getName().isEmpty()) throw new StudentException("Student name cannot be empty");
        if (studentRequest.getEmail() == null || studentRequest.getEmail().isEmpty()) throw new StudentException("Student email cannot be empty");
        if(studentRequest.getDepartment() == null || studentRequest.getDepartment().isEmpty()) throw new StudentException("Department name cannot be empty");
        if (studentRequest.getPassword() == null || studentRequest.getPassword().isEmpty() || studentRequest.getPassword().length() < MIN_PASSWORD_LENGTH ) throw new StudentException("Password cannot be empty or less than 6 characters");
    }

    public static void validateLoginRequest(LoginRequest loginRequest){
        if (loginRequest == null) throw new StudentException("Login request cannot be null");
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) throw new StudentException("Email cannot be empty");
        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty() || loginRequest.getPassword().length() < MIN_PASSWORD_LENGTH) throw new StudentException("Password cannot be empty");
    }

    public static void validateCourseRequest(CreateCourseRequest courseRequest){
        if (courseRequest == null) throw new CourseException("Course request cannot be null");
        if (courseRequest.getCourseCode() == null || courseRequest.getCourseCode().isEmpty()) throw new CourseException("Course id cannot be empty");
        if (courseRequest.getTitle() == null || courseRequest.getTitle().isEmpty()) throw new CourseException("Course title cannot be empty");
        if (courseRequest.getCreditUnit() == 0 ) throw  new CourseException("Credit unit cannot be empty");
        if (courseRequest.getDepartment() == null || courseRequest.getDepartment().isEmpty()) throw  new CourseException("Department name cannot be empty");
    }

    public static void validateStudentRequest(UpdateStudentRequest studentRequest){
        if (studentRequest == null) throw new StudentException("Student request cannot be null");
        if (studentRequest.getName() == null || studentRequest.getName().isEmpty()) throw new StudentException("Student name cannot be empty");
        if(studentRequest.getDepartment() == null || studentRequest.getDepartment().isEmpty()) throw new StudentException("Department name cannot be empty");
        if (studentRequest.getPassword() == null || studentRequest.getPassword().isEmpty() || studentRequest.getPassword().length() < MIN_PASSWORD_LENGTH ) throw new StudentException("Password cannot be empty or less than 6 characters");
    }

    public static void validateUpdateCourseRequest(UpdateCourseRequest updateRequest) {
        if (updateRequest == null) throw new CourseException("Update request cannot be null");
        if (updateRequest.getTitle() == null || updateRequest.getTitle().isEmpty()) throw new CourseException("Course title cannot be empty");
        if (updateRequest.getCreditUnit() == 0) throw new CourseException("Credit unit cannot be empty");
        if (updateRequest.getDepartment() == null || updateRequest.getDepartment().isEmpty()) throw new CourseException("Department name cannot be empty");
    }
}
