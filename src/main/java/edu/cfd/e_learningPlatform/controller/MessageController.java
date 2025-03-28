package edu.cfd.e_learningPlatform.controller;


import edu.cfd.e_learningPlatform.dto.request.MessageRequest;
import edu.cfd.e_learningPlatform.dto.request.SendInvatitionRequest;
import edu.cfd.e_learningPlatform.dto.response.MessageResponse;
import edu.cfd.e_learningPlatform.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/message")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessageController {

    MessageService messageService;

    @MessageMapping("/message/send")
    @SendTo("/topic/receiveMessage")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest messageRequest) {
        return ResponseEntity.ok(messageService.sendMessage(messageRequest));
    }

    @PostMapping("/get")
    public ResponseEntity<List<MessageResponse>> getMessagesFriend(@RequestBody SendInvatitionRequest sendInvatitionRequest) {
        return ResponseEntity.ok(messageService.getMessageFriend(sendInvatitionRequest.getIdUser(), sendInvatitionRequest.getIdFriend()));
    }
}
