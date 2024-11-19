package edu.cfd.e_learningPlatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.cfd.e_learningPlatform.entity.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {}
