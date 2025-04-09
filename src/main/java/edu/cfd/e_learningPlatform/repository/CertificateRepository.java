package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Certificate;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByUser(User user);
    boolean existsByUserAndCourse(User user, Course course);
}
