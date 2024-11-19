package edu.cfd.e_learningPlatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.cfd.e_learningPlatform.entity.Lecture;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Lecture findById(long id);
}
