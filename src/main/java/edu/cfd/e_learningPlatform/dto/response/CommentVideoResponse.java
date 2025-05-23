package edu.cfd.e_learningPlatform.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentVideoResponse {
    private Long id;
    private String idUserComment;
    private String fullnameUserComment;
    private String avatarUserComment;
    private String commentText;
    private Long idVideo;
    private LocalDateTime createdAt;
    private Long idCommentParent;
    private String fullnameUserCommentParent;
}
