package edu.cfd.e_learningPlatform.dto.response;

import edu.cfd.e_learningPlatform.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffResponse {
    String id;
    String username;
    String email;
    String fullname;
    Date birthday;
    Gender gender;
    String phone;
    String avatarUrl;
    boolean enabled;
    Set<String> permissions;
}
