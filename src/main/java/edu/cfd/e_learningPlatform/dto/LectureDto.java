package edu.cfd.e_learningPlatform.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
