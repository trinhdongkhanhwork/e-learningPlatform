package edu.cfd.e_learningPlatform.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.entity.Withdraw;
import edu.cfd.e_learningPlatform.enums.WithdrawStatus;
import feign.Param;

public interface WithdrawRepository extends JpaRepository<Withdraw, Long> {
    List<Withdraw> findByStatusAndOtpExpirationTimeBefore(WithdrawStatus status, LocalDateTime expirationTime);

    List<Withdraw> findByUser(User user);

}
