package us.courseEnrollmentsystem.dtos.responses;

import lombok.Data;
import us.courseEnrollmentsystem.data.models.Semester;

import java.time.Year;
import java.util.List;

@Data
public class CreateEnrollmentResponse {

    private String enrollmentId;
    private Integer session;
    private Semester semester;
    private List<String> courseCodes;
    private String studentId;
    private String message;
}
