package edu.cfd.e_learningPlatform.service;


import edu.cfd.e_learningPlatform.dto.WithdrawDto;
import jakarta.mail.MessagingException;

import java.math.BigDecimal;
import java.util.List;


public interface WithdrawService {

    WithdrawDto requestWithdraw(String userId, BigDecimal amount) throws MessagingException;

    BigDecimal calculateTotalPayments(String userId);

    List<WithdrawDto> getWithdrawalHistory(String userId);

    void confirmWithdraw(Long withdrawId, String otp);
}