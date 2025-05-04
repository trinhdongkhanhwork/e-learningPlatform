package edu.cfd.e_learningPlatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
