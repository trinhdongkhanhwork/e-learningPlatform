package edu.cfd.e_learningPlatform.dto.response;

import edu.cfd.e_learningPlatform.enums.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendUserResponse {
    private FriendResponse friendStatus;
    private UserResponse user;
}
