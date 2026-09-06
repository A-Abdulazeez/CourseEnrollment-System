package us.courseEnrollmentsystem.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Course {

    @Id
    private String courseCode;
    private String title;
    private int creditUnit;
    private String department;

}
