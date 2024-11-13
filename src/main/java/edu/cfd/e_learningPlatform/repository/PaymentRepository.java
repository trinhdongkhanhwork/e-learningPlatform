package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Payment;
import edu.cfd.e_learningPlatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT SUM(p.price) FROM Payment p WHERE p.user.id = ?1")
    BigDecimal getTotalPaymentsByUserId(String userId);
    Optional<Payment> findByPaymentId(String paymentId);

    List<Payment> findAllByUserId(String userId); // Đảm bảo phương thức này tồn tại
    List<Payment> findByUserAndPaymentStatus_Id(User user, Long paymentStatusId);

    List<Payment> findAllByPaymentStatusId(long l);

    Payment findByUserIdAndCourseId(String userId, Long courseId);
}

