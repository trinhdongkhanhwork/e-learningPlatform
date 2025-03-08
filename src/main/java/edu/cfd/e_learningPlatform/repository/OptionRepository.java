package edu.cfd.e_learningPlatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.cfd.e_learningPlatform.entity.Option;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    @Query("SELECT o FROM Option o WHERE o.question.quiz.id = :quizId AND o.correct = true")
    List<Option> findCorrectOptionsByQuizId(Long quizId);
}
