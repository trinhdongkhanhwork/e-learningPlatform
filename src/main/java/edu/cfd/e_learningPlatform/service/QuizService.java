package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.QuizDto;
import edu.cfd.e_learningPlatform.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizService {
    QuizDto findByLectureId(Long lectureId);
}