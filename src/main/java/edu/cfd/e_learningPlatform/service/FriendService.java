package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.response.FriendResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.enums.FriendStatus;

import java.util.List;

public interface FriendService {
    FriendResponse sendInvitation(String idUser, String idFriend);
    FriendResponse confirmInvitation(String idUser, String idFriend, FriendStatus status);
    String deleteFriend(String idUser, String idFriend);
    List<UserResponse> getFriends(String idUser);
}
