package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.response.WithdrawResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawTransactionResponse;

import java.math.BigDecimal;
import java.util.List;

public interface WithdrawService {

    WithdrawResponse withdraw(String userId, BigDecimal amount);
    WithdrawResponse confirmWithdraw(String userId, String otpInput, Long withdrawId);
    void autoDeleteExpiredWithdrawals();
    List<WithdrawTransactionResponse> getUserWithdrawalHistory(String userId);
}
