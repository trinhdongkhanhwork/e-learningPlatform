package edu.cfd.e_learningPlatform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.cfd.e_learningPlatform.entity.UserCourseProgress;

@Repository
public interface UserCourseProgressRepository extends JpaRepository<UserCourseProgress, Long> {
    Optional<UserCourseProgress> findByUserIdAndCourseIdAndSectionIdAndLectureId(
            String userId, Long courseId, Long sectionId, Long lectureId);

    List<UserCourseProgress> findByUserIdAndCourseId(String userId, Long courseId);

    @Query("SELECT COUNT(l) FROM Lecture l WHERE l.section.course.id = :courseId")
    long countTotalLecturesByCourseId(@Param("courseId") Long courseId);

    @Query(
            "SELECT COUNT(p) FROM UserCourseProgress p WHERE p.userId = :userId AND p.courseId = :courseId AND p.completed = true")
    long countCompletedLecturesByUserAndCourse(@Param("userId") String userId, @Param("courseId") Long courseId);
}
