package edu.cfd.e_learningPlatform.service;

import java.util.List;

import edu.cfd.e_learningPlatform.dto.request.CommentRequest;
import edu.cfd.e_learningPlatform.dto.response.CommentVideoResponse;
import edu.cfd.e_learningPlatform.entity.Comment;

public interface CommentService {
    List<CommentVideoResponse> getCommentVideo(Long idVideo);

    CommentVideoResponse addComment(CommentRequest commentRequest);

    CommentVideoResponse updateComment(CommentRequest commentRequest);

    Comment deleteComment(Long idComment);
}
