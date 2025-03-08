
package edu.cfd.e_learningPlatform.repository;


import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.Payment;
import edu.cfd.e_learningPlatform.entity.PaymentStatus;
import edu.cfd.e_learningPlatform.entity.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPaymentId(String paymentId);

    long countByCourseIdAndEnrollmentTrue(Long courseId);

    @Query("select p from Payment p where p.user.id = :userId and p.course.id = :idCourse and p.enrollment = true")
    Payment findByPaymentisErollment(Long idCourse, String userId);

    @Query("SELECT SUM(p.price) FROM Payment p WHERE p.course.id = :courseId AND p.paymentStatus.id = 1") // chỉ tính thanh toán thành công
    BigDecimal sumPriceByCourseId(@Param("courseId") Long courseId);

    List<Payment> findByUser_Id(String userId);

    List<Payment> findAllByPaymentStatusId(long l);

    List<Payment> findByUserIdAndCourseIdAndPaymentStatusId(String userId, Long courseId, Long paymentStatusId);

    List<Payment> findByUserIdAndCourseId(String userId, Long courseId);
}
