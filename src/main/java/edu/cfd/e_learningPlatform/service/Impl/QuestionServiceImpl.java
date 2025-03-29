package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.entity.Question;
import edu.cfd.e_learningPlatform.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getQuestions(Long courseId, Long sectionId, Long lectureId, Long quizId) {
        return questionRepository.findQuestionsByCourseSectionLectureQuiz(courseId, sectionId, lectureId, quizId);
    }
}
