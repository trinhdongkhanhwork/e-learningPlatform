package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.MessageRequest;
import edu.cfd.e_learningPlatform.dto.response.MessageFriendResponse;
import edu.cfd.e_learningPlatform.dto.response.MessageResponse;

public interface MessageService {
    MessageResponse sendMessage(MessageRequest messageRequest);
    MessageFriendResponse getMessageFriend(String idUser, String idFriend);
    MessageResponse recallMessage(Long idMessage);
}
