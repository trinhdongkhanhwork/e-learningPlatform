package edu.cfd.e_learningPlatform.controller;

import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import edu.cfd.e_learningPlatform.dto.CourseDto;
import edu.cfd.e_learningPlatform.dto.request.CourseCreationRequest;
import edu.cfd.e_learningPlatform.dto.response.ApiResponse;
import edu.cfd.e_learningPlatform.dto.response.CourseResponse;
import edu.cfd.e_learningPlatform.service.CourseService;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseCreationRequest courseCreationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(courseCreationRequest));
    }

    @GetMapping
    public ResponseEntity<Page<CourseResponse>> getAllCourses(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(courseService.getAllCourses(page, size));
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(Map.of("message", "Course " + id + " deleted successfully"));
    }

    @GetMapping("/getCourseById/{id}")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/getCourseByIdForLoad/{id}")
    public ApiResponse<CourseDto> getCourseByIdForLoad(@PathVariable Long id) {
        CourseDto courseDto = courseService.getCourseByIdForLoad(id);
        return ApiResponse.<CourseDto>builder().result(courseDto).build();
    }
}
