package edu.cfd.e_learningPlatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.cfd.e_learningPlatform.entity.Answer;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByUser_IdAndLecture_Id(String userId, Long lectureId);
}
