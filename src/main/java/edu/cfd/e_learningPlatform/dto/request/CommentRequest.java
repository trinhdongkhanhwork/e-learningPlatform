package edu.cfd.e_learningPlatform.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private Long id;
    private String userId;
    private String commentText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long videoId;
    private Long parentId;
}
