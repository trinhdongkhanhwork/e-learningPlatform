package edu.cfd.e_learningPlatform.dto.response;

import edu.cfd.e_learningPlatform.dto.LectureDto;
import edu.cfd.e_learningPlatform.dto.QuestionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponse {
    private Long id;
    private String title;
    private LectureDto lectureId;
    private List<QuestionDto> questionsId;
}