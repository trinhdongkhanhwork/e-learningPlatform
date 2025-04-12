package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.dto.response.EarningsSummaryResponse;
import edu.cfd.e_learningPlatform.entity.Transactions;
import edu.cfd.e_learningPlatform.entity.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRespository extends JpaRepository<Transactions,Long> {
    List<Transactions> findByTypeIn(List<String> types);
    List<Transactions> findByUserAndTypeIn(User user, List<String> types);
    List<Transactions> findByUserAndTypeInAndCreatedAtBetween(
            User user,
            List<String> types,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
    List<Transactions> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
