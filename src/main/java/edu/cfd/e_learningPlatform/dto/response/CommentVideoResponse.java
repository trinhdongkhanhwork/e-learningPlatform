package edu.cfd.e_learningPlatform.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentVideoResponse {
    private Long id;
    private String fullName;
    private String idUserComment;
    private String profilePicture;
    private String commentText;
    private String nameUserParent;
    private Long parentId;
}
