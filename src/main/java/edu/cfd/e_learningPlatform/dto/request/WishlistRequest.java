package edu.cfd.e_learningPlatform.dto.request;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class WishlistRequest {
    Long id;
    String userId;
    Long courseId;
    LocalDateTime addAt;
}
