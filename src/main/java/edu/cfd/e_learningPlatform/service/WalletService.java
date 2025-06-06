package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.response.WalletResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawResponse;

import java.math.BigDecimal;

public interface WalletService {

    WalletResponse depositToAdminWallet(Long courseId, BigDecimal amount);
    WalletResponse getWalletByUserId(String userId);
}
