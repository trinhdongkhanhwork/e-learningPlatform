package edu.cfd.e_learningPlatform.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import edu.cfd.e_learningPlatform.dto.request.CommentRequest;
import edu.cfd.e_learningPlatform.dto.response.CommentVideoResponse;
import edu.cfd.e_learningPlatform.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    public static final CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "parentId", target = "comment.id")
    @Mapping(source = "videoId", target = "video.id")
    Comment commentRequestToComment(CommentRequest commentRequest);

    @Mapping(source = "user.avatarUrl", target = "profilePicture")
    @Mapping(source = "user.fullname", target = "fullName")
    @Mapping(source = "user.id", target = "idUserComment")
    @Mapping(source = "comment.id", target = "parentId")
    @Mapping(source = "comment.user.fullname", target = "nameUserParent")
    CommentVideoResponse commentToCommentVideoResponse(Comment comment);
}
