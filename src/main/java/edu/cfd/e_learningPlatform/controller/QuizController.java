package edu.cfd.e_learningPlatform.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import edu.cfd.e_learningPlatform.dto.response.ApiResponse;
import edu.cfd.e_learningPlatform.dto.response.QuizResultResponse;
import edu.cfd.e_learningPlatform.repository.QuizRepository;
import edu.cfd.e_learningPlatform.service.QuizServiceForGrading;
import edu.cfd.e_learningPlatform.service.UserCourseProgressService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizServiceForGrading quizServiceForGrading;
    private final UserCourseProgressService progressService;
    private static final int PASS_SCORE = 85;
    private final QuizRepository quizRepository;

    @PostMapping("/submit/{quizId}")
    public ApiResponse<QuizResultResponse> submitQuiz(
            @PathVariable Long quizId, @RequestParam String userId, @RequestBody List<Long> selectedOptionIds) {
        int score = quizServiceForGrading.calculateScore(quizId, userId, selectedOptionIds);
        boolean completed = score >= PASS_SCORE;

        return ApiResponse.<QuizResultResponse>builder()
                .message("Quiz submitted successfully.")
                .result(new QuizResultResponse(score, completed))
                .build();
    }
}
