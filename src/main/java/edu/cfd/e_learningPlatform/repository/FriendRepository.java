package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.dto.response.FriendUserResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
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
            "and u.id <> :idUser " +
            "and f.status = 1")
    List<User> selectFriends(@Param("idUser") String idUser);

    @Query("SELECT new edu.cfd.e_learningPlatform.dto.response.FriendUserResponse(new edu.cfd.e_learningPlatform.dto.response.FriendResponse(f.id, f.user.id, f.friend.id, f.createdAt, f.status), new edu.cfd.e_learningPlatform.dto.response.UserResponse(u.id, u.username, u.email, u.fullname, u.birthday, u.gender, u.phone, u.avatarUrl, u.updatedDate, u.createdDate, u.version, u.isActive, u.roles)) " +
            "FROM User u LEFT JOIN Friend f ON (f.user = u OR f.friend = u) " +
            "                                  AND (f.user.id = :idUser OR f.friend.id = :idUser) " +
            "WHERE LOWER(u.fullname) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%')) AND u.id NOT LIKE :idUser AND :keyword NOT LIKE ''")
    List<FriendUserResponse> findByFullnameContaining(String keyword, String idUser);

    @Query("select new edu.cfd.e_learningPlatform.dto.response.UserResponse(u.id, u.username, u.email, u.fullname, u.birthday, u.gender, u.phone, u.avatarUrl, u.updatedDate, u.createdDate, u.version, u.isActive, u.roles) " +
            "from User u left join Friend f on u = f.user " +
            "where f.status = 0 and f.friend.id = :idUser")
    List<UserResponse> selectInvitation(String idUser);
}
