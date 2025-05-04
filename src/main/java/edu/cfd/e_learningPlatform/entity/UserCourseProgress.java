package edu.cfd.e_learningPlatform.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_course_progress",
        indexes = {@Index(name = "idx_user_course", columnList = "user_id, course_id")})
public class UserCourseProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private Long sectionId;

    @Column(nullable = false)
    private Long lectureId;

    @Enumerated(EnumType.STRING)
    private ProgressStatus status = ProgressStatus.NOT_STARTED;

    private Integer timeSpent;

    private Integer score;

    @Column(nullable = false)
    private LocalDateTime progressTimestamp = LocalDateTime.now();

    public enum ProgressStatus {
        NOT_STARTED, IN_PROGRESS, COMPLETED
    }
}
