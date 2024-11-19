package edu.cfd.e_learningPlatform.controller;

import org.springframework.web.bind.annotation.*;

import edu.cfd.e_learningPlatform.dto.request.ProgressUpdateRequest;
import edu.cfd.e_learningPlatform.dto.response.ApiResponse;
import edu.cfd.e_learningPlatform.service.UserCourseProgressService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user-progress")
@RequiredArgsConstructor
public class UserCourseProgressController {

    private final UserCourseProgressService progressService;

    @PostMapping("/{userId}")
    public ApiResponse<Void> updateProgress(@PathVariable String userId, @RequestBody ProgressUpdateRequest request) {
        try {
            progressService.updateProgress(userId, request);
            return ApiResponse.<Void>builder()
                    .message("Progress updated successfully.")
                    .build();
        } catch (IllegalArgumentException e) {
            return ApiResponse.<Void>builder()
                    .code(4000)
                    .message(e.getMessage())
                    .build();
        }
    }
}
