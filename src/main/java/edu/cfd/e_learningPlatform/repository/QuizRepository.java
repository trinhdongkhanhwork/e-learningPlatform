package edu.cfd.e_learningPlatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cfd.e_learningPlatform.entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Quiz findByLecture_Id(Long id);
}
