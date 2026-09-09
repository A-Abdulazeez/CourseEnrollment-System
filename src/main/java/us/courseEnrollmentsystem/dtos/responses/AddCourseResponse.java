package us.courseEnrollmentsystem.dtos.responses;

import lombok.Data;
import us.courseEnrollmentsystem.data.models.Semester;

import java.util.List;

@Data
public class AddCourseResponse {
    private String enrollmentId;
    private Integer session;
    private Semester semester;
    private List<String> courseCodes;
    private String studentId;
    private String message;
}
