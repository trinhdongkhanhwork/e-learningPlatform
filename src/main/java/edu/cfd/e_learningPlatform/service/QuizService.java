package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.QuizDto;

public interface QuizService {
    QuizDto findByLectureId(Long lectureId);
}
