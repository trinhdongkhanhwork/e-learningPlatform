package edu.cfd.e_learningPlatform.repository;


import edu.cfd.e_learningPlatform.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Object> findByPaymentId(String transactionNo);

    long countByCourseIdAndEnrollmentTrue(Long courseId);

    @Query("select p from Payment p " +
            "where p.user.id = :userId and p.course.id = :idCourse and p.enrollment = true")
    Payment findByPaymentisErollment(Long idCourse, String userId);
}
