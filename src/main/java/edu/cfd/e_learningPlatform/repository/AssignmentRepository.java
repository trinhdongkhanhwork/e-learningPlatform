package edu.cfd.e_learningPlatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cfd.e_learningPlatform.entity.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Assignment findByLecture_Id(Long id);
}
