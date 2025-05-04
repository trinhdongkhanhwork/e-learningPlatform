package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Quiz findByLecture_Id(Long lectureId);

    List<Quiz> findByLecture_Section_Course_IdAndLecture_Section_IdAndLecture_IdAndId(
            Long courseId, Long sectionId, Long lectureId, Long quizId
    );
}

