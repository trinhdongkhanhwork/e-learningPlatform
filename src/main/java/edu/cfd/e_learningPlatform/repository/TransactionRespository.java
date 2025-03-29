package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.dto.response.EarningsSummaryResponse;
import edu.cfd.e_learningPlatform.entity.Transactions;
import edu.cfd.e_learningPlatform.entity.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRespository extends JpaRepository<Transactions,Long> {
    List<Transactions> findByUser(User user);
    List<Transactions> findByUserAndType(User user, String type);
}
