package edu.cfd.e_learningPlatform.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {
    private Long id;
    private int points;
    private String type;
    private String title;
    private List<OptionDto> options;
}
