package edu.cfd.e_learningPlatform.service;


import edu.cfd.e_learningPlatform.dto.WithdrawDto;
import jakarta.mail.MessagingException;

import java.math.BigDecimal;
import java.util.List;


public interface WithdrawService {

    WithdrawDto requestWithdraw(String userId, BigDecimal amount) throws MessagingException;

    List<WithdrawDto> getWithdrawalHistory(String userId);

    BigDecimal calculateTotalPaymentsForInstructor(String instructorId);

    void confirmWithdraw(Long withdrawId, String otp);
}