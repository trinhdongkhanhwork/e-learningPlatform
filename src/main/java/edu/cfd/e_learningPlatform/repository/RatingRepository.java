package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Rating;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating,Long> {
    List<Rating> findByCourseId(Long courseId);
    Optional<Rating> findByIdAndUserId(Long id, String userId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Rating r WHERE r.course.id = :courseId")
    Double findAverageRatingByCourseId(@Param("courseId") Long courseId);
}
