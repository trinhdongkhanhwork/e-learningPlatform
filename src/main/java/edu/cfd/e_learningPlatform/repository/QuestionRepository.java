package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
