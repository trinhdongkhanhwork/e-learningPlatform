package edu.cfd.e_learningPlatform.dto.request;

import lombok.Data;

@Data
public class ProgressUpdateRequest {
    private String userId;
    private Long lectureId;
    private Integer currentSecond;
    private boolean completed;
}
