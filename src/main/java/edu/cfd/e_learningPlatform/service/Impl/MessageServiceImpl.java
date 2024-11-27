package edu.cfd.e_learningPlatform.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import edu.cfd.e_learningPlatform.dto.response.CourseResponse;
import edu.cfd.e_learningPlatform.entity.Message;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import org.springframework.stereotype.Service;

import edu.cfd.e_learningPlatform.dto.request.MessageRequest;
import edu.cfd.e_learningPlatform.dto.response.MessageResponse;
import edu.cfd.e_learningPlatform.mapstruct.MessageMapper;
import edu.cfd.e_learningPlatform.repository.MessageRepository;
import edu.cfd.e_learningPlatform.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    MessageRepository messageRepository;
    UserRepository userRepository;
    CourseRepository courseRepository;
    MessageMapper messageMapper;

    @Override
    public List<MessageResponse> getMessagesToUser(String idUserTo, String idUserFrom) {
        List<MessageResponse> messageRepons = messageMapper.toMessageReponses(messageRepository.findMessageToUser(idUserTo, idUserFrom));
        return messageRepons;
    }

    @Override
    public List<MessageResponse> getMessagesToCourse(Long idCourse) {
        List<MessageResponse> messageRepons =
                messageMapper.toMessageReponses(messageRepository.findMessageToCourse(idCourse));
        return messageRepons;
    }

    @Override
    public Long deleteMessage(Long idMessage) {
        messageRepository.deleteById(idMessage);
        return idMessage;
    }

    @Override
    public MessageResponse addMessage(MessageRequest request) {
        Message message = messageMapper.toMessage(request);
        message.setCreatedAt(LocalDateTime.now());
        message.setUserTo(request.getIdUserTo() != null ? userRepository.findById(request.getIdUserTo()).orElse(null) : null);
        message.setUserFrom(request.getIdUserFrom() != null ? userRepository.findById(request.getIdUserFrom()).orElse(null) : null);
        return messageMapper.toMessageReponse(messageRepository.save(message));
    }
}
