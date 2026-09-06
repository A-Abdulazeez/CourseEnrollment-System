package us.courseEnrollmentsystem.dtos.requests;

import lombok.Data;

@Data
public class UpdateStudentRequest {
    private String name;
    private String password;
    private String department;
}
