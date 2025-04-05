package edu.cfd.e_learningPlatform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import edu.cfd.e_learningPlatform.dto.request.CommentRequest;
import edu.cfd.e_learningPlatform.dto.response.CommentVideoResponse;
import edu.cfd.e_learningPlatform.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RestController()
@RequestMapping("/comments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentController {

    CommentService commentService;
    SimpMessagingTemplate messagingTemplate;

    @GetMapping("/get/{idVideo}")
    public ResponseEntity<List<CommentVideoResponse>> fetchComment(@PathVariable Long idVideo) {
        return ResponseEntity.ok(commentService.getCommentVideo(idVideo));
    }

    @MessageMapping("/comments/post")
    public void addComment(@RequestBody CommentRequest commentRequest) {
        CommentVideoResponse response = commentService.addComment(commentRequest);
        messagingTemplate.convertAndSend("/comment/" + commentRequest.getVideoId() + "/private", response);
    }

    @MessageMapping("/comments/update")
    public CommentVideoResponse updateComment(@RequestBody CommentRequest commentRequest) {
        return commentService.updateComment(commentRequest);
    }

    @MessageMapping("/comments/delete")
    public Long deleteComment(@RequestBody CommentVideoResponse commentVideoResponse) {
        commentService.deleteComment(commentVideoResponse.getId());
        return commentVideoResponse.getId();
    }
}
