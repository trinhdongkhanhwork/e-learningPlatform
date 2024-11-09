package edu.cfd.e_learningPlatform.repository;


import edu.cfd.e_learningPlatform.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
