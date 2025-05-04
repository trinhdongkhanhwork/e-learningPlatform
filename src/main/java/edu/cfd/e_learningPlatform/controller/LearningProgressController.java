package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.ProgressUpdateRequest;
import edu.cfd.e_learningPlatform.dto.response.FirstIncompleteLectureResponse;
import edu.cfd.e_learningPlatform.service.Impl.LearningProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class LearningProgressController {

    private final LearningProgressService progressService;

    @PostMapping("/update")
    public ResponseEntity<?> updateProgress(@RequestBody ProgressUpdateRequest request) {
        progressService.updateProgress(request);
        return ResponseEntity.ok("Progress updated successfully");
    }

    @GetMapping("/first-incomplete/{id}")
    public ResponseEntity<FirstIncompleteLectureResponse> getFirstIncompleteLecture(@PathVariable Long id) {
        FirstIncompleteLectureResponse response = progressService.getFirstIncompleteLecture(id);
        return ResponseEntity.ok(response);
    }
}
