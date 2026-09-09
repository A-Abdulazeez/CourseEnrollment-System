package us.courseEnrollmentsystem.dtos.requests;

import lombok.Data;
import us.courseEnrollmentsystem.data.models.Semester;

import java.time.Year;
import java.util.List;

@Data
public class CreateEnrollmentRequest {

    private Integer session;
    private Semester semester;
    private List<String> courseCodes;
    private String studentId;
}
