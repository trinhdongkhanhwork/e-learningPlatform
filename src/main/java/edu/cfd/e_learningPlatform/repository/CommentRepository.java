package edu.cfd.e_learningPlatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.cfd.e_learningPlatform.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select " + "c.id as id, "
                    + "u.fullname as fullName, "
                    + "u.id as idUserComment, "
                    + "u.avatar_url as profilePicture, "
                    + "c.comment_text as commentText, "
                    + "userCommentParent.fullname as nameUserReply, "
                    + "c.comment_id as parentId "
                    + "from comments c "
                    + "inner join users u on c.user_id = u.id "
                    + "left join comments commentParent on c.comment_id = commentParent.id "
                    + "left join users userCommentParent on commentParent.user_id = userCommentParent.id "
                    + "where c.video_id = :id "
                    + "order by c.id ",
            nativeQuery = true)
    List<Object[]> getCommentVideo(Long id);

    @Query("SELECT c from Comment c where c.comment.id = :id")
    List<Comment> findByReplyId(Long id);

    @Query("SELECT c from Comment c where c.video.id = :id")
    List<Comment> findByVideo(Long id);
}
