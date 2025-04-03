package edu.cfd.e_learningPlatform.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingResponse {
    private Long id;
    private Long courseId;
    private String userId;
    private String fullname;
    private int rating;
    private String comment;
    private LocalDateTime createAt;
}