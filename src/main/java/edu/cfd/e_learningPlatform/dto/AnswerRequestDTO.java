package edu.cfd.e_learningPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerRequestDTO {
    private Long quizId;
    private String userId;
    private List<UserAnswerDTO> answers;
}
