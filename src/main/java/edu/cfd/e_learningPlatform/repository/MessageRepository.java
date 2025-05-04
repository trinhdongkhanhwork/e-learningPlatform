package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m where m.friend.id = :id")
    List<Message> findMessagesByFriendId(Long id);
}
