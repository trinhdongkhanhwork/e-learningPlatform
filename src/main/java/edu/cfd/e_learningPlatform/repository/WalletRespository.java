package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRespository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUser(User user);
}
