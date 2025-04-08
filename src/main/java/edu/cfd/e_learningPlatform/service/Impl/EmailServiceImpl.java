package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.request.ApprovedCourseRequest;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.enums.WithdrawStatus;
import edu.cfd.e_learningPlatform.exception.AppException;
import edu.cfd.e_learningPlatform.exception.ErrorCode;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {

    JavaMailSender mailSender;
    UserRepository userRepository;
    CourseRepository courseRepository;

    PasswordEncoder passwordEncoder;

    @Override
    public String generateOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        return otp;
    }

    @Override
    public void sendOTPEmail(String email, String otp) throws MessagingException {
        if (!userRepository.existsByEmail(email))
            throw new AppException(ErrorCode.USER_EXISTED);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("OTP Confirmation");
        helper.setText("Your OTP code is: " + otp, true);
        mailSender.send(message);
    }

    @Override
    public boolean verifyOTP(String request, String encryptedOtp, LocalDateTime creationTime, LocalDateTime expirationTime) {
        return encryptedOtp != null
                && passwordEncoder.matches(request, encryptedOtp)
                && Duration.between(creationTime, expirationTime).getSeconds() <= 120;

    }

    @Override
    public void sendOTPEmailForCreationUser(String email,String username, String otp) throws MessagingException {
        if (userRepository.existsByEmail(email) && userRepository.existsByUsername(username))
            throw new AppException(ErrorCode.USER_EXISTED);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("OTP Confirmation");
        helper.setText("Your OTP code is: " + otp, true);
        mailSender.send(message);
    }

    @Override
    public void sendEmailApprovedCourse(ApprovedCourseRequest approvedCourseRequest) throws MessagingException {
        Course course = courseRepository.findByCourseName(approvedCourseRequest.getCourseName());
        if (course == null) {
            throw new RuntimeException("Course not found");
        }else {
            course.setPublished(approvedCourseRequest.isCourseStatus());
            courseRepository.save(course);
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setSubject("Course status information: " + approvedCourseRequest.getCourseName());
        helper.setTo(approvedCourseRequest.getEmail());

        String htmlContent = "<html><body>" +
                "<p>Hello " + course.getInstructor().getFullname() + ",</p>" +
                "<p>Your Course: <strong>" + approvedCourseRequest.getCourseName() + "</strong> now in status: " +
                (approvedCourseRequest.isCourseStatus() ? "<span style='color:green;'>Published</span>" : "<span style='color:red;'>Draft</span>") +
                "</p>" +
                "<p>" + approvedCourseRequest.getText() + "</p>" +
                "</body></html>";

        helper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailDeleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Course deleted information: " + course.getTitle());
        message.setTo(course.getInstructor().getEmail());
        message.setText("Your course: " + course.getTitle() + " has been deleted");
        mailSender.send(message);
    }
    @Override
    public void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true để chỉ định là nội dung HTML

        mailSender.send(mimeMessage);
    }
    @Override
    public void sendPaymentConfirmationEmail(String email, String paymentId, Double price) throws MessagingException {
        String htmlContent = String.format(
                "<html><body>" +
                        "<h1>Payment Confirmation</h1>" +
                        "<p>Your payment has been processed successfully.</p>" +
                        "<ul>" +
                        "<li><strong>Payment ID:</strong> %s</li>" +
                        "<li><strong>Amount:</strong> %.2f USD</li>" +
                        "</ul>" +
                        "</body></html>", paymentId, price
        );
        sendEmail(email, "Payment Confirmation", htmlContent);
    }

    @Override
    public void sendWithdrawConfirmationEmail(String email, String fullname, BigDecimal amount, LocalDateTime requestDate, WithdrawStatus status) throws MessagingException {
        String subject = "Withdraw Request Confirmation";

        String formattedAmount = amount.stripTrailingZeros().toPlainString();

        String htmlContent = String.format(
                "<html>" +
                        "<body style='font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;'>" +
                        "<div style='max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; padding: 30px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
                        "<h2 style='color: #4CAF50;'>Withdraw Request Confirmation</h2>" +
                        "<p>Dear <strong>%s</strong>,</p>" +
                        "<p>Your withdraw request has been <strong>submitted successfully</strong>. Here are the details:</p>" +
                        "<table style='width: 100%%; border-collapse: collapse; margin-top: 20px;'>"+
                        "  <tr><td style='padding: 8px; border-bottom: 1px solid #ddd;'><strong>Amount:</strong></td><td style='padding: 8px; border-bottom: 1px solid #ddd;'>%s USD</td></tr>" +
                        "  <tr><td style='padding: 8px; border-bottom: 1px solid #ddd;'><strong>Request Date:</strong></td><td style='padding: 8px; border-bottom: 1px solid #ddd;'>%s</td></tr>" +
                        "  <tr><td style='padding: 8px;'><strong>Status:</strong></td><td style='padding: 8px;'>%s</td></tr>" +
                        "</table>" +
                        "<p style='margin-top: 20px;'>We are currently processing your request. You will receive another email once the process is complete.</p>" +
                        "<p>Thank you for using our service!</p>" +
                        "<p style='color: #888888; font-size: 12px;'>This is an automated message. Please do not reply to this email.</p>" +
                        "</div>" +
                        "</body>" +
                        "</html>",
                fullname,
                formattedAmount,
                requestDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                status.name()
        );

        sendEmail(email, subject, htmlContent);
    }
}