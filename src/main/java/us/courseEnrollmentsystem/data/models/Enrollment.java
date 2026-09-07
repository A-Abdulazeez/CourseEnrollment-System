package us.courseEnrollmentsystem.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Year;
import java.util.List;

@Data
public class Enrollment {
    @Id
    private String enrollmentId;

    private Year session;
    private Semester semester;
    private List<String> courseCodes;
    private String studentId;
}
