package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.entity.Withdraw;
import edu.cfd.e_learningPlatform.enums.WithdrawStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WithdrawRepository extends JpaRepository<Withdraw, Long> {
    List<Withdraw> findByStatusAndOtpExpirationTimeBefore(WithdrawStatus status, LocalDateTime expirationTime);

    List<Withdraw> findByUser(User user);

}
