package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.TransactionPayment;
import edu.cfd.e_learningPlatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionPaymentRepository extends JpaRepository<TransactionPayment, Long> {
    List<TransactionPayment> findByUserAndTypeIn(User user, List<String> types);
    List<TransactionPayment> findByUserAndTypeInAndCreatedAtBetween(
            User user,
            List<String> types,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
    List<TransactionPayment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
