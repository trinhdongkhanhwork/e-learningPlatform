package edu.cfd.e_learningPlatform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cfd.e_learningPlatform.entity.PaymentStatus;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
    Optional<PaymentStatus> findByStatusName(String statusName);
}
