package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Assignment findByLecture_Id(Long id);
}