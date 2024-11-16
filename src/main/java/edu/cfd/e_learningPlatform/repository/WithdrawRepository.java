package edu.cfd.e_learningPlatform.repository;


import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.entity.Withdraw;
import edu.cfd.e_learningPlatform.enums.WithdrawStatus;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface WithdrawRepository extends JpaRepository<Withdraw,Long> {
    List<Withdraw> findByUser(User user);

    List<Withdraw> findByStatus(WithdrawStatus withdrawStatus);
    @Query("SELECT SUM(w.price) FROM Withdraw w WHERE w.user.id = :userId AND w.status = :status")
    BigDecimal sumAmountByUserIdAndStatus(@Param("userId") String userId, @Param("status") WithdrawStatus status);

}
