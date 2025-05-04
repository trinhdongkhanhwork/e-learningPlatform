package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.entity.Quiz;
import edu.cfd.e_learningPlatform.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    public Optional<Quiz> getQuizById(Long quizId) {
        return quizRepository.findById(quizId);
    }
}
