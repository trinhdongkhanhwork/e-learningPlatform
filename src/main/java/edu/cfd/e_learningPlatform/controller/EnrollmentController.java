package edu.cfd.e_learningPlatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.cfd.e_learningPlatform.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmEnrollment(
            @RequestParam("paymentId") String paymentId, @RequestParam("courseId") Long courseId) {
        enrollmentService.confirmEnrollment(paymentId, courseId);
        return ResponseEntity.ok("Enrollment confirmed successfully");
    }
}
