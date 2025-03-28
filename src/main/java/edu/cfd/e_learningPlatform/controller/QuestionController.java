package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.entity.Question;
import edu.cfd.e_learningPlatform.service.Impl.QuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionServiceImpl questionService;

    @GetMapping("/{courseId}/{sectionId}/{lectureId}/{quizId}")
    public List<Question> getQuestions(
            @PathVariable Long courseId,
            @PathVariable Long sectionId,
            @PathVariable Long lectureId,
            @PathVariable Long quizId
    ) {
        return questionService.getQuestions(courseId, sectionId, lectureId, quizId);
    }
}
