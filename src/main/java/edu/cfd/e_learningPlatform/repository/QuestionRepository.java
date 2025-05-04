package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q " +
            "JOIN q.quiz quiz " +
            "JOIN quiz.lecture lecture " +
            "JOIN lecture.section section " +
            "JOIN section.course course " +
            "WHERE course.id = :courseId " +
            "AND section.id = :sectionId " +
            "AND lecture.id = :lectureId " +
            "AND quiz.id = :quizId")
    List<Question> findQuestionsByCourseSectionLectureQuiz(
            @Param("courseId") Long courseId,
            @Param("sectionId") Long sectionId,
            @Param("lectureId") Long lectureId,
            @Param("quizId") Long quizId
    );
}
