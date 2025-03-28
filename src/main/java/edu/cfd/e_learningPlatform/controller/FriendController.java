package edu.cfd.e_learningPlatform.controller;


import edu.cfd.e_learningPlatform.dto.request.SendInvatitionRequest;
import edu.cfd.e_learningPlatform.dto.response.FriendResponse;
import edu.cfd.e_learningPlatform.dto.response.FriendUserResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.enums.FriendStatus;
import edu.cfd.e_learningPlatform.service.FriendService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/friend")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FriendController {

    FriendService friendService;

    @PostMapping("/invitation")
    public ResponseEntity<FriendResponse> sendInvitation(@RequestBody SendInvatitionRequest sendInvatitionRequest){
        return ResponseEntity.ok(friendService.sendInvitation(sendInvatitionRequest.getIdUser(), sendInvatitionRequest.getIdFriend()));
    }

    @PutMapping("/{status}")
    public ResponseEntity<FriendResponse> acceptInvitation(@PathVariable FriendStatus status, @RequestBody SendInvatitionRequest sendInvatitionRequest){
        return ResponseEntity.ok(friendService.confirmInvitation(sendInvatitionRequest.getIdUser(), sendInvatitionRequest.getIdFriend(), status));
    }

    @GetMapping("/invitation/{idUser}")
    public ResponseEntity<List<UserResponse>> getInvitation(@PathVariable String idUser){
        return ResponseEntity.ok(friendService.getInvitations(idUser));
    }
    
    @DeleteMapping
    public ResponseEntity<String> deleteFriend(@RequestBody SendInvatitionRequest sendInvatitionRequest){
        return ResponseEntity.ok(friendService.deleteFriend(sendInvatitionRequest.getIdUser(), sendInvatitionRequest.getIdFriend()));
    }

    @PostMapping("/get")
    public ResponseEntity<List<UserResponse>> getFriends(@RequestBody SendInvatitionRequest sendInvatitionRequest){
        return ResponseEntity.ok(friendService.getFriends(sendInvatitionRequest.getIdUser()));
    }

    @PostMapping("/search")
    public ResponseEntity<List<FriendUserResponse>> searchFriend(@RequestBody SendInvatitionRequest sendInvatitionRequest){
        return ResponseEntity.ok(friendService.searchFriends(sendInvatitionRequest.getIdFriend(), sendInvatitionRequest.getIdUser()));
    }
}