package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.RatingRequest;
import edu.cfd.e_learningPlatform.dto.response.RatingResponse;

import java.util.List;

public interface RatingService {
    RatingResponse createRating(RatingRequest request);
    RatingResponse updateRating(RatingRequest request);
    void deleteRating(RatingRequest request);
    List<RatingResponse> getRatingsByCourseId(Long courseId);
    Double getAverageRatingByCourseId(Long courseId);
}
