package edu.cfd.e_learningPlatform.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import edu.cfd.e_learningPlatform.dto.request.MessageRequest;
import edu.cfd.e_learningPlatform.dto.response.MessageResponse;
import edu.cfd.e_learningPlatform.entity.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    public static final MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "idUserFrom", target = "userFrom.id")
    @Mapping(source = "idAssembly", target = "assembly.id")
    Message messageRequestToMessage(MessageRequest messageRequest);

    @Mapping(source = "userFrom.id", target = "idUserFrom")
    @Mapping(source = "userFrom.fullname", target = "nameUserFrom")
    @Mapping(source = "userFrom.avatarUrl", target = "avatarUserFrom")
    @Mapping(source = "assembly.id", target = "idAssembly")
    MessageResponse messagetoMessageResponse(Message message);

    @Mapping(source = "userFrom.id", target = "idUserFrom")
    @Mapping(source = "userFrom.fullname", target = "nameUserFrom")
    @Mapping(source = "userFrom.avatarUrl", target = "avatarUserFrom")
    @Mapping(source = "assembly.id", target = "idAssembly")
    List<MessageResponse> messagetoMessageResponses(List<Message> messages);
}
