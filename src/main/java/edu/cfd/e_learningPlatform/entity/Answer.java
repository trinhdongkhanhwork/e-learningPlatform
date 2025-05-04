package edu.cfd.e_learningPlatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer score;
    private LocalDateTime progressTimestamp;
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean passedLecture = false;
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isCorrect = false;
    private LocalDateTime createdAt = LocalDateTime.now();

}
