package edu.cfd.e_learningPlatform.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePassWordRequest {
    @Size(min = 3, message = "INVALID_PASSWORD")
    String password;
}
