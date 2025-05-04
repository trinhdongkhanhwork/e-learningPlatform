package edu.cfd.e_learningPlatform.controller;


import edu.cfd.e_learningPlatform.dto.request.SendInvatitionRequest;
import edu.cfd.e_learningPlatform.dto.response.FriendUserResponse;
import edu.cfd.e_learningPlatform.dto.response.InvitationResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.enums.FriendStatus;
import edu.cfd.e_learningPlatform.service.FriendService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/friend")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FriendController {

    FriendService friendService;
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/friend/invitation")
    public void sendInvitation(@Payload SendInvatitionRequest sendInvatitionRequest){
        InvitationResponse response = friendService.sendInvitation(sendInvatitionRequest.getIdUser(), sendInvatitionRequest.getIdFriend());
        messagingTemplate.convertAndSend("/friend/" + sendInvatitionRequest.getIdFriend() + "/private", response);
    }

    @MessageMapping("/friend/confirm")
    public void acceptInvitation(@RequestBody SendInvatitionRequest sendInvatitionRequest){
        InvitationResponse response = friendService.confirmInvitation(sendInvatitionRequest.getIdUser(), sendInvatitionRequest.getIdFriend(), FriendStatus.FRIEND);
        messagingTemplate.convertAndSend("/friend/" + sendInvatitionRequest.getIdFriend() + "/confirm/private", response);
    }

    @GetMapping("/invitation/{idUser}")
    public ResponseEntity<List<InvitationResponse>> getInvitation(@PathVariable String idUser){
        return ResponseEntity.ok(friendService.getInvitations(idUser));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFriend(@RequestBody SendInvatitionRequest sendInvatitionRequest){
        return ResponseEntity.ok(friendService.deleteFriend(sendInvatitionRequest.getIdUser(), sendInvatitionRequest.getIdFriend()));
    }

    @PostMapping("/get")
    public ResponseEntity<List<InvitationResponse>> getFriends(@RequestBody SendInvatitionRequest sendInvatitionRequest){
        return ResponseEntity.ok(friendService.getFriends(sendInvatitionRequest.getIdUser()));
    }

    @PostMapping("/search")
    public ResponseEntity<List<FriendUserResponse>> searchFriend(@RequestBody SendInvatitionRequest sendInvatitionRequest){
        return ResponseEntity.ok(friendService.searchFriends(sendInvatitionRequest.getIdFriend(), sendInvatitionRequest.getIdUser()));
    }
}
