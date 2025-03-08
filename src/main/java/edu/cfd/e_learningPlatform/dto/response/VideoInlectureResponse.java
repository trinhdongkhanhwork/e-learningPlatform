package edu.cfd.e_learningPlatform.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoInlectureResponse {
    private Long idVideo;
    private String videoUrl;
    private List<CommentVideoResponse> listComment;
}
