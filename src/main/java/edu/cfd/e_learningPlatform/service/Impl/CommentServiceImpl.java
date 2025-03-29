package edu.cfd.e_learningPlatform.service.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.cfd.e_learningPlatform.dto.request.CommentRequest;
import edu.cfd.e_learningPlatform.dto.response.CommentVideoResponse;
import edu.cfd.e_learningPlatform.entity.Comment;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.entity.Video;
import edu.cfd.e_learningPlatform.mapstruct.CommentMapper;
import edu.cfd.e_learningPlatform.repository.CommentRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.repository.VideoRepository;
import edu.cfd.e_learningPlatform.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;
    CommentMapper commentMapper;
    VideoRepository videoReponsitory;
    private final UserRepository userRepository;

    @Override
    public List<CommentVideoResponse> getCommentVideo(Long idVideo) {
        List<Object[]> resutls = commentRepository.getCommentVideo(idVideo);
        if (resutls == null) {
            return null;
        }
        List<CommentVideoResponse> commentVideoResponses = resutls.stream()
                .map(result -> new CommentVideoResponse(
                        (Long) result[0], // id
                        (String) result[1], // fullName
                        (String) result[2], // idUserComment
                        (String) result[3], // profilePicture
                        (String) result[4], // commentText
                        (String) result[5], // nameUserParent
                        (Long) result[6] // parentId
                        ))
                .collect(Collectors.toList());
        return commentVideoResponses;
    }

    @Override
    public CommentVideoResponse addComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        if (commentRequest.getUserId() != null) {
            User user = userRepository
                    .findById(commentRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("Not found User"));
            comment.setUser(user);
        } else {
            comment.setUser(null);
        }

        if (commentRequest.getParentId() != null) {
            Comment commentParent =
                    commentRepository.findById(commentRequest.getParentId()).orElse(null);
            comment.setComment(commentParent);
        } else {
            comment.setComment(null);
        }

        if (commentRequest.getVideoId() != null) {
            Video video = videoReponsitory.findById(commentRequest.getVideoId()).orElse(null);
            comment.setVideo(video);
        } else {
            comment.setVideo(null);
        }
        comment.setCommentText(commentRequest.getCommentText());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return commentMapper.commentToCommentVideoResponse(comment);
    }

    @Override
    public CommentVideoResponse updateComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        if (commentRequest.getUserId() != null) {
            User user = userRepository
                    .findById(commentRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("Not found User"));
            comment.setUser(user);
        } else {
            comment.setUser(null);
        }

        if (commentRequest.getParentId() != null) {
            Comment commentParent =
                    commentRepository.findById(commentRequest.getParentId()).orElse(null);
            comment.setComment(commentParent);
        } else {
            comment.setComment(null);
        }

        if (commentRequest.getVideoId() != null) {
            Video video = videoReponsitory.findById(commentRequest.getVideoId()).orElse(null);
            comment.setVideo(video);
        } else {
            comment.setVideo(null);
        }
        comment.setId(commentRequest.getId());
        comment.setCommentText(commentRequest.getCommentText());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return commentMapper.commentToCommentVideoResponse(comment);
    }

    @Override
    public Long deleteComment(Long idComment) {
        if (commentRepository.existsById(idComment)) {
            Comment commentIndex = commentRepository.findById(idComment).orElse(null);

            while (commentIndex != null) {
                List<Comment> commentChilds =
                        commentRepository.findByReplyId(commentIndex.getId()); // Lấy ra comment con
                if (commentChilds.size() <= 0) { // Nếu không có comment con thì xóa
                    commentRepository.delete(commentIndex);
                    commentIndex = commentRepository.findById(idComment).orElse(null);
                } else { // Nếu có comment con thì chuyển comment con tiếp theo làm cha và tiếp tục vòng lặp
                    commentIndex = commentChilds.get(0);
                }
            }
            return idComment;
        } else {
            throw new RuntimeException("Not found comment");
        }
    }
}
