package us.courseEnrollmentsystem.dtos.responses;

import lombok.Data;

@Data
public class CreateCourseResponse {
    private String courseCode;
    private String title;
    private int creditUnit;
    private String department;
    private String message;
}
