package edu.cfd.e_learningPlatform.dto.response;

import edu.cfd.e_learningPlatform.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoInlectureResponse {
    private Long idVideo;
    private String videoUrl;
    private List<CommentVideoResponse> listComment;
}