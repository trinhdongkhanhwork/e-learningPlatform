package edu.cfd.e_learningPlatform.dto.request;

import edu.cfd.e_learningPlatform.entity.Role;
import edu.cfd.e_learningPlatform.enums.Gender;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Size(min = 3, message = "INVALID_PASSWORD")
    String password;
    String email;
    String fullname;
    Date birthday;
    Gender gender;
    String phone;
    String avatarUrl;
    LocalDateTime updatedDate;
    LocalDateTime createdDate;
    boolean isActive;
    Role roleEntity;
}
