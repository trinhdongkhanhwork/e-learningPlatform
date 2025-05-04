package edu.cfd.e_learningPlatform.dto.response;

import edu.cfd.e_learningPlatform.enums.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendUserResponse {
    private FriendStatus friendStatus;
    private String userId;
    private String avatarUrl;
    private String fullName;
}
