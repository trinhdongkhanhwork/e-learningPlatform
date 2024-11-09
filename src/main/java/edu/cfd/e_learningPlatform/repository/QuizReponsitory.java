package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizReponsitory extends JpaRepository<Quiz, Long> {

    Quiz findByLecture_Id(Long id);
}