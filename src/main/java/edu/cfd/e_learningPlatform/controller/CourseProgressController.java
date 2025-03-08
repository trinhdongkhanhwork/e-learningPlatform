package edu.cfd.e_learningPlatform.controller;

import org.springframework.web.bind.annotation.*;

import edu.cfd.e_learningPlatform.dto.response.ApiResponse;
import edu.cfd.e_learningPlatform.dto.response.CourseProgressResponse;
import edu.cfd.e_learningPlatform.service.CourseProgressService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/course-progress")
@RequiredArgsConstructor
public class CourseProgressController {

    private final CourseProgressService progressService;

    @GetMapping("/{courseId}/{userId}")
    public ApiResponse<CourseProgressResponse> getProgress(@PathVariable String userId, @PathVariable Long courseId) {
        try {
            CourseProgressResponse progress = progressService.getCourseProgress(userId, courseId);
            return ApiResponse.<CourseProgressResponse>builder()
                    .message("Course progress retrieved successfully.")
                    .result(progress)
                    .build();
        } catch (IllegalArgumentException e) {
            return ApiResponse.<CourseProgressResponse>builder()
                    .code(4000)
                    .message(e.getMessage())
                    .build();
        }
    }
}
