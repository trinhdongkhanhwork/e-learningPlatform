package edu.cfd.e_learningPlatform.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;

import edu.cfd.e_learningPlatform.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", length = 255)
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "username", length = 255, nullable = false)
    String username;

    @Column(name = "password", length = 255, nullable = false)
    String password;

    @Column(name = "email", columnDefinition = "TEXT", nullable = false)
    String email;

    @Column(name = "fullname", length = 255)
    String fullname;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    Date birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;

    @Column(name = "phone", length = 10)
    String phone;

    @Column(name = "avatar_url", length = 255)
    String avatarUrl;

    @Column(name = "updated_date")
    LocalDateTime updatedDate;

    @Column(name = "created_date")
    LocalDateTime createdDate;

    @Version
    @Column(name = "version")
    int version;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role roleEntity;

    private BigDecimal price;

}