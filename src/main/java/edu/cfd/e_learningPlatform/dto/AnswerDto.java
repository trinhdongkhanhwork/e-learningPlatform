package edu.cfd.e_learningPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private Long lectureId;
    private String userId;
    private List<Long> optionId;
    private boolean passedLecture;
    private LocalDateTime createdAt;
    private Integer score;
    private LocalDateTime progressTimestamp;
}
