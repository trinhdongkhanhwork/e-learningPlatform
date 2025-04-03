package edu.cfd.e_learningPlatform.dto.request;

import lombok.Data;

@Data
public class RatingRequest {
    private String userId;
    private Long courseId;
    private Long ratingId;
    private int rating;
    private String comment;
}