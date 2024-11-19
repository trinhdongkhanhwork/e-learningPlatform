package edu.cfd.e_learningPlatform.service;

import java.math.BigDecimal;
import java.util.List;

import jakarta.mail.MessagingException;

import edu.cfd.e_learningPlatform.dto.WithdrawDto;

public interface WithdrawService {

    WithdrawDto requestWithdraw(String userId, BigDecimal amount) throws MessagingException;

    List<WithdrawDto> getWithdrawalHistory(String userId);

    BigDecimal calculateTotalPaymentsForInstructor(String instructorId);

    void confirmWithdraw(Long withdrawId, String otp);
}
