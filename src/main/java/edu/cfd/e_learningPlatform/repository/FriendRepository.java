package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Friend;
import edu.cfd.e_learningPlatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select f from Friend f where f.user.id = :idUser and f.friend.id = :idFriend " +
            "or f.user.id = :idFriend and f.friend.id = :idUser")
    Friend existFriend(String idUser, String idFriend);

    @Query("select distinct u from User u " +
            "join Friend f on u = f.user or u = f.friend " +
            "where :idUser in (f.user.id, f.friend.id) " +
            "and u.id <> :idUser")
    List<User> selectFriends(@Param("idUser") String idUser);
}