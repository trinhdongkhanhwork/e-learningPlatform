package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.LearningProgress;
import edu.cfd.e_learningPlatform.entity.Lecture;
import edu.cfd.e_learningPlatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LearningProgressRepository extends JpaRepository<LearningProgress, Long> {
    Optional<LearningProgress> findByUserAndLecture(User user, Lecture lecture);
}
