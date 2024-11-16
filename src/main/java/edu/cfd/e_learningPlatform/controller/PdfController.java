package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.response.CourseResponse;
import edu.cfd.e_learningPlatform.service.CourseService;
import edu.cfd.e_learningPlatform.utils.PdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/courses")
public class PdfController {

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private CourseService courseService;  // Service to get the course from DB

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadCoursePdf(@PathVariable Long id) {
        try {
            // Retrieve course information from the database
            CourseResponse course = courseService.getCourseById(id);  // Assume you have a method to get course by ID

            // Generate the PDF
            byte[] pdfContent = pdfGenerator.generateCoursePdf(course);

            // Return the PDF as a downloadable file
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/pdf");
            headers.add("Content-Disposition", "attachment; filename=course_" + course.getId() + ".pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Handle error during PDF generation
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
