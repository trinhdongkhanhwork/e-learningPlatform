package edu.cfd.e_learningPlatform.service.Impl;


import com.paypal.api.payments.Payout;
import com.paypal.api.payments.PayoutBatch;
import com.paypal.api.payments.PayoutItem;
import com.paypal.api.payments.PayoutSenderBatchHeader;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import edu.cfd.e_learningPlatform.dto.WithdrawDto;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.Payment;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.entity.Withdraw;
import edu.cfd.e_learningPlatform.enums.WithdrawStatus;
import edu.cfd.e_learningPlatform.mapstruct.WithdrawMapper;
import edu.cfd.e_learningPlatform.repository.*;
import edu.cfd.e_learningPlatform.service.EmailService;
import edu.cfd.e_learningPlatform.service.WithdrawService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {
    APIContext apiContext;
    UserRepository userRepository;
    PaymentRepository paymentRepository;
    EmailService emailService;
    WithdrawMapper withdrawMapper;
    WithdrawRepository withdrawRepository;
    CourseRepository courseRepository;
    PaymentStatusRepository paymentStatusRepository;
    private static final int OTP_LENGTH = 6; // Độ dài OTP
    private static final SecureRandom random = new SecureRandom();

    // Phương thức tạo OTP ngẫu nhiên
    private String generateOTP() {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10)); // Tạo số ngẫu nhiên từ 0-9
        }
        return otp.toString();
    }

    @Transactional
    @Override
    public WithdrawDto requestWithdraw(String userId, BigDecimal priceInVND) throws MessagingException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        // Kiểm tra số dư của người dùng
        if (user.getPrice().compareTo(priceInVND) < 0) {
            throw new IllegalArgumentException("Không đủ số dư để thực hiện yêu cầu rút tiền");
        }

        // Tính 20% lợi nhuận cho admin từ số tiền rút
        BigDecimal adminProfit = priceInVND.multiply(new BigDecimal("0.20"));
        // Tạo yêu cầu rút tiền
        Withdraw withdraw = new Withdraw();
        withdraw.setUser(user);
        withdraw.setPrice(priceInVND);
        withdraw.setRequestDate(LocalDateTime.now());
        withdraw.setStatus(WithdrawStatus.PENDING);

        // Tạo OTP và lưu vào đối tượng Withdraw
        String otp = generateOTP();
        withdraw.setOtp(otp);
        withdrawRepository.save(withdraw); // Lưu yêu cầu rút tiền

        // Gửi OTP qua email
        sendOTPEmail(user.getEmail(), otp);

        return withdrawMapper.toDto(withdraw); // Trả về WithdrawDto
    }

    private void sendOTPEmail(String email, String otp) throws MessagingException {
        String subject = "Xác thực yêu cầu rút tiền";
        String content = String.format("<html><body>" +
                "<p>Mã OTP của bạn để xác thực yêu cầu rút tiền là: <strong>%s</strong>.</p>" +
                "<p>Vui lòng nhập mã OTP để tiếp tục.</p>" +
                "</body></html>", otp);
        emailService.sendEmail(email, subject, content);
    }

    private PayoutBatch executePayout(User user, BigDecimal priceInUSD) throws PayPalRESTException {
        // Sử dụng trực tiếp số tiền USD mà không cần chuyển đổi
        BigDecimal amountInUSD = priceInUSD.setScale(2, RoundingMode.HALF_EVEN);  // Đảm bảo số tiền có 2 chữ số thập phân

        // Tạo một đối tượng Payout
        Payout payout = new Payout();
        PayoutSenderBatchHeader senderBatchHeader = new PayoutSenderBatchHeader();
        senderBatchHeader.setSenderBatchId(UUID.randomUUID().toString())
                .setEmailSubject("Bạn vừa nhận được một khoản thanh toán!");

        // Tạo mục thanh toán PayPal
        PayoutItem payoutItem = new PayoutItem();
        payoutItem.setRecipientType("EMAIL")
                .setReceiver(user.getEmail())
                .setAmount(new com.paypal.api.payments.Currency()
                        .setCurrency("USD")  // Sử dụng USD trực tiếp
                        .setValue(amountInUSD.toString())) // Sử dụng số tiền USD đã xác định
                .setNote("Rút tiền qua PayPal")
                .setSenderItemId(UUID.randomUUID().toString());

        // Đặt tiêu đề và mục thanh toán
        payout.setSenderBatchHeader(senderBatchHeader)
                .setItems(Collections.singletonList(payoutItem));

        // Thực hiện yêu cầu tạo thanh toán qua PayPal
        return payout.create(apiContext, new HashMap<>());
    }

    @Override
    public BigDecimal calculateTotalPaymentsForInstructor(String userId) {
        List<Course> courses = courseRepository.findByInstructorId(userId); // Lấy tất cả khóa học của giảng viên
        BigDecimal totalPayments = BigDecimal.ZERO;

        // Tính tổng số tiền từ các thanh toán liên quan đến khóa học của giảng viên
        for (Course course : courses) {
            BigDecimal courseTotal = paymentRepository.sumPriceByCourseId(course.getId());
            // Nếu courseTotal là null, gán giá trị là BigDecimal.ZERO
            if (courseTotal == null) {
                courseTotal = BigDecimal.ZERO;
            }
            totalPayments = totalPayments.add(courseTotal);
        }

        // Tính tổng số tiền đã rút của giảng viên
        BigDecimal totalWithdrawals = withdrawRepository.sumAmountByUserIdAndStatus(userId, WithdrawStatus.COMPLETED);
        // Nếu totalWithdrawals là null, gán giá trị là BigDecimal.ZERO
        if (totalWithdrawals == null) {
            totalWithdrawals = BigDecimal.ZERO;
        }

        // Trừ số tiền đã rút ra khỏi tổng số tiền
        BigDecimal netTotal = totalPayments.subtract(totalWithdrawals);

        // Lưu tổng số tiền vào bảng User
        Optional<User> userOptional = userRepository.findById(userId); // Tìm người dùng theo userId
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPrice(netTotal); // Lưu tổng số tiền đã trừ vào thuộc tính price của User
            userRepository.save(user); // Lưu lại vào cơ sở dữ liệu
        }

        return netTotal; // Trả về tổng số tiền đã trừ
    }


    @Override
    public void confirmWithdraw(Long withdrawId, String otp) {
        // Tìm yêu cầu rút tiền theo withdrawId
        Withdraw withdraw = withdrawRepository.findById(withdrawId)
                .orElseThrow(() -> new IllegalArgumentException("Yêu cầu rút tiền không hợp lệ"));

        // Kiểm tra trạng thái của yêu cầu rút tiền
        if (!withdraw.getStatus().equals(WithdrawStatus.PENDING)) {
            throw new IllegalArgumentException("Yêu cầu rút tiền đã được xử lý hoặc không hợp lệ");
        }

        // Kiểm tra tính hợp lệ của OTP
        if (!isValidOTP(withdrawId, otp)) {
            throw new IllegalArgumentException("Mã OTP không hợp lệ");
        }

        User user = withdraw.getUser();

        // Kiểm tra số dư tài khoản
        if (user.getPrice().compareTo(withdraw.getPrice()) < 0) {
            throw new IllegalArgumentException("Không đủ số dư để thực hiện yêu cầu rút tiền");
        }

        try {
            // Thực hiện yêu cầu rút tiền qua PayPal
            executePayout(user, withdraw.getPrice());  // Truyền số tiền USD vào hàm

            // Cập nhật trạng thái yêu cầu rút tiền thành hoàn tất
            withdraw.setStatus(WithdrawStatus.COMPLETED);
            withdrawRepository.save(withdraw);
            // Gửi email xác nhận rút tiền thành công
            sendWithdrawSuccessEmail(user.getEmail(), withdraw.getPrice());

        } catch (PayPalRESTException | MessagingException e) {
            // Xử lý lỗi khi thực hiện thanh toán qua PayPal
            throw new RuntimeException("Có lỗi xảy ra khi thực hiện yêu cầu rút tiền qua PayPal: " + e.getMessage());
        }
    }

    private void sendWithdrawSuccessEmail(String email, BigDecimal amount) throws MessagingException {
        String subject = "Xác nhận rút tiền thành công";
        String content = String.format("<html><body>" +
                "<p>Yêu cầu rút tiền của bạn với số tiền <strong>%s VND</strong> đã được xử lý thành công.</p>" +
                "<p>Xin cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>" +
                "</body></html>", amount);

        emailService.sendEmail(email, subject, content);
    }

    @Override
    public List<WithdrawDto> getWithdrawalHistory(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        List<Withdraw> withdrawals = withdrawRepository.findByUser(user);

        // Sắp xếp danh sách rút tiền theo ngày yêu cầu từ mới đến cũ
        return withdrawals.stream()
                .sorted(Comparator.comparing(Withdraw::getRequestDate).reversed())
                .map(withdrawMapper::toDto)
                .collect(Collectors.toList());
    }

    @Scheduled(fixedDelay = 60000)
    public void checkWithdrawRequestExpiry() {
        List<Withdraw> pendingWithdrawals = withdrawRepository.findByStatus(WithdrawStatus.PENDING);

        for (Withdraw withdraw : pendingWithdrawals) {
            Duration duration = Duration.between(withdraw.getRequestDate(), LocalDateTime.now());

            if (duration.toMinutes() >= 2) {
                withdraw.setStatus(WithdrawStatus.CANCELLED);
                withdrawRepository.save(withdraw);
            }
        }
    }

    private boolean isValidOTP(Long withdrawId, String otp) {
        Withdraw withdraw = withdrawRepository.findById(withdrawId)
                .orElseThrow(() -> new IllegalArgumentException("Yêu cầu rút tiền không hợp lệ"));

        // In ra mã OTP đã lưu và mã OTP nhập vào để kiểm tra
        System.out.println("Mã OTP đã lưu: " + withdraw.getOtp());
        System.out.println("Mã OTP nhập vào: " + otp);

        // Kiểm tra nếu OTP không hợp lệ hoặc đã được sử dụng
        boolean valid = withdraw.getOtp() != null && withdraw.getOtp().equals(otp) && withdraw.getStatus().equals(WithdrawStatus.PENDING);

        if (!valid) {
            System.out.println("OTP không hợp lệ cho yêu cầu rút tiền ID: " + withdrawId);
        }
        return valid;
    }
}
