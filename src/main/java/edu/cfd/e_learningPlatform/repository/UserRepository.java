package edu.cfd.e_learningPlatform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.cfd.e_learningPlatform.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("Select u.email from User u where u.id = :id")
    String findEmailById(String id);

    Optional<User> findByRoleEntity_RoleName(String roleName);

    @Query("SELECT COUNT(u) FROM User u WHERE u.roleEntity.id = 1")
    long countUsersByRoleId();


}
