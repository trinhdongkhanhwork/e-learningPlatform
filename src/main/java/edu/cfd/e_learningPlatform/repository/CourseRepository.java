package edu.cfd.e_learningPlatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.cfd.e_learningPlatform.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE c.title = ?1")
    Course findByCourseName(String courseName);

    long countByCategoryId(Long categoryId);

    List<Course> findByInstructorId(String instructorId);

    List<Course> findByCategoryId(Long categoryId);
}
