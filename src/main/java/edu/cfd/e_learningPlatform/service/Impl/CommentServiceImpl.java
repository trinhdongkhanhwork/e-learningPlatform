package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.request.CommentRequest;
import edu.cfd.e_learningPlatform.dto.response.CommentVideoResponse;
import edu.cfd.e_learningPlatform.entity.Comment;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.entity.Video;
import edu.cfd.e_learningPlatform.mapstruct.CommentMapper;
import edu.cfd.e_learningPlatform.repository.CommentReponsitory;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.repository.VideoRepository;
import edu.cfd.e_learningPlatform.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    CommentReponsitory commentReponsitory;
    CommentMapper commentMapper;
    VideoRepository videoReponsitory;
    private final UserRepository userRepository;

    @Override
    public List<CommentVideoResponse> getCommentVideo(Long idVideo) {
        List<Object[]> resutls = commentReponsitory.getCommentVideo(idVideo);
        if(resutls == null){
            return null;
        }
        List<CommentVideoResponse> commentVideoResponses = resutls.stream()
                .map(result -> new CommentVideoResponse(
                        (Long) result[0],         // id
                        (String) result[1],       // fullName
                        (String) result[2],           //idUserComment
                        (String) result[3],       // profilePicture
                        (String) result[4],    // commentText
                        (String) result[5],       // nameUserParent
                        (Long) result[6]         // parentId
                )).collect(Collectors.toList());
        return commentVideoResponses;
    }

    @Override
    public CommentVideoResponse addComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        if(commentRequest.getUserId() != null){
            User user  = userRepository.findById(commentRequest.getUserId()).orElseThrow(() -> new RuntimeException("Not found User"));
            comment.setUser(user);
        } else {
            comment.setUser(null);
        }

        if(commentRequest.getParentId() != null){
            Comment commentParent = commentReponsitory.findById(commentRequest.getParentId()).orElse(null);
            comment.setComment(commentParent);
        } else {
            comment.setComment(null);
        }

        if(commentRequest.getVideoId() != null){
            Video video = videoReponsitory.findById(commentRequest.getVideoId()).orElse(null);
            comment.setVideo(video);
        } else {
            comment.setVideo(null);
        }
        comment.setCommentText(commentRequest.getCommentText());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setStar(commentRequest.getStar());
        commentReponsitory.save(comment);
        return commentMapper.commentToCommentVideoResponse(comment);
    }

    @Override
    public CommentVideoResponse updateComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        if(commentRequest.getUserId() != null){
            User user  = userRepository.findById(commentRequest.getUserId()).orElseThrow(() -> new RuntimeException("Not found User"));
            comment.setUser(user);
        } else {
            comment.setUser(null);
        }

        if(commentRequest.getParentId() != null){
            Comment commentParent = commentReponsitory.findById(commentRequest.getParentId()).orElse(null);
            comment.setComment(commentParent);
        } else {
            comment.setComment(null);
        }

        if(commentRequest.getVideoId() != null){
            Video video = videoReponsitory.findById(commentRequest.getVideoId()).orElse(null);
            comment.setVideo(video);
        } else {
            comment.setVideo(null);
        }
        comment.setId(commentRequest.getId());
        comment.setCommentText(commentRequest.getCommentText());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setStar(commentRequest.getStar());
        commentReponsitory.save(comment);
        return commentMapper.commentToCommentVideoResponse(comment);
    }

    @Override
    public Long deleteComment(Long idComment) {
        if(commentReponsitory.existsById(idComment)) {
            commentReponsitory.deleteById(idComment);
            return idComment;
        } else {
            throw new RuntimeException("Not found comment");
        }
    }
}