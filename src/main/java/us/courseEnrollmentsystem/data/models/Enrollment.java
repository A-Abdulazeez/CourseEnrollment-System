package us.courseEnrollmentsystem.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Enrollment {
    @Id
    private String enrollmentId;

    private Integer session;
    private Semester semester;
    private List<String> courseCodes;
    private String studentId;
}
