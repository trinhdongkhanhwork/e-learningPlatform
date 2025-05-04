package edu.cfd.e_learningPlatform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import edu.cfd.e_learningPlatform.enums.WithdrawStatus;
import lombok.Data;

@Data
public class WithdrawDto {
    private Long id;
    private String userId;
    private BigDecimal price;
    private LocalDateTime requestDate;
    private String otp;
    private WithdrawStatus status;
}
