package us.courseEnrollmentsystem.utils;

import us.courseEnrollmentsystem.data.models.*;
import us.courseEnrollmentsystem.dtos.requests.*;
import us.courseEnrollmentsystem.dtos.responses.*;

public class Mapper {

    public static Student map(RegisterStudentRequest studentRequest){
        Student student = new Student();
        student.setName(studentRequest.getName());
        student.setEmail(studentRequest.getEmail());
        student.setDepartment(studentRequest.getDepartment());
        student.setPassword(studentRequest.getPassword());
        student.setRole(Role.STUDENT);

        return student;
    }

    public static RegisterStudentResponse map(Student student){
        RegisterStudentResponse studentResponse = new RegisterStudentResponse();
        studentResponse.setEmail(student.getEmail());
        studentResponse.setName(student.getName());
        studentResponse.setDepartment(student.getDepartment());
        studentResponse.setStudentId(student.getStudentId());

        return studentResponse;
    }

    public static LoginResponse mapLogin(Student student) {

        LoginResponse response = new LoginResponse();

        response.setEmail(student.getEmail());
        response.setMessage("Login successful");
        response.setRole(Role.STUDENT);

        return response;
    }

    public static LoginResponse map(Admin admin) {

        LoginResponse response = new LoginResponse();

        response.setEmail(admin.getADMIN_EMAIL());
        response.setMessage("Login successful");
        response.setRole(Role.ADMIN);

        return response;
    }

    public static Course map(CreateCourseRequest courseRequest){
        Course course = new Course();
        course.setCourseCode(courseRequest.getCourseCode());
        course.setTitle(courseRequest.getTitle());
        course.setCreditUnit(courseRequest.getCreditUnit());
        course.setDepartment(courseRequest.getDepartment());

        return course;
    }

    public static CreateCourseResponse map(Course course){
        CreateCourseResponse courseResponse = new CreateCourseResponse();
        courseResponse.setCourseCode(course.getCourseCode());
        courseResponse.setTitle(course.getTitle());
        courseResponse.setCreditUnit(course.getCreditUnit());
        courseResponse.setDepartment(course.getDepartment());
        courseResponse.setMessage("Course created successfully");

        return courseResponse;
    }

    public static Student map(UpdateStudentRequest updateRequest){
        Student student = new Student();
        student.setName(updateRequest.getName());
        student.setPassword(updateRequest.getPassword());
        student.setDepartment(updateRequest.getDepartment());
        student.setRole(Role.STUDENT);

        return student;
    }

    public static UpdateStudentResponse mapUpdate(Student student){
        UpdateStudentResponse response = new UpdateStudentResponse();
        response.setName(student.getName());
        response.setDepartment(student.getDepartment());
        response.setMessage("Update Successful");

        return response;
    }

    public static Course map(UpdateCourseRequest updateRequest) {
        Course course = new Course();
        course.setTitle(updateRequest.getTitle());
        course.setCreditUnit(updateRequest.getCreditUnit());
        course.setDepartment(updateRequest.getDepartment());

        return course;
    }

    public static UpdateCourseResponse mapUpdate(Course course) {
        UpdateCourseResponse response = new UpdateCourseResponse();
        response.setCourseCode(course.getCourseCode());
        response.setTitle(course.getTitle());
        response.setCreditUnit(course.getCreditUnit());
        response.setDepartment(course.getDepartment());
        response.setMessage("Course update successful");

        return response;
    }

    public static Enrollment map(CreateEnrollmentRequest request) {
        Enrollment enrollment = new Enrollment();
        enrollment.setSession(request.getSession());
        enrollment.setSemester(request.getSemester());
        enrollment.setCourseCodes(request.getCourseCodes());
        enrollment.setStudentId(request.getStudentId());

        return enrollment;
    }

    public static CreateEnrollmentResponse map(Enrollment enrollment) {
        CreateEnrollmentResponse response = new CreateEnrollmentResponse();
        response.setEnrollmentId(enrollment.getEnrollmentId());
        response.setSession(enrollment.getSession());
        response.setSemester(enrollment.getSemester());
        response.setCourseCodes(enrollment.getCourseCodes());
        response.setStudentId(enrollment.getStudentId());
        response.setMessage("Enrolled successfully");

        return response;
    }

    public static AddCourseResponse mapAddCourse(Enrollment enrollment) {
        AddCourseResponse response = new AddCourseResponse();
        response.setEnrollmentId(enrollment.getEnrollmentId());
        response.setSession(enrollment.getSession());
        response.setSemester(enrollment.getSemester());
        response.setCourseCodes(enrollment.getCourseCodes());
        response.setStudentId(enrollment.getStudentId());
        response.setMessage("Course added successfully");

        return response;
    }

    public static RemoveCourseResponse mapRemoveCourse(Enrollment enrollment) {
        RemoveCourseResponse response = new RemoveCourseResponse();
        response.setEnrollmentId(enrollment.getEnrollmentId());
        response.setSession(enrollment.getSession());
        response.setSemester(enrollment.getSemester());
        response.setCourseCodes(enrollment.getCourseCodes());
        response.setStudentId(enrollment.getStudentId());
        response.setMessage("Course removed successfully");

        return response;
    }
}
