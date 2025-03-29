package edu.cfd.e_learningPlatform.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WithdrawResponse {
    private Long withdrawId;
    private String userId;
    private BigDecimal amount;
    private LocalDateTime requestDate;
    private String status;
}