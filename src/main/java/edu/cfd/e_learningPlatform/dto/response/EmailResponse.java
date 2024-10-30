package edu.cfd.e_learningPlatform.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailResponse {
    String hashedOtp;
    LocalDateTime creationTime ;
}
