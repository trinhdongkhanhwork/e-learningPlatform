package edu.cfd.e_learningPlatform.dto.response;

import java.time.LocalDateTime;
import java.util.Date;

import edu.cfd.e_learningPlatform.entity.Role;
import edu.cfd.e_learningPlatform.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String email;
    String fullname;
    Date birthday;
    Gender gender;
    String phone;
    String avatarUrl;
    LocalDateTime updatedDate;
    LocalDateTime createdDate;
    int version;
    boolean active;
    Role roleEntity;
}
