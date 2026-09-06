package us.courseEnrollmentsystem.dtos.requests;

import lombok.Data;

@Data
public class CreateCourseRequest {
    private String courseCode;
    private String title;
    private int creditUnit;
    private String department;

}
