package edu.cfd.e_learningPlatform.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import edu.cfd.e_learningPlatform.dto.response.CourseResponse;
import edu.cfd.e_learningPlatform.entity.Assembly;
import edu.cfd.e_learningPlatform.entity.Message;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.repository.AssemblyRepository;
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
    MessageMapper messageMapper;
    private final UserRepository userRepository;
    private final AssemblyRepository assemblyRepository;

    @Override
    public List<MessageResponse> getMessageToGroup(Long idGroup) {
        return messageMapper.messagetoMessageResponses(messageRepository.findMessageToGroup(idGroup));
    }

    @Override
    public MessageResponse sendMessageToGroup(MessageRequest messageRequest) {
        User user = userRepository.findById(messageRequest.getIdUserFrom()).orElseThrow(() -> new RuntimeException("User not found"));
        Assembly assembly = assemblyRepository.findById(messageRequest.getIdAssembly()).orElseThrow(() -> new RuntimeException("Assembly not found"));
        Message message = messageMapper.messageRequestToMessage(messageRequest);
        message.setId(null);
        message.setUserFrom(user);
        message.setCreatedAt(LocalDateTime.now());
        if(assembly.isActive()){
            message.setAssembly(assembly);
            return messageMapper.messagetoMessageResponse(messageRepository.save(message));
        } else {
            return null;
        }
    }

    @Override
    public MessageResponse updateMessageToGroup(MessageRequest messageRequest) {
        Message message = messageRepository.findById(messageRequest.getId()).orElseThrow(() -> new RuntimeException("Not found message"));
        message.setMessage(messageRequest.getMessage());
        return messageMapper.messagetoMessageResponse(messageRepository.save(message));
    }

    @Override
    public Long deleteMessage(Long idMessage) {
        Message message = messageRepository.findById(idMessage).orElseThrow(() -> new RuntimeException("Not found message"));
        messageRepository.delete(message);
        return message.getId();
    }



}
