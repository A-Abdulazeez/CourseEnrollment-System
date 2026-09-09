package us.courseEnrollmentsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.courseEnrollmentsystem.data.models.Enrollment;
import us.courseEnrollmentsystem.services.EnrollmentService;

import java.util.List;

@RestController
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

}
