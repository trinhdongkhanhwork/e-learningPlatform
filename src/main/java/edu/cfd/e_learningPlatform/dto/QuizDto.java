package edu.cfd.e_learningPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto {
    private Long id;
    private String title;
    private Long lectureId;
    private List<QuestionDto> questions;
}
