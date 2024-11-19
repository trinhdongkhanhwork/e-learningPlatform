package edu.cfd.e_learningPlatform.dto.response;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishlistDtoResponse {
    Long id;
    Long courseId;
    String coverImage;
    BigDecimal price;
    String title;
    String level;
}
