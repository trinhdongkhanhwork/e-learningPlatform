package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.CommentRequest;
import edu.cfd.e_learningPlatform.dto.response.CommentVideoResponse;
import edu.cfd.e_learningPlatform.dto.response.VideoInlectureResponse;
import edu.cfd.e_learningPlatform.service.CommentService;
import edu.cfd.e_learningPlatform.service.VideoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/comments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentController {

    CommentService commentService;

    @MessageMapping("/comments/post")
    @SendTo("/topic/comments")
    public CommentVideoResponse addComment(@RequestBody CommentRequest commentRequest) {
        return commentService.addComment(commentRequest);
    }

    @MessageMapping("/comments/update")
    @SendTo("/topic/comments")
    public CommentVideoResponse updateComment(@RequestBody CommentRequest commentRequest) {
        return commentService.updateComment(commentRequest);
    }

    @MessageMapping("/comments/delete")
    @SendTo("/topic/idComment")
    public Long deleteComment(@RequestBody CommentVideoResponse commentVideoResponse) {
        commentService.deleteComment(commentVideoResponse.getId());
        return commentVideoResponse.getId();
    }
}