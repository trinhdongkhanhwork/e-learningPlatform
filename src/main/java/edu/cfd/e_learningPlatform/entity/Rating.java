package edu.cfd.e_learningPlatform.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Rating")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    User user;
    @ManyToOne
    @JoinColumn(name = "course_id",nullable = false)
    Course course;
    @Column(name = "rating",nullable = false)
    int rating;
    @Column(name = "comment",nullable = false)
    String comment;
    @Column(name = "create_at",nullable = false)
    LocalDateTime createAt = LocalDateTime.now();
}
