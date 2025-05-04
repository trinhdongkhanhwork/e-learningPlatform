package edu.cfd.e_learningPlatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseProgressResponse {
    private Long courseId;
    private long completedLectures;
    private long totalLectures;
    private double completionPercentage;
    private List<SectionProgress> sections;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SectionProgress {
        private Long sectionId;
        private List<LectureProgress> lectures;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LectureProgress {
        private Long lectureId;
        private Boolean completed;
        private Integer timeSpent;
        private Integer score;
    }
}
