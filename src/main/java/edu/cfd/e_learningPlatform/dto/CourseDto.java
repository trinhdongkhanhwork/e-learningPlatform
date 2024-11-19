package edu.cfd.e_learningPlatform.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;
    private String title;
    private List<SectionDto> sections;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionDto {
        private Long id;
        private String title;
        private List<LectureDto> lectures;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LectureDto {
        private Long id;
        private String title;
        private String type;
        private String content;
        private boolean completed;
    }
}
