package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
}