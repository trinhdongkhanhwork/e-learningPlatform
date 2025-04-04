package edu.cfd.e_learningPlatform.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletResponse {

    private Long walletId;
    private String userId;
    private String fullname;
    private BigDecimal balance;
    private LocalDateTime updatedAt;
}
