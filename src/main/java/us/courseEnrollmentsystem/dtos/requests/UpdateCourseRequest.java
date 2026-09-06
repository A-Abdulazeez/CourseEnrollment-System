package us.courseEnrollmentsystem.dtos.requests;

import lombok.Data;

@Data
public class UpdateCourseRequest {
    private String title;
    private int creditUnit;
    private String department;
}
