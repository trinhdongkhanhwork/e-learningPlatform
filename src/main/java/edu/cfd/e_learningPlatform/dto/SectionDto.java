package edu.cfd.e_learningPlatform.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionDto {
    private Long id;
    private String title;
    private Long courseId;
    private List<LectureDto> lectures = new ArrayList<>();
}
