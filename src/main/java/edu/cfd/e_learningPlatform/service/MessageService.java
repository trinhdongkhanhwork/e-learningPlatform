package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.MessageRequest;
import edu.cfd.e_learningPlatform.dto.response.MessageResponse;

import java.util.List;

public interface MessageService {
    MessageResponse sendMessage(MessageRequest messageRequest);
    List<MessageResponse> getMessageFriend(String idUser, String idFriend);
}
