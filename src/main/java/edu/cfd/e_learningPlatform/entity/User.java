package edu.cfd.e_learningPlatform.entity;


import edu.cfd.e_learningPlatform.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
    @Builder.Default
    LocalDateTime updatedDate = LocalDateTime.now();

    @Column(name = "created_date")
    @Builder.Default
    LocalDateTime createdDate = LocalDateTime.now();

    @Version
    @Column(name = "version")
    int version;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role roleEntity;
    private BigDecimal price;

    @OneToMany(mappedBy = "userTo")
    private List<Message> messagesTo;

    @OneToMany(mappedBy = "userFrom")
    private List<Message> messagesFrom;

    @OneToMany(mappedBy = "user")
    private List<Friend> userFriends;

    @OneToMany(mappedBy = "friend")
    private List<Friend> friendUsers;

}
