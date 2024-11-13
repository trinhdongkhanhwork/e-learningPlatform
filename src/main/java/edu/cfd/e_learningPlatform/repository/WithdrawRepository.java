package edu.cfd.e_learningPlatform.repository;


import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.entity.Withdraw;
import edu.cfd.e_learningPlatform.enums.WithdrawStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawRepository extends JpaRepository<Withdraw,Long> {
    List<Withdraw> findByUser(User user);

    List<Withdraw> findByStatus(WithdrawStatus withdrawStatus);
    List<Withdraw> findByUser_IdAndStatus(String userId, WithdrawStatus status);
}
