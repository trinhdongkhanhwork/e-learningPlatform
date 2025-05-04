package edu.cfd.e_learningPlatform.service;

import java.math.BigDecimal;
import java.util.List;

import edu.cfd.e_learningPlatform.dto.response.WithdrawResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawTransactionResponse;
import jakarta.mail.MessagingException;

import edu.cfd.e_learningPlatform.dto.WithdrawDto;

public interface WithdrawService {

    WithdrawResponse withdraw(String userId, BigDecimal amount);
    WithdrawResponse confirmWithdraw(String userId, String otpInput, Long withdrawId);
    void autoDeleteExpiredWithdrawals();
    List<WithdrawTransactionResponse> getUserWithdrawalHistory(String userId);
}
