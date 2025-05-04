package edu.cfd.e_learningPlatform.mapstruct;

import edu.cfd.e_learningPlatform.dto.response.FriendResponse;
import edu.cfd.e_learningPlatform.entity.Friend;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    FriendMapper INSTANCE = Mappers.getMapper(FriendMapper.class);

    @Mapping(source = "user.id", target = "idUser")
    @Mapping(source = "friend.id", target = "idFriend")
    FriendResponse friendToFriendResponse(Friend friend);

}
