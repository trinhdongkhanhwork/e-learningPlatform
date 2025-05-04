package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRpository extends JpaRepository<Cart,Long> {
    List<Cart> findByUserId(String userId);
}
