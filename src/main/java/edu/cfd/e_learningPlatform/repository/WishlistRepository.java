package edu.cfd.e_learningPlatform.repository;


import edu.cfd.e_learningPlatform.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    List<Wishlist> findByUserId(String userId);
}
