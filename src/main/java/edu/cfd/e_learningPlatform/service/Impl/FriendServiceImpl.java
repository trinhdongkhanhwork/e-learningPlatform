package edu.cfd.e_learningPlatform.service.Impl;


import edu.cfd.e_learningPlatform.dto.response.FriendResponse;
import edu.cfd.e_learningPlatform.dto.response.FriendUserResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.entity.Friend;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.enums.FriendStatus;
import edu.cfd.e_learningPlatform.mapstruct.FriendMapper;
import edu.cfd.e_learningPlatform.mapstruct.UserMapper;
import edu.cfd.e_learningPlatform.repository.FriendRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.FriendService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    FriendRepository friendRepository;
    UserRepository userRepository;
    FriendMapper friendMapper;
    UserMapper userMapper;

    @Override
    public FriendResponse sendInvitation(String idUser, String idFriend) {
        Friend friend = friendRepository.existFriend(idUser, idFriend);
        if (friend == null && !idUser.equals(idFriend)) {
            friend = new Friend();
            friend.setUser(userRepository.findById(idUser).orElseThrow(() -> new RuntimeException("User not found !")));
            friend.setFriend(userRepository.findById(idFriend).orElseThrow(() -> new RuntimeException("Friend not found !")));
            friend.setCreatedAt(LocalDateTime.now());
            friend.setStatus(FriendStatus.INVITATION);
            try {
                friendRepository.save(friend);
            } catch (Exception e) {
                throw new RuntimeException("Error invitation friend: " + e.getMessage());
            }
            return friendMapper.friendToFriendResponse(friend);
        } else {
            throw new RuntimeException("Alreadly friends !");
        }
    }

    @Override
    public List<UserResponse> getInvitations(String idUser) {
        return friendRepository.selectInvitation(idUser);
    }

    @Override
    public FriendResponse confirmInvitation(String idUser, String idFriend, FriendStatus status) {
        Friend friend = friendRepository.existFriend(idUser, idFriend);
        if (friend == null && !idUser.equals(idFriend)) throw new RuntimeException("Friend not found !");
        friend.setStatus(status);
        try {
           friend = friendRepository.save(friend);
           return friendMapper.friendToFriendResponse(friend);
        } catch (Exception e) {
            throw new RuntimeException("Error updating friend: " + e.getMessage());
        }
    }

    @Override
    public String deleteFriend(String idUser, String idFriend) {
        Friend friend = friendRepository.existFriend(idUser, idFriend);
        if (friend == null && !idUser.equals(idFriend)) throw new RuntimeException("Friend not found !");
        try {
            friendRepository.delete(friend);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting friend: " + e.getMessage());
        };
        return "Delete friend successfully !";
    }

    @Override
    public List<UserResponse> getFriends(String idUser) {
        List<User> users = friendRepository.selectFriends(idUser);
        return userMapper.toUserResponses(users);
    }

    @Override
    public List<FriendUserResponse> searchFriends(String keyword, String idUser) {
        return friendRepository.findByFullnameContaining(keyword, idUser);
    }
}
