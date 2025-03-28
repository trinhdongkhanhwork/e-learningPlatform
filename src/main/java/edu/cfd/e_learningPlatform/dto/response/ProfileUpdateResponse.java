package edu.cfd.e_learningPlatform.dto.response;

import edu.cfd.e_learningPlatform.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileUpdateResponse {
    String id;
    String username;
    String email;
    String fullname;
    String phone;
    Role roleEntity;
    String avatarUrl;
}
