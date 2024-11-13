package edu.cfd.e_learningPlatform.service;

import jakarta.mail.MessagingException;

import java.time.LocalDateTime;

public interface EmailService {
    String generateOTP(String email);
    void sendOTPEmail(String email, String otp) throws MessagingException;
    boolean verifyOTP(String request, String encryptedOtp, LocalDateTime creationTime, LocalDateTime expirationTime);
    void sendOTPEmailForCreationUser(String email,String username, String otp) throws MessagingException;

    void sendEmail(String to, String subject, String htmlContent) throws MessagingException;

    void sendEmailDeleteCourse(Long id);

    void sendEmailPayment(String to, String subject, String paymentId, Double price, String paymentDate) throws MessagingException;
}
