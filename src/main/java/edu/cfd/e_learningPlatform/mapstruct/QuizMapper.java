package edu.cfd.e_learningPlatform.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import edu.cfd.e_learningPlatform.dto.QuizDto;
import edu.cfd.e_learningPlatform.entity.Quiz;

@Mapper(componentModel = "spring")
public interface QuizMapper {
    public static final QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);

    QuizDto quizToQuizDto(Quiz quiz);

    Quiz quizDtoToQuiz(QuizDto quizDto);
}
