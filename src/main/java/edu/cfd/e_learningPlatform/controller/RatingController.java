package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.RatingRequest;
import edu.cfd.e_learningPlatform.dto.response.RatingResponse;
import edu.cfd.e_learningPlatform.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    // Tạo bình luận
    @PostMapping
    public ResponseEntity<RatingResponse> createRating(@RequestBody RatingRequest request) {
        RatingResponse response = ratingService.createRating(request);
        return ResponseEntity.ok(response);
    }

    // Sửa bình luận
    @PutMapping
    public ResponseEntity<RatingResponse> updateRating(@RequestBody RatingRequest request) {
        RatingResponse response = ratingService.updateRating(request);
        return ResponseEntity.ok(response);
    }

    // Xóa bình luận
    @DeleteMapping
    public ResponseEntity<Void> deleteRating(@RequestBody RatingRequest request) {
        ratingService.deleteRating(request);
        return ResponseEntity.noContent().build();
    }

    // Xem tất cả bình luận của khóa học
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<RatingResponse>> getRatingsByCourse(@PathVariable Long courseId) {
        List<RatingResponse> ratings = ratingService.getRatingsByCourseId(courseId);
        return ResponseEntity.ok(ratings);
    }

    // Tính trung bình rating
    @GetMapping("/course/{courseId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long courseId) {
        Double averageRating = ratingService.getAverageRatingByCourseId(courseId);
        return ResponseEntity.ok(averageRating);
    }
}
