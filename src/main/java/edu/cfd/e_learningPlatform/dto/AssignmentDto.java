package edu.cfd.e_learningPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDto {
    private Long id;
    private String title;
    private String description;
    private String assignmentFileUrl;
    private LocalDateTime dueDate;
    private LocalDateTime submittedAt;
    private Long lectureId;
    private Long userId;
}
