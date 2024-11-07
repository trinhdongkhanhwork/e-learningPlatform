package edu.cfd.e_learningPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureDto {
    private Long id;
    private String title;
    private String type;
    private List<VideoDto> videos;
    private QuizDto quiz;
}
