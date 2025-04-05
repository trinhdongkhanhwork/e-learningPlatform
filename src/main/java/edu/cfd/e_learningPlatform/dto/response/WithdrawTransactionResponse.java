package edu.cfd.e_learningPlatform.dto.response;

import edu.cfd.e_learningPlatform.enums.WithdrawStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawTransactionResponse {

    Long id;
    BigDecimal amount;
    LocalDateTime createdAt;
    String type;
    String email;
    WithdrawStatus status;
    String fullname;
}
