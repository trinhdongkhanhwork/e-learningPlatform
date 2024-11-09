package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.ApprovedCourseRequest;
import jakarta.mail.MessagingException;

import java.time.LocalDateTime;

public interface EmailService {
    String generateOTP(String email);
    void sendOTPEmail(String email, String otp) throws MessagingException;
    boolean verifyOTP(String request, String encryptedOtp, LocalDateTime creationTime, LocalDateTime expirationTime);
    void sendOTPEmailForCreationUser(String email,String username, String otp) throws MessagingException;
    void sendEmailApprovedCourse(ApprovedCourseRequest approvedCourseRequest) throws MessagingException;
    void sendEmailDeleteCourse(Long id);
}
