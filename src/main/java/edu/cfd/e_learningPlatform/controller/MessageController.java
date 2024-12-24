package edu.cfd.e_learningPlatform.controller;

import java.util.List;

import edu.cfd.e_learningPlatform.dto.request.AssemblyUserRequest;
import edu.cfd.e_learningPlatform.dto.response.AssemblyUserResponse;
import edu.cfd.e_learningPlatform.dto.response.EnrollCourseResponse;
import edu.cfd.e_learningPlatform.service.AssemblyService;
import edu.cfd.e_learningPlatform.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import edu.cfd.e_learningPlatform.dto.request.MessageRequest;
import edu.cfd.e_learningPlatform.dto.response.MessageResponse;
import edu.cfd.e_learningPlatform.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController()
@RequestMapping("/messages")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessageController {
    MessageService messageService;

    @GetMapping("/group/{id}")
    public ResponseEntity<List<MessageResponse>> getMessagesToGroup(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.getMessageToGroup(id));
    }

    @MessageMapping("/sendToGroup")
    @SendTo("/topic/message")
    public ResponseEntity<MessageResponse> createMessage(@RequestBody MessageRequest messageRequest) {
        return ResponseEntity.ok(messageService.sendMessageToGroup(messageRequest));
    }

    @MessageMapping("/updateMessage")
    @SendTo("/topic/message")
    public ResponseEntity<MessageResponse> updateMessage(@RequestBody MessageRequest messageRequest) {
        return ResponseEntity.ok(messageService.updateMessageToGroup(messageRequest));
    }

    @MessageMapping("/deleteMessage")
    @SendTo("/topic/deleteMessage")
    public Long deleteMessage(@RequestBody MessageRequest messageRequest) {
        return messageService.deleteMessage(messageRequest.getId());
    }
}
