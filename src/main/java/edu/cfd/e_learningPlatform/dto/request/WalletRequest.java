package edu.cfd.e_learningPlatform.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletRequest {

    private String userId;
    private BigDecimal amount;
    private String transactionType;
}
