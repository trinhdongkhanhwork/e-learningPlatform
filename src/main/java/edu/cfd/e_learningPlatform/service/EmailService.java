package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.ApprovedCourseRequest;
import edu.cfd.e_learningPlatform.enums.WithdrawStatus;
import jakarta.mail.MessagingException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface EmailService {
    String generateOTP(String email);
    void sendOTPEmail(String email, String otp) throws MessagingException;
    boolean verifyOTP(String request, String encryptedOtp, LocalDateTime creationTime, LocalDateTime expirationTime);
    void sendOTPEmailForCreationUser(String email,String username, String otp) throws MessagingException;
    void sendEmailApprovedCourse(ApprovedCourseRequest approvedCourseRequest) throws MessagingException;
    void sendEmailDeleteCourse(Long id);

    void sendEmail(String to, String subject, String htmlContent) throws MessagingException;

    void sendPaymentConfirmationEmail(String email, String paymentId, Double price) throws MessagingException;

    void sendWithdrawConfirmationEmail(String email, String fullname, BigDecimal amount, LocalDateTime requestDate, WithdrawStatus status) throws MessagingException;
}