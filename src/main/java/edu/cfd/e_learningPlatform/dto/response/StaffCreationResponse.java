package edu.cfd.e_learningPlatform.dto.response;

import edu.cfd.e_learningPlatform.enums.Gender;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffCreationResponse {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;
    String email;
    String fullname;
    String birthday;
    Gender gender;
    String phone;
    String avatarUrl;
    List<String> permissions;
}
