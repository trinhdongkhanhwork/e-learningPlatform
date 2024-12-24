package edu.cfd.e_learningPlatform.repository;

import java.time.LocalDateTime;
import java.util.List;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.cfd.e_learningPlatform.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "Select * from message m " +
            "where m.assembly_id = :idGroup " +
            "order by m.created_at asc ", nativeQuery = true)
    List<Message> findMessageToGroup(Long idGroup);

}
