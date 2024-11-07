package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
