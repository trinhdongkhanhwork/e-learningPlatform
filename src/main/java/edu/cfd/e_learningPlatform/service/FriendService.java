package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.response.FriendUserResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.enums.FriendStatus;

import java.util.List;

public interface FriendService {
    UserResponse sendInvitation(String idUser, String idFriend);
    List<UserResponse> getInvitations(String idUser);
    UserResponse confirmInvitation(String idUser, String idFriend, FriendStatus status);
    String deleteFriend(String idUser, String idFriend);
    List<UserResponse> getFriends(String idUser);
    List<FriendUserResponse> searchFriends(String keyword, String idUser);
}
