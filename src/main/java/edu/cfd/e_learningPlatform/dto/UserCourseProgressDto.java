package edu.cfd.e_learningPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCourseProgressDto {
    private Long id;
    private Long userId;
    private Long courseId;
    private Long sectionId;
    private Long lectureId;
    private String status;
    private Integer timeSpent;
    private Integer score;
    private LocalDateTime progressTimestamp;
}
