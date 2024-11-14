package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.request.ApprovedCourseRequest;
import edu.cfd.e_learningPlatform.entity.Course;
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

import java.time.Duration;
import java.time.LocalDateTime;
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
}
