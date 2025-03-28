package edu.cfd.e_learningPlatform.mapstruct;

import edu.cfd.e_learningPlatform.dto.request.MessageRequest;
import edu.cfd.e_learningPlatform.dto.response.MessageResponse;
import edu.cfd.e_learningPlatform.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(target = "recall", constant = "false")
    Message messageRequestToMessage(MessageRequest messageRequest);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "friendId", source = "friend.id")
    MessageResponse messageToMessageResponse(Message messageRequest);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "friendId", source = "friend.id")
    List<MessageResponse> messagesToMessageResponses(List<Message> messages);
}
