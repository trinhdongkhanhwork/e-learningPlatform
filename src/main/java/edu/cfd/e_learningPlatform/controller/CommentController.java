package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.CommentRequest;
import edu.cfd.e_learningPlatform.dto.response.CommentVideoResponse;
import edu.cfd.e_learningPlatform.entity.Comment;
import edu.cfd.e_learningPlatform.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

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
        messagingTemplate.convertAndSend("/comment/" + response.getIdVideo() + "/post", response);
    }

    @MessageMapping("/comments/update")
    public void updateComment(@RequestBody CommentRequest commentRequest) {
        CommentVideoResponse response = commentService.updateComment(commentRequest);
        messagingTemplate.convertAndSend("/comment/" + response.getIdVideo() + "/put", response);
    }

    @MessageMapping("/comments/delete")
    public void deleteComment(Long idComment) {
        Comment response = commentService.deleteComment(idComment);
        messagingTemplate.convertAndSend("/comment/" + response.getVideo().getId() + "/delete", response.getId());
    }
}
