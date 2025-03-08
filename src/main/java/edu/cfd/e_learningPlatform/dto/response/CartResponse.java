package edu.cfd.e_learningPlatform.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

    Long id;
    Long courseId;
    String coverImage;
    BigDecimal price;
    String title;
    String level;
}
