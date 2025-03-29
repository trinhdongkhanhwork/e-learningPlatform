package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.request.MessageRequest;
import edu.cfd.e_learningPlatform.dto.response.MessageResponse;
import edu.cfd.e_learningPlatform.entity.Friend;
import edu.cfd.e_learningPlatform.entity.Message;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.enums.FriendStatus;
import edu.cfd.e_learningPlatform.mapstruct.MessageMapper;
import edu.cfd.e_learningPlatform.repository.FriendRepository;
import edu.cfd.e_learningPlatform.repository.MessageRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    MessageRepository messageRepository;
    MessageMapper messageMapper;
    FriendRepository friendRepository;
    UserRepository userRepository;

    @Override
    public MessageResponse sendMessage(MessageRequest messageRequest) {
        Friend friend = friendRepository.existFriend(messageRequest.getUserId(), messageRequest.getFriendId());
        if (friend == null || messageRequest.getUserId().equals(messageRequest.getFriendId())) throw new RuntimeException("Friend not found !");
        if(friend.getStatus() != FriendStatus.FRIEND) throw new RuntimeException("Friend is not friend !");
        User user = userRepository.findById(messageRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found !"));
        Message message = messageMapper.messageRequestToMessage(messageRequest);
        message.setFriend(friend);
        message.setCreatedAt(LocalDateTime.now());
        message.setUser(user);
        try {
           message = messageRepository.save(message);
           return messageMapper.messageToMessageResponse(message);
        } catch (Exception e) {
            throw new RuntimeException("Error sending message: " + e.getMessage());
        }
    }

    @Override
    public List<MessageResponse> getMessageFriend(String idUser, String idFriend) {
        Friend friend = friendRepository.existFriend(idUser, idFriend);
        if (friend == null || idUser.equals(idFriend)) throw new RuntimeException("Friend not found !");
        if(friend.getStatus() != FriendStatus.FRIEND) throw new RuntimeException("Friend is not friend !");
        List<Message> messages = messageRepository.findMessagesByFriendId(friend.getId());
        return messageMapper.messagesToMessageResponses(messages);
    }


}
