package edu.cfd.e_learningPlatform.service;

import java.time.LocalDateTime;

import jakarta.mail.MessagingException;

import edu.cfd.e_learningPlatform.dto.request.ApprovedCourseRequest;

public interface EmailService {
    String generateOTP(String email);

    void sendOTPEmail(String email, String otp) throws MessagingException;

    boolean verifyOTP(String request, String encryptedOtp, LocalDateTime creationTime, LocalDateTime expirationTime);

    void sendOTPEmailForCreationUser(String email, String username, String otp) throws MessagingException;

    void sendEmailApprovedCourse(ApprovedCourseRequest approvedCourseRequest) throws MessagingException;

    void sendEmailDeleteCourse(Long id);

    void sendEmail(String to, String subject, String htmlContent) throws MessagingException;
}
