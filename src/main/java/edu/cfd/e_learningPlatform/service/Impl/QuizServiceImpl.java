package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.QuizDto;

import edu.cfd.e_learningPlatform.mapstruct.QuizMapper;
import edu.cfd.e_learningPlatform.repository.QuizReponsitory;
import edu.cfd.e_learningPlatform.service.QuizService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    QuizReponsitory quizReponsitory;
    QuizMapper quizMapper;
    @Override
    public QuizDto findByLectureId(Long lectureId) {
        return quizMapper.quizToQuizDto(quizReponsitory.findByLecture_Id(lectureId));
    }
}