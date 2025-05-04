package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.response.QuizResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.entity.Question;
import edu.cfd.e_learningPlatform.entity.Quiz;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import edu.cfd.e_learningPlatform.dto.QuizDto;
import edu.cfd.e_learningPlatform.mapstruct.QuizMapper;
import edu.cfd.e_learningPlatform.repository.QuizRepository;
import edu.cfd.e_learningPlatform.service.QuizService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    QuizMapper quizMapper;
    QuizRepository quizRepository;

    @Override
    public QuizDto findByLectureId(Long lectureId) {
        return quizMapper.quizToQuizDto(quizRepository.findByLecture_Id(lectureId));
    }

    public List<QuizResponse> getQuiz() {
        return quizRepository.findAll().stream().map(quizMapper::toQuizResponse).toList();
    }

    public List<Quiz> getQuestionsByQuiz(Long courseId, Long sectionId, Long lectureId, Long quizId) {
        return quizRepository.findByLecture_Section_Course_IdAndLecture_Section_IdAndLecture_IdAndId(
                courseId, sectionId, lectureId, quizId
        );
    }
}
