package edu.cfd.e_learningPlatform.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "userCourseProgress")
public class UserCourseProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private Long sectionId;

    @Column(nullable = false)
    private Long lectureId;

    @Column(nullable = false)
    private Boolean completed = false;

    private Integer timeSpent;

    private Integer score;

    @Column(nullable = false)
    private LocalDateTime progressTimestamp = LocalDateTime.now();
}
