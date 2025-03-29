package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.response.ApiResponse;
import edu.cfd.e_learningPlatform.dto.response.QuizResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.entity.Question;
import edu.cfd.e_learningPlatform.entity.Quiz;
import edu.cfd.e_learningPlatform.service.Impl.QuizService;
import edu.cfd.e_learningPlatform.service.Impl.QuizServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    @Autowired
    private QuizServiceImpl quizService;

//    @GetMapping("/{quizId}/questions")
//    public ResponseEntity<List<Question>> getQuizQuestions(@PathVariable Long quizId) {
//        return quizService.getQuizById(quizId)
//                .map(quiz -> ResponseEntity.ok(quiz.getQuestions()))
//                .orElse(ResponseEntity.notFound().build());
//    }

    @GetMapping
    ApiResponse<List<QuizResponse>> getQuiz() {
        return ApiResponse.<List<QuizResponse>>builder()
                .result(quizService.getQuiz())
                .build();
    }

    @GetMapping("/questions/{courseId}/{sectionId}/{lectureId}/{quizId}")
    public ResponseEntity<List<Quiz>> getQuestions(
            @PathVariable Long courseId,
            @PathVariable Long sectionId,
            @PathVariable Long lectureId,
            @PathVariable Long quizId
    ) {
        List<Quiz> questions = quizService.getQuestionsByQuiz(courseId, sectionId, lectureId, quizId);
        return questions.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList())
                : ResponseEntity.ok(questions);
    }
}
