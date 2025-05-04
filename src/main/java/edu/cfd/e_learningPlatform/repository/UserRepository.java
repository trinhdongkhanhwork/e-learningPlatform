package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("Select u.email from User u where u.id = :id")
    String findEmailById(String id);

    Optional<User> findByRoleEntity_RoleName(String roleName);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r where r.id = :roleID")
    long countUsersByRoleId(long roleID);

    @Query("""
    SELECT DISTINCT u FROM User u
    JOIN u.roles r
    JOIN u.permissions p
    WHERE r.roleName = 'ADMIN' AND p.name <> 'ADMIN_SYSTEM'
""")
    List<User> findAdminsWithPermissions();
}
