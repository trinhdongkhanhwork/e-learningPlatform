package edu.cfd.e_learningPlatform.controller;


import edu.cfd.e_learningPlatform.dto.request.MessageRequest;
import edu.cfd.e_learningPlatform.dto.request.SendInvatitionRequest;
import edu.cfd.e_learningPlatform.dto.response.MessageFriendResponse;
import edu.cfd.e_learningPlatform.dto.response.MessageResponse;
import edu.cfd.e_learningPlatform.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Clock;
import java.util.List;

@RestController()
@RequestMapping("/message")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessageController {

    MessageService messageService;
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/message/send")
    public void sendMessage(@Payload MessageRequest messageRequest) {
        MessageResponse response = messageService.sendMessage(messageRequest);
        messagingTemplate.convertAndSend("/message/" + response.getFriendId() + "/post", response);
    }

    @PostMapping("/get")
    public ResponseEntity<MessageFriendResponse> getMessagesFriend(@RequestBody SendInvatitionRequest sendInvatitionRequest) {
        return ResponseEntity.ok(messageService.getMessageFriend(sendInvatitionRequest.getIdUser(), sendInvatitionRequest.getIdFriend()));
    }
}
