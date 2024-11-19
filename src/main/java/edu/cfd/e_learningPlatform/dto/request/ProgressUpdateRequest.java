package edu.cfd.e_learningPlatform.dto.request;

import lombok.Data;

@Data
public class ProgressUpdateRequest {
    private Long courseId;
    private Long sectionId;
    private Long lectureId;
    private Integer timeSpent;
    private Integer score;
}
