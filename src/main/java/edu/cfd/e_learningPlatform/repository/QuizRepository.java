package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Quiz findByLecture_Id(Long id);
}
