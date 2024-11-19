package edu.cfd.e_learningPlatform.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import edu.cfd.e_learningPlatform.dto.request.CommentRequest;
import edu.cfd.e_learningPlatform.dto.response.CommentVideoResponse;
import edu.cfd.e_learningPlatform.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

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
