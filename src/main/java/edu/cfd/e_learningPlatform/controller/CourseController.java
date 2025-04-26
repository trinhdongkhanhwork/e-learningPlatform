package edu.cfd.e_learningPlatform.controller;

import java.util.List;
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
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseCreationRequest courseCreationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(courseCreationRequest));
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long courseId,
            @RequestBody CourseCreationRequest courseCreationRequest) {
        CourseResponse updatedCourse = courseService.updateCourse(courseId, courseCreationRequest);
        return ResponseEntity.ok(updatedCourse);
    }

    @GetMapping
    public ResponseEntity<Page<CourseResponse>> getAllCourses(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size) {
        return ResponseEntity.ok(courseService.getAllCourses(page, size));
    }

    // Cập nhật khóa học, thay đổi trạng thái pendingDelete thay vì xóa
    @PutMapping("/{id}/pending-delete")
    public ResponseEntity<Void> markCourseForDeletion(@PathVariable Long id) {
        courseService.markForDeletion(id);
        return ResponseEntity.noContent().build();
    }

    // Xóa khóa học thực tế khi admin duyệt xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getCourseById/{id}")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable Long id){
        return ResponseEntity.ok(courseService.getCourseById(id));
    }


    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<CourseResponse>> getCoursesByCategoryId(@PathVariable Long categoryId){
        List<CourseResponse> courses = courseService.getCoursesByCategoryId(categoryId);
        return ResponseEntity.ok(courses);
    }
}
