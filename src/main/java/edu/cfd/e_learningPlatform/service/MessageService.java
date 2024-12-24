package edu.cfd.e_learningPlatform.service;

import java.util.List;

import edu.cfd.e_learningPlatform.dto.request.MessageRequest;
import edu.cfd.e_learningPlatform.dto.response.MessageResponse;

public interface MessageService {
    List<MessageResponse> getMessageToGroup(Long idGroup);
    MessageResponse sendMessageToGroup(MessageRequest request);
    MessageResponse updateMessageToGroup(MessageRequest request);
    Long deleteMessage(Long idMessage);
}
